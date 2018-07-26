package ru.jswap.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.jswap.dao.intefaces.FilesDAO;
import ru.jswap.dao.intefaces.PostsDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.FileData;
import ru.jswap.entities.Post;
import ru.jswap.objects.AccessParams;
import ru.jswap.services.Implementations.HtmlGeneratorImpl;
import ru.jswap.services.interfaces.HtmlGenerator;

import java.util.ArrayList;
import java.util.List;

@Service
public class HtmlService {


    @Autowired
    private PostsDAO postsDAO;
    @Autowired
    private FilesDAO filesDAO;

    public String getFeedsAsOptions(List<Feeds> feeds){
        StringBuilder res = new StringBuilder();
        HtmlGenerator option = new HtmlGeneratorImpl("option");
        if(feeds.isEmpty())
            return "";
        for (Feeds feed : feeds) {
            option.setInnerText(feed.getFeedname());
            res.append(option.toString());
        }
        return res.toString();
    }

    public String getFeedsHtml(List<Feeds> feeds, boolean showPrivateContent){
        StringBuilder res = new StringBuilder();
        for (Feeds feed:feeds) {
            res.append(this.getFeedHtml(feed, showPrivateContent));
        }
        return res.toString();
    }

    public String getFeedHtml(Feeds feed, boolean showPrivateContent){
        HtmlGenerator tr = new HtmlGeneratorImpl("tr");

        //tdLeft---------------------------------------------------------------------------------
        HtmlGenerator tdLeft = new HtmlGeneratorImpl("td");
        tdLeft.addAttribute("class", "tdLeft");
        HtmlGenerator img = new HtmlGeneratorImpl("img");
        if(feed.getAccesstype()==0 || feed.getAccesstype()==3 || feed.getAccesstype()==6 ){
            img.addAttribute("hidden", "hidden");
        }
        img.addAttribute("src", "../../resources/media/feeds/locked.png");
        tdLeft.setInnerText(img.toString());


        //tdCenter-------------------------------------------------------------------------------
        HtmlGenerator tdCenter = new HtmlGeneratorImpl("td");
        tdCenter.addAttribute("class", "tdCenter");

        HtmlGenerator inputHidden = new HtmlGeneratorImpl("input");
        inputHidden.addAttribute("type", "radio");
        inputHidden.addAttribute("name", "emotion");
        inputHidden.addAttribute("id", "pic_"+feed.getId());
        inputHidden.addAttribute("class", "input-hidden");

        HtmlGenerator label = new HtmlGeneratorImpl("label");
        label.addAttribute("for", "pic_"+feed.getId());
        HtmlGenerator span = new HtmlGeneratorImpl("span");
        span.addAttribute("id", "feed_"+feed.getId());
        span.addAttribute("class","feeds");

        span.setInnerText(feed.getFeedname());
        label.setInnerText(span.toString());

        tdCenter.setInnerText(inputHidden.toString() + label.toString());

        //tdRight--------------------------------------------------------------------
        HtmlGenerator tdRight = new HtmlGeneratorImpl("td");
        tdRight.addAttribute("class", "tdRight");
        tdRight.addAttribute("id", "feedTdRight_"+feed.getId());

        double sizeByte = feed.getSize();
        //sizeSpaceOfFeeds
        tdRight.setInnerText(String.valueOf(sizeByte));

        //tdEditFeed--------------------------------------------------------------------
        if (showPrivateContent) {
            HtmlGenerator tdEditFeed = new HtmlGeneratorImpl("td");
            tdEditFeed.addAttribute("class", "tdEditFeed");
            tdEditFeed.addAttribute("style", "display: none;");
            HtmlGenerator tdEditFeedImg = new HtmlGeneratorImpl("img");
            tdEditFeedImg.addAttribute("src", "../../resources/media/feeds/edit.png");

            tdEditFeed.setInnerText(tdEditFeedImg.toString());
            tr.setInnerText(tdLeft.toString() + tdCenter.toString() + tdRight.toString() + tdEditFeed.toString());
        }else{
            tr.setInnerText(tdLeft.toString() + tdCenter.toString() + tdRight.toString());
        }
        return tr.toString();
    }

    public String getAllPostsHtml(Feeds feed, boolean showPrivateContent){
        StringBuilder response = new StringBuilder();
        List<Post> posts = postsDAO.getPosts(feed);
        if (posts.isEmpty())
            return null;

        posts.sort((o1, o2) -> {
            if (o1.getPostPk() > o2.getPostPk()) return -1;
            else return 1;
        });

        for (int i = 0; i < posts.size(); i++) {
            response.append(getPostHtml(posts.get(i), showPrivateContent));
        }

        return response.toString();
    }


