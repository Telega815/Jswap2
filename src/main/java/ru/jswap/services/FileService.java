package ru.jswap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.jswap.dao.intefaces.*;
import ru.jswap.entities.*;
import ru.jswap.objects.RequestPostInfo;
import ru.jswap.objects.TempPost;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.*;

@Component(value = "fileService")
public class FileService implements Runnable {

    private final int MILLS = 180000;
    private User tempPostOwner;
    private Post tempPost;
    private List<FileData> tempFiles;
    private List<FilePath> tempFilePaths;
    private List<String> filenames;
    private Thread timeOut;
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


    public Integer getAndIncrementMaxId(){
        maxId++;
        return maxId-1;
    }


    /**
     * @param multipartFile uploaded file
     * @param clientId received from front end (Used to distinguish between different tabs)
     * @param sessionId should be passed from controller (User for a temp folder name)
     * @return ID of file in temp post
     */
    public int fileUploadProcess(MultipartFile multipartFile, int clientId, String sessionId){
        if(tempPosts == null) tempPosts = new HashMap<>();
        int fileId;
        TempPost post;

        if(tempPosts.containsKey(clientId)){
            post = tempPosts.get(clientId);
        }else{
            post = new TempPost();
        }
        fileId = post.addFile(multipartFile, clientId, sessionId);
        if(fileId >= 0) {
            tempPosts.put(clientId, post);
        }
        return fileId;
    }





// OLD STAFF
    //Saving file to temp post------------------------------------------------------------------------------------------
    public int saveMultipartFile(MultipartFile multipartFile, User user){
        if (tempPost == null) formNewTempPost();
        if (tempPostOwner == null) tempPostOwner = user;

        if (!writeFileToTempPost(multipartFile)) return -1;

        this.startTimeOut();
        return 1;
    }

    private boolean writeFileToTempPost(MultipartFile multipartFile){
        logger.info("\n{}: started writeFileToTempPost(MultipartFile multipartFile)", new java.util.Date(System.currentTimeMillis()).toString());
        String filename = this.chooseName(multipartFile.getOriginalFilename());

        File file = new File(this.getPostFolder(tempPost) + File.separator + filename);

        if (!file.exists())
            if (!file.mkdirs())
                return false;

        FilePath filePath = new FilePath(file.getAbsolutePath());

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        tempFilePaths.add(filePath);

        FileData fileData = new FileData(filename, null, filePath, multipartFile.getSize());
        tempFiles.add(fileData);
        tempPost.setSize(tempPost.getSize() + multipartFile.getSize());
        logger.info("\n{}: finished writeFileToTempPost(MultipartFile multipartFile)", new java.util.Date(System.currentTimeMillis()).toString());
        return true;
    }

    private String chooseName(String filename){
        logger.info("\n{}: started chooseName(String filename) filename = "+ filename, new java.util.Date(System.currentTimeMillis()).toString());
        String checkedName = filename;
        if (filenames == null){
            filenames = new LinkedList<>();
        }else{
            int i = 0;
            while (filenames.contains(checkedName)){
                checkedName = filename;
                checkedName += "-" + i;
                i++;
            }
        }

        filenames.add(checkedName);
        logger.info("\n{}: finished chooseName(String filename) filename = "+ checkedName, new java.util.Date(System.currentTimeMillis()).toString());
        return checkedName;
    }

    private void formNewTempPost(){
        logger.info("\n{}: started formNewTempPost()", new java.util.Date(System.currentTimeMillis()).toString());
        Long timeMillis = System.currentTimeMillis();
        tempPost = new Post(null, new Date(timeMillis), new Time(timeMillis), false);
        tempPost.setPostPk(1L);
        filenames = new LinkedList<>();
        tempFilePaths = new ArrayList<>();
        tempFiles = new ArrayList<>();
        logger.info("\n{}: finished formNewTempPost()", new java.util.Date(System.currentTimeMillis()).toString());
    }

    private void destroyTempObjects(){
        logger.info("\n{}: started destroyTempObjects()", new java.util.Date(System.currentTimeMillis()).toString());
        tempPost = null;
        tempPostOwner = null;
        filenames = null;
        tempFilePaths = null;
        tempFiles = null;
        timeOut = null;
        logger.info("\n{}: finished destroyTempObjects()", new java.util.Date(System.currentTimeMillis()).toString());
    }


    public boolean deleteFileFromTempPost(String filename){
        logger.info("\n{}: started deleteFileFromTempPost(String filename)", new java.util.Date(System.currentTimeMillis()).toString());
        int index = -1;
        for (int i = 0; i < tempFiles.size(); i++) {
            if (tempFiles.get(i).getFilename().equals(filename)) {
                index = i;
                break;
            }
        }
        if (index >= 0) {
            deleteFileFromHDD(tempFiles.get(index));
            tempFiles.remove(index);
            tempFilePaths.remove(index);
            logger.info("\n{}: finished deleteFileFromTempPost(String filename); file deleted", new java.util.Date(System.currentTimeMillis()).toString());
            return true;
        }
        logger.info("\n{}: finished deleteFileFromTempPost(String filename); file wasn't deleted", new java.util.Date(System.currentTimeMillis()).toString());
        return false;
    }

