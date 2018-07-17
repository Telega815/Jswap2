package ru.jswap.objects.JSON;

public class ResponsePostInfo {
    private String htmlPost;
    private long postId;
    private boolean nullPost;

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
}