    public String getPostHtml(Post post, boolean showPrivateContent){



        HtmlGenerator feedContainer = new HtmlGeneratorImpl("div");
        feedContainer.addAttribute("class", "FeedContainer");
        feedContainer.addAttribute("id", "FeedContainer_" + post.getPostPk());

        //postHeader---------------------------------------------------------------
        HtmlGenerator postHeader = new HtmlGeneratorImpl("div");
        postHeader.addAttribute("class", "FeedHeader");
        //postHeaderLeft
        HtmlGenerator postHeaderLeft = new HtmlGeneratorImpl("div");
        postHeaderLeft.addAttribute("class", "FeedHeaderLeft");

        HtmlGenerator postHeaderLeftImg = new HtmlGeneratorImpl("img");
        postHeaderLeftImg.addAttribute("class", "FeedLogo");
        postHeaderLeftImg.addAttribute("src", "../../resources/media/feeds/nick_name_B.png");

        HtmlGenerator postText = new HtmlGeneratorImpl("div");
        postText.addAttribute("class", "FeedText");
//
        HtmlGenerator postHeaderName = new HtmlGeneratorImpl("p");
        postHeaderName.addAttribute("class", "FeedHeaderText");
        postHeaderName.setInnerText(post.getCreatedBy().toUpperCase());

        HtmlGenerator postHeaderDate = new HtmlGeneratorImpl("p");
        postHeaderDate.addAttribute("class", "FeedHeaderTextTime");
        postHeaderDate.setInnerText(post.getDate().toString().replace('-', '.') + (" ") + (post.getTime().toString().substring(0, 5)));

        postText.setInnerText(postHeaderName.toString()+postHeaderDate.toString());

        postHeaderLeft.setInnerText(postHeaderLeftImg.toString()+postText.toString());




        if(showPrivateContent) {
            HtmlGenerator postHeaderRight = new HtmlGeneratorImpl("div");
            postHeaderRight.addAttribute("class", "EditBlock");
            postHeaderRight.addAttribute("id", "EditBlock_"+post.getPostPk());
            postHeaderRight.addAttribute("onmouseover", "showOpt(event)");
            postHeaderRight.addAttribute("onmouseout", "hideOpt(event)");

            HtmlGenerator postHeaderRightImg = new HtmlGeneratorImpl("img");
            postHeaderRightImg.addAttribute("class", "FeedLogoEdit");
            postHeaderRightImg.addAttribute("id", "FeedLogoEdit_"+post.getPostPk());
            postHeaderRightImg.addAttribute("onmouseover", "showOpt(event)");
            postHeaderRightImg.addAttribute("onmouseout", "hideOpt(event)");
            postHeaderRightImg.addAttribute("src", "../../resources/media/feeds/editfeed.png");

            postHeaderRight.setInnerText(postHeaderRightImg.toString());

            HtmlGenerator postOptions = new HtmlGeneratorImpl("div");
            postOptions.addAttribute("class", "ediFeedOptions");
            postOptions.addAttribute("id", "ediFeedOptions_"+post.getPostPk());
            postOptions.addAttribute("onmouseover", "showOpt(event)");
            postOptions.addAttribute("onmouseout", "hideOpt(event)");
            postOptions.setFullTag(true);

            List<HtmlGenerator> options = new ArrayList<>();
            options.add(new HtmlGeneratorImpl("p"));
            options.get(0).setInnerText("Edit");
            options.get(0).addAttribute("id", "editPost_"+ post.getPostPk());
            options.get(0).addAttribute("onclick", "optionsAction(event)");

            options.add(new HtmlGeneratorImpl("p"));
            options.get(1).setInnerText("Delete");
            options.get(1).addAttribute("id", "deletePost_"+ post.getPostPk());
            options.get(1).addAttribute("onclick", "optionsAction(event)");
            postOptions.setInnerText(options.get(0).toString() + options.get(1).toString());

            postHeader.setInnerText(postHeaderLeft.toString() + postHeaderRight.toString() + postOptions.toString());
        }else{
            postHeader.setInnerText(postHeaderLeft.toString());
        }



        //postForm-----------------------------------------------------------------------------------
        HtmlGenerator postForm = new HtmlGeneratorImpl("form");
        postForm.addAttribute("class", "PostCheckboxForm");
        postForm.addAttribute("action", "");

        //postComment
        HtmlGenerator hideButton = new HtmlGeneratorImpl("div");
        hideButton.addAttribute("class", "LinkSpoiler");
        hideButton.addAttribute("onclick", "MoreInfo(event)");
//        HtmlGenerator hideButtonText = new HtmlGeneratorImpl("p");
        hideButton.addAttribute("id", "hideButton_" + post.getPostPk().toString());
        hideButton.setInnerText("Показать заметку полностью");
        hideButton.setFullTag(true);
        HtmlGenerator commentpostWrapper = new HtmlGeneratorImpl("div");
        commentpostWrapper.addAttribute("class", "CommentPostHeight");

        HtmlGenerator postComment = new HtmlGeneratorImpl("div");
        postComment.addAttribute("class", "commentPost");
        postComment.addAttribute("id", "postComment_" + post.getPostPk().toString());


        if (post.getCommentary() != null && !post.getCommentary().equals("")){
            postComment.setInnerText(post.getCommentary());
        }else{
            postComment.setFullTag(true);
        }
        commentpostWrapper.setInnerText(postComment.toString());

        //----------------------------------------------

        HtmlGenerator postGradient = new HtmlGeneratorImpl("div");
        postGradient.addAttribute("class", "CommentPostGrad");
        postGradient.addAttribute("id", "CommentPostGrad_"+post.getPostPk());
        postGradient.setFullTag(true);

        HtmlGenerator dragArea = new HtmlGeneratorImpl("div");
        dragArea.addAttribute("class", "postEditDragArea");
        dragArea.addAttribute("id", "postEditDragArea_"+post.getPostPk());
        HtmlGenerator dragAreaInnerEnterBlock = new HtmlGeneratorImpl("div");
        dragAreaInnerEnterBlock.addAttribute("class", "dragAreaInnerEnterBlock");
        dragAreaInnerEnterBlock.addAttribute("id", "dragAreaInnerEnterBlock_"+post.getPostPk());
        dragAreaInnerEnterBlock.setInnerText("Перетащите сюда файлы");
        HtmlGenerator dragAreaInnerOverBlock = new HtmlGeneratorImpl("div");
        dragAreaInnerOverBlock.addAttribute("class", "dragAreaInnerOverBlock");
        dragAreaInnerOverBlock.addAttribute("id", "dragAreaInnerOverBlock_"+post.getPostPk());
        dragAreaInnerOverBlock.setInnerText("Отпустите кнопку мыши");

        dragArea.setInnerText(dragAreaInnerEnterBlock.toString()+dragAreaInnerOverBlock.toString());

        HtmlGenerator dragHiddenArea = new HtmlGeneratorImpl("div");
        dragHiddenArea.addAttribute("id", "editPostDragHiddenArea_"+post.getPostPk());
        dragHiddenArea.addAttribute("class", "editPostDragHiddenArea");

        HtmlGenerator buttonsEditDeleteSelectAll = new HtmlGeneratorImpl("div");
        buttonsEditDeleteSelectAll.addAttribute("class", "ButtonsPostEditDeleteSelectAll");
        buttonsEditDeleteSelectAll.addAttribute("id", "ButtonsPostEditDeleteSelectAll_"+post.getPostPk());
        HtmlGenerator postInputEditDelete = new HtmlGeneratorImpl("input");
        postInputEditDelete.addAttribute("type","submit");
        postInputEditDelete.addAttribute("value","Delete selection");
        postInputEditDelete.addAttribute("style","background-image: url(../../resources/media/feeds/delete.png)");
        postInputEditDelete.addAttribute("id", "postInputEditDelete_"+post.getPostPk());
        postInputEditDelete.addAttribute("class", "postInputEditDelete");
        postInputEditDelete.addAttribute("onclick", "postEditDeleteButtonClick( event )");
        HtmlGenerator postInputEditSelectAll = new HtmlGeneratorImpl("input");
        postInputEditSelectAll.addAttribute("type","submit");
        postInputEditSelectAll.addAttribute("value","Select All");
        postInputEditSelectAll.addAttribute("id", "postInputEditSelectAll_"+post.getPostPk());
        postInputEditSelectAll.addAttribute("class", "postInputEditSelectAll");
        postInputEditSelectAll.addAttribute("onclick", "postEditMainDeleteCheckboxClicked(event)");
        postInputEditSelectAll.addAttribute("style","background-image: url(../../resources/media/feeds/checked.png)");

        buttonsEditDeleteSelectAll.setInnerText(postInputEditDelete.toString()+postInputEditSelectAll.toString());

        //FilesTable
        HtmlGenerator postFilesTable  = new HtmlGeneratorImpl("table");
        postFilesTable.addAttribute("class", "FeedFolders");
        postFilesTable.addAttribute("id", "PostFilesTable_"+post.getPostPk());

//        HtmlGenerator tr = new HtmlGeneratorImpl("tr");
//        tr.addAttribute("class" , "deleteRow DeleteBorder BackgroundNone");
//        tr.addAttribute("id" , "deleteRow_"+post.getPostPk());
//
//        HtmlGenerator tdLeft = new HtmlGeneratorImpl("td");
//        tdLeft.addAttribute("class", "FeedtdLeftEdit");
//        tdLeft.setFullTag(true);
//
//        HtmlGenerator tdCenter = new HtmlGeneratorImpl("td");
//        tdCenter.addAttribute("class", "FeedtdCenterEdit");
//        tdCenter.setInnerText("<span> </span>");

//        HtmlGenerator tdRight = new HtmlGeneratorImpl("td");
//        tdRight.addAttribute("class", "FeedtdRightEdit");
//        HtmlGenerator deleteLabel = new HtmlGeneratorImpl("p");
//        HtmlGenerator deleteButtonImg = new HtmlGeneratorImpl("img");
//        deleteButtonImg.addAttribute("src", "../../resources/media/feeds/PostEdit.png");
//        deleteButtonImg.addAttribute("id", "postDeleteButtonImg_" + post.getPostPk());
//        deleteLabel.setInnerText(deleteButtonImg.toString() + "Delete");
//        deleteLabel.addAttribute("onclick", "postEditDeleteButtonClick(event)");
//        deleteLabel.addAttribute("id", "postDeleteLabel_" + post.getPostPk());
//        tdRight.setInnerText(deleteLabel.toString());

//        HtmlGenerator tdDeleteCheckbox = new HtmlGeneratorImpl("td");
//        HtmlGenerator deleteCheckBox = new HtmlGeneratorImpl("input");
//        deleteCheckBox.addAttribute("id" ,"deleteCheckBox_"+ post.getPostPk());
//        deleteCheckBox.addAttribute("onclick", "postEditMainDeleteCheckboxClicked(event)");
//        deleteCheckBox.addAttribute("type" ,"checkbox");
//        tdDeleteCheckbox.setInnerText(deleteCheckBox.toString());

//        tr.setInnerText(tdLeft.toString() + tdCenter.toString() + tdRight.toString());

        StringBuilder filesHtml = new StringBuilder();
        List<FileData> files = filesDAO.getFiles(post);
        for (int j = 0; j < files.size(); j++) {
            filesHtml.append(getFileHtml(files.get(j), showPrivateContent, j));
        }
        postFilesTable.setInnerText( buttonsEditDeleteSelectAll.toString()+filesHtml.toString());

        //Upload and submit
        HtmlGenerator postUploadBlock = new HtmlGeneratorImpl("div");
        postUploadBlock.addAttribute("class", "uploadDad");
        postUploadBlock.addAttribute("id", "uploadDad_"+post.getPostPk());

        HtmlGenerator inputFile = new HtmlGeneratorImpl("input");
        inputFile.addAttribute("id", "inputFile_"+post.getPostPk());
        inputFile.addAttribute("onchange", "postEditInputClick(event)");
        inputFile.addAttribute("type", "file");
        inputFile.addAttribute("hidden", "hidden");
        inputFile.addAttribute("multiple", "multiple");

        HtmlGenerator uploadImg = new HtmlGeneratorImpl("img");
        uploadImg.addAttribute("onclick", "inputFile_"+post.getPostPk() +".click();");
        uploadImg.addAttribute("src", "../../resources/media/feeds/UploadDad.png");

        postUploadBlock.setInnerText(inputFile.toString()+ uploadImg.toString());

        dragHiddenArea.setInnerText(postFilesTable.toString()+postUploadBlock.toString());


        HtmlGenerator postSaveBlock = new HtmlGeneratorImpl("div");


        postSaveBlock.addAttribute("class", "SaveButton");
        postSaveBlock.addAttribute("id", "SaveButton_"+ post.getPostPk());

        HtmlGenerator submitCancel = new HtmlGeneratorImpl("input");
//        submitCancel.addAttribute("class", "SaveSubmit");
//        submitCancel.addAttribute("onclick", "updatePostAfterEdit(event)");
//        submitCancel.addAttribute("id", "SaveSubmit_"+ post.getPostPk());
        submitCancel.addAttribute("id", "postEditCancel_"+post.getPostPk());
        submitCancel.addAttribute("type", "submit");
        submitCancel.addAttribute("value", "Cancel");
        submitCancel.addAttribute("onclick", "postEditCancelClicked(event)");
        submitCancel.addAttribute("style","background-image: url(../../resources/media/feeds/cancel.png);");

        HtmlGenerator submit = new HtmlGeneratorImpl("input");
        submit.addAttribute("class", "SaveSubmit");
        submit.addAttribute("onclick", "updatePostAfterEdit(event)");
        submit.addAttribute("id", "SaveSubmit_"+ post.getPostPk());
        submit.addAttribute("type", "submit");
        submit.addAttribute("value", "Save");
        submit.addAttribute("style","background-image: url(../../resources/media/feeds/SavePost.png);");

        postSaveBlock.setInnerText(submitCancel.toString() + submit.toString());

        postForm.setInnerText(hideButton.toString() + commentpostWrapper.toString()
                + postGradient.toString() + dragArea.toString() + dragHiddenArea.toString() + postSaveBlock.toString());

        feedContainer.setInnerText(postHeader.toString() + postForm.toString());

        return feedContainer.toString();
    }

