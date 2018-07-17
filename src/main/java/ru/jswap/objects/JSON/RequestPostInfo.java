package ru.jswap.objects.JSON;

public class RequestPostInfo {
    private String comment;
    private long feedId;
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

    public long getFeedName() {
        return feedId;
    }

    public void setFeedName(long feedId) {
        this.feedId = feedId;
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
