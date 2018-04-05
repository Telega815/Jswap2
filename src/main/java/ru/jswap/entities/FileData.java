package ru.jswap.entities;

import java.util.Objects;

public class FileData {
    private int filePk;
    private String filename;
    private Post post;
    private FilePath filepath;

    public FileData() {
    }

    public FileData(String filename, Post post, FilePath filepath) {
        this.filename = filename;
        this.post = post;
        this.filepath = filepath;
    }

    public int getFilePk() {
        return filePk;
    }

    public void setFilePk(int filePk) {
        this.filePk = filePk;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FileData fileData = (FileData) o;
        return filePk == fileData.filePk &&
                Objects.equals(post,fileData.post) &&
                Objects.equals(filename, fileData.filename)&&
                Objects.equals(filepath, fileData.filepath);
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePk,filename,post, filepath);
    }

    public FilePath getFilepath() {
        return filepath;
    }

    public void setFilepath(FilePath filepath) {
        this.filepath = filepath;
    }
}
