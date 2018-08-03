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
import ru.jswap.objects.JSON.RequestPostInfo;
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
    private Map<Long, TempPost> tempForExistingPosts;

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
        fileId = post.addFile(multipartFile, (long) clientId, sessionId, key);
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

    public Post saveNewPost(NewPostInfo info, Feeds feed, String createdBy){
        Post post = new Post();
        post.setCommentary(info.getPostComment());
        File feedPath = new File(this.getFeedFolder(feed));
        if(!feedPath.exists()){
            if (!feedPath.mkdirs()) {
                logger.info("\n{}: WARNING! = directory creation failed; path = {}", new java.util.Date(System.currentTimeMillis()).toString(), feedPath.getAbsolutePath());
                return null;
            }
        }
        post.setCreatedBy(createdBy);
        post.setFeed(feed);
        TempPost tempPost = tempPosts.get(info.getClientId());
        post.setSize(tempPost.getPostSize());
        long currTime = System.currentTimeMillis();
        post.setDate(new java.sql.Date(currTime));
        post.setTime(new Time(currTime));
        post.setPostPk(postsDAO.savePost(post));

        boolean res = true;
        for (int key : info.getFilesToSave()) {
            res &= tempPost.saveFileToDb(key, feedPath, post);
        }
        if (!res){
            logger.info("\n{}: WARNING! = saving to db failed;", new java.util.Date(System.currentTimeMillis()).toString());
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
    //----------------------------------post edit-----------------------------------------------------
    public boolean uploadFileToExistingPost(Long postId, MultipartFile file, int fileId, String sessionId){
        TempPost tempPost;
        if (tempForExistingPosts.containsKey(postId)){
            tempPost = tempForExistingPosts.get(postId);
        }else{
            tempPost = new TempPost(filesDAO, filePathDAO);
            tempForExistingPosts.put(postId, tempPost);
        }
        return tempPost.addFile(file, postId, sessionId, fileId) != -1;
    }

    public boolean deleteFileFromTempForExistingPost(Long postId, int fileId){
        TempPost tempPost;
        if (tempForExistingPosts.containsKey(postId)){
            tempPost = tempForExistingPosts.get(postId);
            return tempPost.deleteFile(fileId);
        }else{
            return false;
        }
    }

    public Post updateExistingPost(RequestPostInfo info, Post post){
        TempPost tempPost = tempForExistingPosts.get(info.getPostID());
        File newLocation = new File(this.getFeedFolder(post.getFeed()));
        boolean res = true;
        for (int key : info.getFilesToSave()) {
            res &= tempPost.saveFileToDb(key, newLocation, post);
        }
        if (!res){
            logger.info("\n{}: WARNING! = saving to db failed;", new java.util.Date(System.currentTimeMillis()).toString());
        }
        this.resizePost(post, tempPost.getPostSize());
        tempForExistingPosts.put(info.getPostID(), new TempPost(filesDAO, filePathDAO));
        return post;
    }


    // Methods for file paths-------------------------------------------------------------------------------------------

    private String getUserFolder(User user){
        return "D:" + File.separator + "anal69" + File.separator + "tmpFiles" + File.separator + user.getUsername();
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
