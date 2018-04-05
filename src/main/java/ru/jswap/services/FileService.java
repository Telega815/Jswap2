package ru.jswap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import ru.jswap.dao.intefaces.FilePathDAO;
import ru.jswap.dao.intefaces.FilesDAO;
import ru.jswap.dao.intefaces.PostsDAO;
import ru.jswap.dao.intefaces.UserDAO;
import ru.jswap.entities.*;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

@Component(value = "fileService")
public class FileService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private PostsDAO postsDAO;

    @Autowired
    private FilesDAO filesDAO;

    @Autowired
    private FilePathDAO filePathDAO;

    private List<String> filenames;


    /**
     * Method writes files to hdd and files info to database,
     * calling next method "writeMultipartFile" for each file
     * @param multipartFiles from httprequest
     * @throws IOException
     */
    public Long writeMultipartFiles(MultipartFile[] multipartFiles, Feeds feed, String postid) throws IOException{
        Post post;
        if(postid.equals("next")) {
            Long timeMillis = System.currentTimeMillis();
            post = new Post(feed, new Date(timeMillis), new Time(timeMillis), false);
            filenames = new LinkedList<>();
            post.setPostPk(postsDAO.savePost(post));
        }else{
            post = postsDAO.getPost(Long.valueOf(postid));
        }

        File dir = new File(this.getPostFolder(post));
        if(!dir.exists()){
            dir.mkdirs();
        }
        for (MultipartFile multipartFile: multipartFiles) {
            writeMultipartFile(multipartFile, dir, post);
        }
        return (long) post.getPostPk();
    }
    public void enableFiles(Long id){
        Post post = postsDAO.getPost(id);
        post.setEnabled(true);
        postsDAO.updeatePost(post);
    }

    private void writeMultipartFile(MultipartFile multipartFile, File dir, Post post) throws IOException{
        String filename = multipartFile.getOriginalFilename(), checkingName  = filename;
        int i = 2;
        while (filenames.contains(checkingName)){
            checkingName = filename;
            checkingName += "-"+i;
            i++;
        }
        filename = checkingName;
        filenames.add(filename);
        byte[] bytes = multipartFile.getBytes();

        File uploadedFileDir = new File(dir.getAbsolutePath() + File.separator + filename);
        String path = uploadedFileDir.getAbsolutePath();
        BufferedOutputStream outputStream = new BufferedOutputStream(new FileOutputStream(uploadedFileDir));
        outputStream.write(bytes);
        outputStream.flush();
        outputStream.close();
        FilePath filepath = new FilePath(path);
        filepath.setId(filePathDAO.savePath(filepath));
        FileData fileData = new FileData(filename, post, filepath);
        filesDAO.saveFile(fileData);
    }

    public boolean deleteFile(FileData fileData){
        boolean res = deleteFileFromDatabase(fileData);
        res &= deleteFileFromHDD(fileData);
        deletePost(fileData.getPost(),false);
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



    private String getUserFolder(User user){
        return "C:/tmpFiles" + File.separator + user.getUsername();
    }

    private String getPostFolder(Post post){
        return getUserFolder(post.getFeed().getUser()) + File.separator + post.getDate().toString() +"_"+ post.getTime().toString().replace(':', '-');
    }

    public String getFilePath(FileData fileData){
        return this.getPostFolder(fileData.getPost()) + File.separator + fileData.getFilename();
    }

    public FileData getFile(String filename, int postid){
        return filesDAO.getFile(filename, postid);
    }

    public Post getPost(Long postid){
        return postsDAO.getPost(postid);
    }
}
