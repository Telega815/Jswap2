package ru.jswap.entities;

import java.util.Objects;

public class FileData {
    private Long filePk;
    private String filename;
    private Post post;
    private FilePath filepath;
    private long size;

    public FileData() {
    }

    public FileData(String filename, Post post, FilePath filepath, Long size) {
        this.filename = filename;
        this.post = post;
        this.filepath = filepath;
        this.size = size;
    }

    public Long getFilePk() {
        return filePk;
    }

    public void setFilePk(Long filePk) {
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
                Objects.equals(filepath, fileData.filepath) &&
                size == fileData.size;
    }

    @Override
    public int hashCode() {
        return Objects.hash(filePk,filename,post, filepath, size);
    }

    public FilePath getFilepath() {
        return filepath;
    }

    public void setFilepath(FilePath filepath) {
        this.filepath = filepath;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
