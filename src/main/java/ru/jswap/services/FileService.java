package ru.jswap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jswap.dao.intefaces.*;
import ru.jswap.entities.*;
import ru.jswap.objects.JSON.ClientIdInfo;
import ru.jswap.objects.JSON.NewPostInfo;
import ru.jswap.objects.JSON.ResponsePostInfo;
import ru.jswap.objects.TempPost;

import java.io.File;
import java.sql.Time;
import java.util.*;

@Component(value = "fileService")
public class FileService {

    //--------------------------------------------------------------------
    private Logger logger = LoggerFactory.getLogger(FileService.class);
    private int maxId = 0;
    private Map<Integer,TempPost> tempPosts;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private FeedsDAO feedsDAO;

    @Autowired
    private PostsDAO postsDAO;

    @Autowired
    private FilesDAO filesDAO;

    @Autowired
    private FilePathDAO filePathDAO;


    /**
     * @return client ID (will be used to distinguish between different tabs)
     */
    public Integer getAndIncrementMaxId(){
        if(tempPosts == null) tempPosts = new HashMap<>();
        tempPosts.put(maxId, new TempPost(filesDAO, filePathDAO));
        maxId++;
        return maxId-1;
    }

    /**
     * @param clientId received from front end (Used to distinguish between different tabs)
     * @return ClientIdInfo if client have any files
     */
    public ClientIdInfo getClientIdInfo(int clientId){
        return tempPosts.get(clientId).getClientIdInfo();
    }


    /**
     * @param multipartFile uploaded file
     * @param clientId received from front end (Used to distinguish between different tabs)
     * @param sessionId should be passed from controller (User for a temp folder name)
     * @param key file ID on front end
     * @return ID of file in temp post
     */
    public int fileUploadProcess(MultipartFile multipartFile, int clientId, String sessionId, int key){
        int fileId;
        TempPost post;

        if(!tempPosts.containsKey(clientId)){
            return -1;
        }
        post = tempPosts.get(clientId);
        fileId = post.addFile(multipartFile, clientId, sessionId, key);
        if(fileId >= 0) {
            tempPosts.put(clientId, post);
        }
        return fileId;
    }

    /**
     * @param clientId received from front end (Used to distinguish between different tabs)
     * @param fileId ID of file to delete
     * @return true if succeeded
     */
    public boolean deleteTmpFile(int clientId, int fileId){
        TempPost post=tempPosts.get(clientId);
        return post.deleteFile(fileId);
    }

    public Post saveNewPost(NewPostInfo info){
        ResponsePostInfo responsePostInfo = new ResponsePostInfo();
        Post post = new Post();
        post.setCommentary(info.getPostComment());
        Feeds feed = feedsDAO.getFeed(info.getFeedId());
        File feedPath = new File(this.getFeedFolder(feed));
        if(!feedPath.exists()){
            if (!feedPath.mkdirs()) {
                logger.info("\n{}: WARNING! = directory creation failed; path = {}", new java.util.Date(System.currentTimeMillis()).toString(), feedPath.getAbsolutePath());
                return null;
            }
        }

        post.setFeed(feed);
        TempPost tempPost = tempPosts.get(info.getClientId());
        post.setSize(tempPost.getPostSize());
        long currTime = System.currentTimeMillis();
        post.setDate(new java.sql.Date(currTime));
        post.setTime(new Time(currTime));
        post.setPostPk(postsDAO.savePost(post));

        for (int key : info.getFilesToSave()) {
            tempPost.saveFileToDb(key, feedPath, post);
        }
        tempPosts.replace(info.getClientId(), new TempPost(filesDAO, filePathDAO));
        resizeFeed(feed, post.getSize());
        return post;
    }

    public boolean deleteFile(long fileId){
        FileData fileData = filesDAO.getFile(fileId);
        return deleteFile(fileData);
    }

    private boolean deleteFile(FileData fileData){
        boolean res;
        File file = new File(fileData.getFilepath().getPath() + File.separator + fileData.getFilepath().getId());
        res = file.delete();
        filesDAO.deleteFile(fileData);
        filePathDAO.deletePath(fileData.getFilepath());
        this.resizePost(fileData.getPost(), -fileData.getSize());
        return res;
    }

