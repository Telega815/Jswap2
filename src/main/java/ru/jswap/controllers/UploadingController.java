package ru.jswap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.FileData;
import ru.jswap.entities.Post;
import ru.jswap.entities.User;
import ru.jswap.objects.AccessParams;
import ru.jswap.objects.JSON.ClientIdInfo;
import ru.jswap.objects.JSON.NewPostInfo;
import ru.jswap.objects.JSON.ResponsePostInfo;
import ru.jswap.services.FileService;
import ru.jswap.services.HtmlService;
import ru.jswap.services.UserService;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLConnection;

@Controller
public class UploadingController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    @Autowired
    private HtmlService htmlService;


    //---------------------------------------------------------------------------
    /**
     * @param file uploaded file
     * @param clientId Used to distinguish between different tabs
     * @param session current session
     * @param fileId used for identifying file between front and back end
     * @return ID of uploaded file in temp post
     */
    @RequestMapping(value = "/restService/uploadFile", method = RequestMethod.POST)
    @ResponseBody
    public int uploadFile(@RequestParam (name="file")MultipartFile file,
                       @RequestParam (name="clientId") Integer clientId,
                       @RequestParam (name="fileId") Integer fileId,
                       HttpSession session){
        return fileService.fileUploadProcess(file, clientId, session.getId(), fileId);
    }

    /**
     * @return client ID (will be used to distinguish between different tabs)
     */
    @PostMapping(value = "/restService/getNewClientId")
    @ResponseBody
    public String getNewClientId(){
        return fileService.getAndIncrementMaxId().toString();
    }

    /**
     * @param clientId received from front end (Used to distinguish between different tabs)
     * @return ClientIdInfo if client have any files
     */
    @PostMapping(value = "/restService/getClientIdInfo", produces = "application/json")
    @ResponseBody
    public ClientIdInfo getClientIdInfo(@RequestParam (name = "clientId") Integer clientId){
        return fileService.getClientIdInfo(clientId);
    }

    /**
     * @param clientId Used to distinguish between different tabs
     * @param fileId used for identifying file between front and back end
     * @return true if delete was successful
     */
    @PostMapping(value = "/restService/deleteFile")
    @ResponseBody
    public boolean deleteFile(@RequestParam (name="clientId") Integer clientId,
                              @RequestParam (name="fileId") Integer fileId){
        return fileService.deleteTmpFile(clientId, fileId);
    }


    /**
     * @param info about creating post (comment, feedID, ...)
     * @return info about created post (id, ... )
     */
    @RequestMapping(method = RequestMethod.POST, value = "restService/saveNewPost", consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponsePostInfo saveNewPost(@RequestBody NewPostInfo info){
        Post post = fileService.saveNewPost(info);
        ResponsePostInfo responsePostInfo = new ResponsePostInfo();
        if (post == null){
            responsePostInfo.setNullPost(true);
        }else{
            responsePostInfo.setPostId(post.getPostPk());
            responsePostInfo.setHtmlPost(htmlService.getPostHtml(post, userService.checkUser(post.getFeed().getUser())));
            responsePostInfo.setNullPost(false);

            responsePostInfo.setFeedSize(post.getFeed().getSize());
            responsePostInfo.setUserSpace(post.getFeed().getUser().getFilesSize());
        }
        return responsePostInfo;
    }


    /**
     * @param feedId id of the required feed
     * @return posts of feed as html
     */
    @PostMapping(value = "restService/getPostsOfFeed")
    @ResponseBody
    public String getPostsHtml(@RequestParam (name="feedId") Integer feedId){
        Feeds feed = userService.getFeed(feedId);
        AccessParams accessParams = new AccessParams();
        accessParams.setParams(feed.getAccesstype());
        Boolean userIdentificated=userService.checkUser(feed.getUser());

        switch (accessParams.getRead()){
            case 0:
                return htmlService.getAllPostsHtml(feed,userIdentificated);
            case 1:
                //TODO PinAccessCheck
                return "Pincode required";

            case 2:
                if (userIdentificated){
                    return htmlService.getAllPostsHtml(feed,true);
                }
                else{
                    return "Authentication required";
                }


        }
      return "Undefined behavior";
    }

    /**
     * @param postId id of post to delete
     * @return true if delete was successful
     */
    @PostMapping(value = "restService/deletePost", produces = "application/json")
    @ResponseBody
    public ResponsePostInfo deletePost(@RequestParam Long postId){
        ResponsePostInfo responsePostInfo = new ResponsePostInfo();
        Post post = fileService.deletePostRecursive(postId);
        if (post!=null){
            responsePostInfo.setFeedSize(post.getFeed().getSize());
            responsePostInfo.setUserSpace(post.getFeed().getUser().getFilesSize());
            responsePostInfo.setNullPost(false);
        }
        else{
            responsePostInfo.setNullPost(true);
        }
        return responsePostInfo;
    }



















    //------------------------------------------------------------------------------

    @PostMapping(value = "/{username}/deleteFile")
    @ResponseBody
    public String deleteFile(@PathVariable("username") String username,
                             @SessionAttribute(value = "user", required = false) User user,
                             @RequestBody String filesString) {
        if (user == null) user = userService.getUser(username);
        String mystr = filesString;
        if (userService.checkUser(user))
            return "loginFailure";
        return "error";
    }

    //TODO this is a total shit
    @RequestMapping(value = "/{username}/{feedname}/download/{postid}/{filename:.+}")
    public void download(@PathVariable("filename") String filename,
                         @PathVariable("postid") int postid,
                         HttpServletResponse response){
        FileData fileData = fileService.getFile(filename, postid);
        String filePath = "dsfsdfsf";//fileService.getFilePath(fileData);

        File file = new File(filePath);

        String mimeType= URLConnection.guessContentTypeFromName(file.getName());
        if(mimeType==null){
            System.out.println("mimetype is not detectable, will take default");
            mimeType = "application/octet-stream";
        }
        System.out.println("mimetype : " + mimeType);

        response.setContentType(mimeType);

        response.setHeader("Content-Disposition", "inline; filename=\"" + filename +"\"");
        response.setContentLengthLong(file.length());
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
            inputStream.close();
        } catch (IOException e) {
            //TODO catch block
            e.printStackTrace();
        }
    }

//    @RequestMapping(value = "/{username}/{feedname}/delete/{postid}")
//    @ResponseBody
//    public String delete(@SessionAttribute(value = "user", required = false) User user,
//                         @PathVariable("username") String username,
//                         @PathVariable("feedname") String feedname,
//                         @PathVariable("postid") long postid){
//        if (user == null) user = userService.getUser(username);
//        if (userService.checkUser(user)){
//            Post post = fileService.getPost(postid);
//            fileService.deletePost(post, true);
//            return "success";
//        }else{
//            return "go fuck your self!";
//        }
//    }
}
