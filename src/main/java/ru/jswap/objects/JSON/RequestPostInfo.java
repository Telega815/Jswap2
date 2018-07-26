package ru.jswap.objects.JSON;

public class RequestPostInfo {
    private String comment;
    private long feedId;
    private long postID;
    private long[] filesToDelete;
    private int[] filesToSave;

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

    public int[] getFilesToSave() {
        return filesToSave;
    }

    public void setFilesToSave(int[] filesToSave) {
        this.filesToSave = filesToSave;
    }
}
