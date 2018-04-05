package ru.jswap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jswap.dao.intefaces.FilesDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.FileData;
import ru.jswap.entities.Post;

import java.util.List;

@Service
public class HtmlService {

    private final String WEBSITE = "http://localhost:8080";
    private final String OPEN_BLOCK = "<div style=\" border: 3px solid\">\n";
    private final String CLOSE_BLOCK = "</div>";

    @Autowired
    private FilesDAO filesDAO;

    public String getFeedsHtml(List<Feeds> feeds, boolean showPrivateContent){
        StringBuilder res = new StringBuilder();
        for (Feeds feed: feeds) {
            res.append(getFeedHtml(feed, showPrivateContent));
            res.append("<br/>");
        }
        return res.toString();
    }

    public String getFeedHtml(Feeds feed, boolean showPrivateContent){
        return "<a href=\"" + WEBSITE + "/" +
                feed.getUser().getUsername() + "/" +
                feed.getFeedname() + "\">Feed: " + feed.getFeedname() + "</a>";

    }

    public String getPostHtml(Post post, boolean showPrivateContent){
        if (!post.isEnabled()) return "";
        StringBuilder viewString = new StringBuilder(OPEN_BLOCK);
        List<FileData> files = filesDAO.getFiles(post);
        for (FileData fileData: files) {
            viewString.append(getFileHtml(fileData, showPrivateContent));
        }
        viewString.append(CLOSE_BLOCK);


        if(showPrivateContent){
            viewString.append("<a href=\"" + WEBSITE + "/")
                    .append(post.getFeed().getUser().getUsername())
                    .append("/")
                    .append(post.getFeed().getFeedname())
                    .append("/delete/")
                    .append(post.getPostPk())
                    .append("\">Удалить пост с сервера</a> <br />");
        }

        return viewString.toString();
    }

    private String getFileHtml(FileData fileData, boolean showPrivateContent){
        StringBuilder viewString = new StringBuilder();
        viewString.append("<a href=\"" + WEBSITE + "/")
                .append(fileData.getPost().getFeed().getUser().getUsername())
                .append("/")
                .append(fileData.getPost().getFeed().getFeedname())
                .append("/download/").append(fileData.getPost().getPostPk())
                .append("/").append(fileData.getFilename()).append("\">")
                .append(fileData.getFilename()).append("</a> <br />");
        if(showPrivateContent) {
            viewString.append("<a href=\"" + WEBSITE + "/")
                    .append(fileData.getPost().getFeed().getUser().getUsername())
                    .append("/")
                    .append(fileData.getPost().getFeed().getFeedname())
                    .append("/delete/").append(fileData.getPost().getPostPk()).append("/")
                    .append(fileData.getFilename()).append("\">Удалить файл с сервера</a> <br />");
        }
        return viewString.toString();
    }
}
