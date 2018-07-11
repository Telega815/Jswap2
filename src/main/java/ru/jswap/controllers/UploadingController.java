package ru.jswap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.jswap.entities.FileData;
import ru.jswap.entities.Post;
import ru.jswap.entities.User;
import ru.jswap.objects.RequestPostInfo;
import ru.jswap.objects.ResponsePostInfo;
import ru.jswap.services.FileService;
import ru.jswap.services.HtmlService;
import ru.jswap.services.UserService;
import ru.jswap.validators.FileValidator;

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
    private FileValidator fileValidator;

    @Autowired
    private HtmlService htmlService;



    //---------------------------------------------------------------------------
    @RequestMapping(value = "/restService/upload", method = RequestMethod.POST)
    @ResponseBody
    public String upld(@RequestParam (name="file", required = false)MultipartFile file,
                       @RequestParam (name="clientId", required = false) Integer clientId,
                       HttpSession session){

        return "suckass";
    }

    @GetMapping(value = "/restService/getNewClientId")
    @ResponseBody
    public String getNewClientId(){
        return fileService.getAndIncrementMaxId().toString();
    }






















//    @PostMapping(value = "/{username}/uploadFile")
//    @ResponseBody
//    public int upload(@RequestParam(name = "file",required = false) MultipartFile[] multipartFiles,
//                      @SessionAttribute(value = "user", required = false) User user,
//                      @PathVariable("username") String username) {
//        if (user == null) user = userService.getUser(username);
//        return fileService.saveMultipartFile(multipartFiles[0], user);
//    }
    //------------------------------------------------------------------------------

    @GetMapping(value = "/{username}/deleteTmpFile/{filename:.+}")
    @ResponseBody
    public String deleteTmpFile(@PathVariable("filename") String filename) {
        if (fileService.deleteFileFromTempPost(filename))
            return "success";
        return "error";
    }

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

    //------------------------------------------------------------------------------
    @RequestMapping(value = "/{username}/save", method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    @ResponseBody
    public ResponsePostInfo enable(@RequestBody RequestPostInfo info) throws IOException {
        Post post = fileService.saveTempPost(info);
        fileService.postEditDeleteFiles(info.getFilesToDelete());

        ResponsePostInfo respInfo = new ResponsePostInfo();
        if (post == null){
            respInfo.setPostId(0);
            respInfo.setHtmlPost("");
            respInfo.setNullPost(true);
            return respInfo;
        }
        respInfo.setHtmlPost(htmlService.getPostHtml(post, userService.checkUser(post.getFeed().getUser())));
        respInfo.setPostId(post.getPostPk());
        respInfo.setNullPost(false);
        return respInfo;
    }



    @RequestMapping(value = "/{username}/{feedname}/download/{postid}/{filename:.+}")
    public void download(@PathVariable("filename") String filename,
                         @PathVariable("postid") int postid,
                         HttpServletResponse response){
        FileData fileData = fileService.getFile(filename, postid);
        String filePath = fileService.getFilePath(fileData);

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

    @RequestMapping(value = "/{username}/{feedname}/delete/{postid}")
    @ResponseBody
    public String delete(@SessionAttribute(value = "user", required = false) User user,
                         @PathVariable("username") String username,
                         @PathVariable("feedname") String feedname,
                         @PathVariable("postid") long postid){
        if (user == null) user = userService.getUser(username);
        if (userService.checkUser(user)){
            Post post = fileService.getPost(postid);
            fileService.deletePost(post, true);
            return "success";
        }else{
            return "go fuck your self!";
        }
    }
}