    public Post saveTempPost(RequestPostInfo info) throws IOException{
        logger.info("\n{}: started saveTempPost(RequestPostInfo info)", new java.util.Date(System.currentTimeMillis()).toString());
        if(info==null){
            logger.info("\n{}  WARNING: finished saveTempPost(RequestPostInfo info) RequestPostInfo was null!!!", new java.util.Date(System.currentTimeMillis()).toString());
            return null;
        }
        if(info.getPostID() != -1) {
            logger.info("\n{}: finished saveTempPost(RequestPostInfo info); postId != -1(editing existing post)", new java.util.Date(System.currentTimeMillis()).toString());
            return updateExistingPost(info);
        }

        if (timeOut!= null  && timeOut.isAlive()){
            timeOut.interrupt();
            if(tempFiles.isEmpty()) return null;

            tempPost.setEnabled(true);
            if (!info.getComment().equals("")) tempPost.setCommentary(info.getComment());
            tempPost.setFeed(feedsDAO.getFeed(info.getFeedName(), tempPostOwner));
            tempPost.setPostPk(postsDAO.savePost(tempPost));
            this.relocatePost(true);
            this.resizeFeed(tempPost.getFeed(), tempPost.getSize());

            Post res = tempPost;
            this.destroyTempObjects();

            logger.info("\n{}: finished saveTempPost(RequestPostInfo info); successfully", new java.util.Date(System.currentTimeMillis()).toString());
            return res;
        }else{
            logger.info("\n{}: finished saveTempPost(RequestPostInfo info); time out was dead", new java.util.Date(System.currentTimeMillis()).toString());
            return null;
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private Post updateExistingPost(RequestPostInfo info){
        logger.info("\n{}: started updateExistingPost(RequestPostInfo info);", new java.util.Date(System.currentTimeMillis()).toString());
        tempPost = postsDAO.getPost(info.getPostID());
        if (!info.getComment().equals("")) tempPost.setCommentary(info.getComment());
        else tempPost.setCommentary("");
        if (timeOut != null && timeOut.isAlive()) {
            this.relocatePost(false);
            postsDAO.updatePost(tempPost);
            Post res = tempPost;
            this.destroyTempObjects();
            logger.info("\n{}: finished updateExistingPost(RequestPostInfo info); timeOut was alive", new java.util.Date(System.currentTimeMillis()).toString());
            return res;
        }else{
            postsDAO.updatePost(tempPost);
            Post res = tempPost;
            this.destroyTempObjects();
            logger.info("\n{}: finished updateExistingPost(RequestPostInfo info); timeOut was dead or null", new java.util.Date(System.currentTimeMillis()).toString());
            return res;
        }
    }
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

    private boolean relocatePost(boolean tmp){
        logger.info("\n{}: started relocatePost(boolean tmp);", new java.util.Date(System.currentTimeMillis()).toString());
        for (int i = 0; i < tempFiles.size(); i++) {
            FilePath filePath = tempFilePaths.get(i);
            FileData fileData = tempFiles.get(i);
            fileData.setPost(tempPost);

            File tempLocation = new File(filePath.getPath());
            File permLocation = new File(getFilePath(fileData));

            if (!permLocation.getParentFile().exists())
                if (!permLocation.getParentFile().mkdirs()) {
                    logger.info("\n{}: finished relocatePost(boolean tmp); mkdirs failed", new java.util.Date(System.currentTimeMillis()).toString());
                    return false;
                }
            //TODO ITS NOT WORKING!!!!
            if(!tempLocation.renameTo(permLocation)){
                logger.info("\n{}: finished relocatePost(boolean tmp); relocate failed! TempLocation: {}, permLocation: {}", new java.util.Date(System.currentTimeMillis()).toString(),tempLocation.getAbsolutePath(), permLocation.getAbsolutePath());
                return false;
            }

            filePath.setPath(permLocation.getAbsolutePath());

            filePath.setId(filePathDAO.savePath(filePath));
            fileData.setFilepath(filePath);
            filesDAO.saveFile(fileData);
            if(!tmp){
                resizePost(tempPost ,fileData.getSize());
            }
        }
        logger.info("\n{}: finished relocatePost(boolean tmp);", new java.util.Date(System.currentTimeMillis()).toString());
        return true;
    }

    //------------------------------------------------------------------------------------------------------------------

    private void startTimeOut(){
        if (timeOut == null) {
            timeOut = new Thread(this);
        }
        if (timeOut.isAlive()){
            timeOut.interrupt();
        }
        timeOut = new Thread(this);
        timeOut.start();
    }

    @Override
    public void run() {
        try {
            Thread.sleep(MILLS);
            tempPost = null;
            for (FileData fileData : tempFiles) {
                deleteFileFromHDD(fileData);
            }
            tempFiles.clear();
            tempFilePaths.clear();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(FileData fileData){
        Post post = fileData.getPost();
        if (fileData.getPost().isEnabled()&&
                timeOut!=null&&
                timeOut.isAlive()) {
            this.startTimeOut();
        }
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
        return "C:/tmpFiles" + File.separator + user.getUsername();
    }

    private String getFeedFolder(Feeds feed){
        return getUserFolder(feed.getUser()) + File.separator + feed.getFeedname();
    }

    private String getPostFolder(Post post){
        if (post.isEnabled())
            return getFeedFolder(post.getFeed()) + File.separator + post.getPostPk().toString();
        else
            return getUserFolder(tempPostOwner) + File.separator + "temp";
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
