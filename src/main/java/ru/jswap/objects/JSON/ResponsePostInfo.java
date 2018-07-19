package ru.jswap.objects.JSON;

public class ResponsePostInfo {
    private String htmlPost;
    private long postId;
    private boolean nullPost;
    private long feedSize;
    private long userSpace;

    public ResponsePostInfo() {
    }

    public String getHtmlPost() {
        return htmlPost;
    }

    public void setHtmlPost(String htmlPost) {
        this.htmlPost = htmlPost;
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(long postId) {
        this.postId = postId;
    }

    public boolean isNullPost() {
        return nullPost;
    }

    public void setNullPost(boolean nullPost) {
        this.nullPost = nullPost;
    }

    public long getFeedSize() {
        return feedSize;
    }

    public void setFeedSize(long feedSize) {
        this.feedSize = feedSize;
    }

    public long getUserSpace() {
        return userSpace;
    }

    public void setUserSpace(long userSpace) {
        this.userSpace = userSpace;
    }
}
