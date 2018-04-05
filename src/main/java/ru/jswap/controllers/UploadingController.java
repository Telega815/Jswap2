package ru.jswap.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.jswap.dao.intefaces.PostsDAO;
import ru.jswap.dao.intefaces.UserDAO;
import ru.jswap.entities.Feeds;
import ru.jswap.entities.FileData;
import ru.jswap.entities.Post;
import ru.jswap.entities.User;
import ru.jswap.objects.UploadedFile;
import ru.jswap.services.FileService;
import ru.jswap.services.UserService;
import ru.jswap.validators.FileValidator;

import javax.servlet.http.HttpServletResponse;
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

    @PostMapping(value = "/{username}/{feedname}/{postid}/upload")
    @ResponseBody
    public String upload(@ModelAttribute("uploadedFile") UploadedFile uploadedFile, BindingResult result,
                         @PathVariable("username") String username,
                         @PathVariable("feedname") String feedname,
                         @PathVariable("postid") String postid) {
        String res;
        try {
            fileValidator.validate(uploadedFile, result);
            Feeds feed = userService.getFeed(feedname);
            if (result.hasErrors()){
                res = result.toString();
            }else {
                MultipartFile[] multipartFiles = uploadedFile.getFiles();
                res = fileService.writeMultipartFiles(multipartFiles, feed, postid).toString();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            res = "IOException: " + e.getMessage();
        }
        return res;
    }

    @PostMapping(value = "/{username}/{feedname}/upload/save")
    @ResponseBody
    public void enable(@RequestBody String reqBody) {
        String s = reqBody.replace("=", "");
        fileService.enableFiles(Long.valueOf(s));
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
        response.setContentLength((int)file.length());
        try {
            InputStream inputStream = new BufferedInputStream(new FileInputStream(file));
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            //TODO catch block
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "/{username}/{feedname}/delete/{postid}/{filename:.+}")
    public String delete(@PathVariable("username") String username,
                         @PathVariable("filename") String filename,
                         @PathVariable("feedname") String feedname,
                         @PathVariable("postid") int postid){
        FileData fileData = fileService.getFile(filename,postid);
        fileService.deleteFile(fileData);
        return "redirect:/"+username+"/"+feedname+"/viewFiles";
    }

    @RequestMapping(value = "/{username}/{feedname}/delete/{postid}")
    public String delete(@PathVariable("username") String username,
                         @PathVariable("feedname") String feedname,
                         @PathVariable("postid") long postid){
        Post post = fileService.getPost(postid);
        fileService.deletePost(post, true);
        return "redirect:/"+username+"/"+feedname+"/viewFiles";
    }
}
