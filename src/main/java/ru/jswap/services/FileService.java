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
import ru.jswap.objects.TempPost;

import java.io.File;
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
        tempPosts.put(maxId, new TempPost());
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

    public void saveNewPost(NewPostInfo info){
        Post post = new Post();
        TempPost tempPost = tempPosts.get(info.getClientId());

        for (int key : info.getFilesToSave()) {
            FilePath filePath = tempPost.getPath(key);
            FileData fileData = tempPost.getFileName(key);

            filePath.setPath(this.getFilePath(fileData));

        }
    }



// OLD STAFF
    public void postEditDeleteFiles(long[] filesToDelete){
        logger.info("\n{}: started postEditDeleteFiles(long[] filesToDelete);", new java.util.Date(System.currentTimeMillis()).toString());
        if(filesToDelete.length != 0){
            for (int i = 0; i < filesToDelete.length; i++) {
                deleteFile(filesDAO.getFile(filesToDelete[i]));
            }
            logger.info("\n{}: finished postEditDeleteFiles(long[] filesToDelete);", new java.util.Date(System.currentTimeMillis()).toString());
            return;
        }
        logger.info("\n{}: finished postEditDeleteFiles(long[] filesToDelete); filesToDelete was empty", new java.util.Date(System.currentTimeMillis()).toString());
    }

    //------------------------------------------------------------------------------------------------------------------

    public boolean deleteFile(FileData fileData){
        Post post = fileData.getPost();
        boolean res = deleteFileFromDatabase(fileData);
        res &= deleteFileFromHDD(fileData);
        if (res) {
            resizePost(fileData.getPost(), -(fileData.getSize()));
        }
        deletePost(post,false);
        return res;
    }

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



    public boolean deletePost(Post post, boolean recursive){
        boolean res = true;
        List<FileData> fileDataList = filesDAO.getFiles(post);
        User user = post.getFeed().getUser();
        if (!fileDataList.isEmpty() && recursive){
            for (FileData fileData:fileDataList) {
                res &= deleteFile(fileData);
            }
        }else if (!fileDataList.isEmpty()) return false;

        res &= deletePostFromDatabase(post);
        res &= deletePostFromHDD(post);
        if(res){
            resizeFeed(post.getFeed(), -(post.getSize()));
        }
        return res;
    }

    private boolean deletePostFromHDD(Post post){
        File file = new File(getPostFolder(post));
        return file.delete();
    }

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
        return getUserFolder(feed.getUser()) + File.separator + feed.getFeedname();
    }

    private String getPostFolder(Post post){
        return getFeedFolder(post.getFeed()) + File.separator + post.getPostPk().toString();
    }

    public String getFilePath(FileData fileData){
        return this.getPostFolder(fileData.getPost()) + File.separator + fileData.getFilename();
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
    public void deletePosts(Feeds feed){
        List<Post> posts = getPosts(feed);
        for (Post post: posts) {
            deletePost(post, true);
        }
    }

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