    public Post deletePostRecursive(long postId){
        boolean res = true;
        Post post = postsDAO.getPost(postId);
        List<FileData> fileDataList = filesDAO.getFiles(post);
        for (FileData fileData: fileDataList) {
            res &= deleteFile(fileData);
        }
        postsDAO.deletePost(post);
        this.resizeFeed(post.getFeed(), -post.getSize());
        if (res){
            return post;
        }
        else{
            return null;
        }
    }



// OLD STAFF
//    public void postEditDeleteFiles(long[] filesToDelete){
//        logger.info("\n{}: started postEditDeleteFiles(long[] filesToDelete);", new java.util.Date(System.currentTimeMillis()).toString());
//        if(filesToDelete.length != 0){
//            for (int i = 0; i < filesToDelete.length; i++) {
//                deleteFile(filesDAO.getFile(filesToDelete[i]));
//            }
//            logger.info("\n{}: finished postEditDeleteFiles(long[] filesToDelete);", new java.util.Date(System.currentTimeMillis()).toString());
//            return;
//        }
//        logger.info("\n{}: finished postEditDeleteFiles(long[] filesToDelete); filesToDelete was empty", new java.util.Date(System.currentTimeMillis()).toString());
//    }

    //------------------------------------------------------------------------------------------------------------------

//    public boolean deleteFile(FileData fileData){
//        Post post = fileData.getPost();
//        boolean res = deleteFileFromDatabase(fileData);
//        res &= deleteFileFromHDD(fileData);
//        if (res) {
//            resizePost(fileData.getPost(), -(fileData.getSize()));
//        }
//        deletePost(post,false);
//        return res;
//    }

    private boolean deleteFileFromDatabase(FileData fileData){
        try {
            FilePath filepath = fileData.getFilepath();
            filesDAO.deleteFile(fileData);
            filePathDAO.deletePath(filepath);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteFileFromHDD(FileData fileData){
        File file = new File(fileData.getFilepath().getPath());
        return file.delete();
    }



//    public boolean deletePost(Post post, boolean recursive){
//        boolean res = true;
//        List<FileData> fileDataList = filesDAO.getFiles(post);
//        User user = post.getFeed().getUser();
//        if (!fileDataList.isEmpty() && recursive){
//            for (FileData fileData:fileDataList) {
//                res &= deleteFile(fileData);
//            }
//        }else if (!fileDataList.isEmpty()) return false;
//
//        res &= deletePostFromDatabase(post);
//        res &= deletePostFromHDD(post);
//        if(res){
//            resizeFeed(post.getFeed(), -(post.getSize()));
//        }
//        return res;
//    }

//    private boolean deletePostFromHDD(Post post){
//        File file = new File(getPostFolder(post));
//        return file.delete();
//    }

    private boolean deletePostFromDatabase(Post post){
        try {
            postsDAO.deletePost(post);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    // Methods for file paths-------------------------------------------------------------------------------------------

    private String getUserFolder(User user){
        return "\\\\DESKTOP-JJNRSE9"+File.separator+"folder_for_swapy" + File.separator + "tmpFiles" + File.separator + user.getUsername();
    }

    private String getFeedFolder(Feeds feed){
        return getUserFolder(feed.getUser()) + File.separator + feed.getFeedname() + File.separator;
    }

    //------------------------------------------------------------------------------------------------------------------


    public FileData getFile(String filename, int postid){
        return filesDAO.getFile(filename, postid);
    }

    public Post getPost(Long postid){
        return postsDAO.getPost(postid);
    }

    public List<Post> getPosts(Feeds feed){
        return postsDAO.getPosts(feed);
    }


//    public void deletePosts(Feeds feed){
//        List<Post> posts = getPosts(feed);
//        for (Post post: posts) {
//            deletePost(post, true);
//        }
//    }

    // Methods for size calculation-------------------------------------------------------------------------------------------
    private void resizeUser(User user, long size){
        user.setFilesSize(user.getFilesSize()+size);
        userDAO.updateUser(user);
    }

    private void resizeFeed(Feeds feed, long size){
        feed.setSize(feed.getSize()+size);
        resizeUser(feed.getUser(), size);
        feedsDAO.updateFeed(feed);
    }
    private void resizePost(Post post, long size){
        post.setSize(post.getSize()+size);
        resizeFeed(post.getFeed(), size);
        postsDAO.updatePost(post);
    }
    // ----------------------------------------------------------------------------------------------------------------------
}