    private String getFileHtml(FileData fileData, boolean showPrivateContent, int fileNumber){
        double sizeByte = fileData.getSize();
        HtmlGenerator tr = new HtmlGeneratorImpl("tr");
        tr.addAttribute("id", "postFileRaw_"+ fileData.getFilePk());
        if (fileNumber == 0) {
            tr.addAttribute("class" , "DeleteBorder postFileRaws_"+ fileData.getPost().getPostPk());
        }else{
            tr.addAttribute("class", "postFileRaws_"+ fileData.getPost().getPostPk());
        }
        HtmlGenerator tdLeft = new HtmlGeneratorImpl("td");
        tdLeft.addAttribute("class", "FeedtdLeft");
        HtmlGenerator fileImg = new HtmlGeneratorImpl("img");
        //TODO preview
        fileImg.addAttribute("src", "../../resources/media/feeds/photofile.png");
        tdLeft.setInnerText(fileImg.toString());

        HtmlGenerator tdCenter = new HtmlGeneratorImpl("td");
        tdCenter.addAttribute("class", "FeedtdCenter PostTdCenter_"+fileData.getPost().getPostPk());
        tdCenter.addAttribute("title", fileData.getFilename());
        tdCenter.setInnerText(fileData.getFilename());


        HtmlGenerator tdRight = new HtmlGeneratorImpl("td");
        tdRight.addAttribute("class", "FeedtdRight FeedtdRight_" + fileData.getPost().getPostPk());
        //SizeFileOfPost
        tdRight.setInnerText(String.valueOf(sizeByte));



        HtmlGenerator tdFileDownload = new HtmlGeneratorImpl("td");
        tdFileDownload.addAttribute("class", "FeedTdDownload FeedTdDownload_"+fileData.getPost().getPostPk());
        HtmlGenerator fileHref = new HtmlGeneratorImpl("a");
        fileHref.addAttribute("href","/" + fileData.getPost().getFeed().getUser().getUsername() +"/" + fileData.getPost().getFeed().getFeedname() + "/download/" + fileData.getPost().getPostPk() + "/" + fileData.getFilename());
        HtmlGenerator fileDownloadImg = new HtmlGeneratorImpl("img");
        fileDownloadImg.addAttribute("src", "../../resources/media/feeds/download.png");
        fileHref.setInnerText(fileDownloadImg.toString());
        tdFileDownload.setInnerText(fileHref.toString());


        HtmlGenerator tdDeleteCheckbox = new HtmlGeneratorImpl("td");
        tdDeleteCheckbox.addAttribute("class", "FeedTdDeleteEdit FeedTdDeleteEdit_"+fileData.getPost().getPostPk());
        HtmlGenerator deleteCheckBox = new HtmlGeneratorImpl("input");
        deleteCheckBox.addAttribute("type" ,"checkbox");
        deleteCheckBox.addAttribute("id", "postEditFileDeleteCheckbox_" + fileData.getFilePk());
        deleteCheckBox.addAttribute("class", "postEditFileDeleteCheckboxes_" + fileData.getPost().getPostPk());
        tdDeleteCheckbox.setInnerText(deleteCheckBox.toString());

        tr.setInnerText(tdLeft.toString() + tdCenter.toString() + tdRight.toString() + tdFileDownload.toString() + tdDeleteCheckbox.toString());
        return tr.toString();
    }
}
