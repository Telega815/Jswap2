package ru.jswap.objects;

public class RequestPostInfo {
    private String comment;
    private String feedName;
    private long postID;
    private long[] filesToDelete;

    public RequestPostInfo() {
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getFeedName() {
        return feedName;
    }

    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public long getPostID() {
        return postID;
    }

    public void setPostID(long postID) {
        this.postID = postID;
    }

    public long[] getFilesToDelete() {
        return filesToDelete;
    }

    public void setFilesToDelete(long[] filesToDelete) {
        this.filesToDelete = filesToDelete;
    }
}
