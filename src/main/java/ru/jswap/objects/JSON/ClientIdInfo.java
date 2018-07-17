package ru.jswap.objects.JSON;

public class ClientIdInfo {
    private String[] filenames;
    private Integer[] fileIds;
    private Long[] fileSizes;
    private boolean haveFiles;

    public ClientIdInfo() {
    }

    public String[] getFilenames() {
        return filenames;
    }

    public void setFilenames(String[] filenames) {
        this.filenames = filenames;
    }

    public Integer[] getFileIds() {
        return fileIds;
    }

    public void setFileIds(Integer[] fileIds) {
        this.fileIds = fileIds;
    }

    public boolean isHaveFiles() {
        return haveFiles;
    }

    public void setHaveFiles(boolean haveFiles) {
        this.haveFiles = haveFiles;
    }

    public Long[] getFileSizes() {
        return fileSizes;
    }

    public void setFileSizes(Long[] fileSizes) {
        this.fileSizes = fileSizes;
    }
}
