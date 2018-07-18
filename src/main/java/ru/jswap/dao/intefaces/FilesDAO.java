package ru.jswap.dao.intefaces;

import ru.jswap.entities.FileData;
import ru.jswap.entities.Post;

import java.util.List;

public interface FilesDAO {

    List<FileData> getFiles();
    FileData getFile(long fileId);
    FileData getFile(String filename, Post post);
    FileData getFile(String filename, Integer postid);
    List<FileData> getFiles(Post post);
    List<FileData> getFiles(Post[] posts);
    void saveFile(FileData fileData);
    void deleteFile(FileData fileData);
    void deleteFile(long fileId);
    void updatefile(FileData fileData);
}
