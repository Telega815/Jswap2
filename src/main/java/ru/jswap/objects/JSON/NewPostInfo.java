package ru.jswap.objects.JSON;

public class NewPostInfo {
    private int clientId;
    private long feedId;
    private String postComment;
    private int[] filesToSave;

    public NewPostInfo() {
    }

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public String getPostComment() {
        return postComment;
    }

    public void setPostComment(String postComment) {
        this.postComment = postComment;
    }

    public int[] getFilesToSave() {
        return filesToSave;
    }

    public void setFilesToSave(int[] filesToSave) {
        this.filesToSave = filesToSave;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }
}
