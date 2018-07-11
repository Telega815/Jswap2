package ru.jswap.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.jswap.entities.FileData;
import ru.jswap.entities.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TempPost implements Runnable{
    private Logger logger = LoggerFactory.getLogger(TempPost.class);
    private final int MILLS = 180000;
    private List<FileData> files;
    private List<FilePath> paths;
    private String FILE_PATH = "C:" + File.separator + "tmpFiles" + File.separator + "temp" + File.separator;


    public TempPost() {
        files = new ArrayList<>();
        paths = new ArrayList<>();
    }

    public int addFile(MultipartFile multipartFile, int clientId, String sessionId){
        File file = new File(FILE_PATH + sessionId + File.separator + clientId + File.separator + files.size());

        if(!file.exists()){
            if (!file.mkdirs()) {
                logger.info("\n{}: WARNING! = directory creation failed; path = {}", new java.util.Date(System.currentTimeMillis()).toString(), file.getAbsolutePath());
                return -1;
            }
        }

        FileData fileData = new FileData();
        fileData.setFilename(multipartFile.getOriginalFilename());
        fileData.setSize(multipartFile.getSize());
        files.add(fileData);

        FilePath filePath = new FilePath();
        filePath.setPath(file.getAbsolutePath());
        paths.add(filePath);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            logger.info("\n{}: WARNING! = relocation failed; path = {}; filename = {}", new java.util.Date(System.currentTimeMillis()).toString(), file.getAbsolutePath(), multipartFile.getOriginalFilename());
            return -1;
        }

        return files.size()-1;
    }

    public FileData getFileName(int key){
        return files.get(key);
    }

    public FilePath getPath(int key){
        return paths.get(key);
    }

    public void deleteFile(int key){
        File file = new File(paths.get(key).getPath());
        if(file.delete()){
            files.remove(key);
            paths.remove(key);
        }
        files.size();
    }


    @Override
    public void run() {
        try {
            Thread.sleep(MILLS);
            for (int i = 0; i < files.size(); i++){
                deleteFile(i);
            }
            logger.info("\n{}: TempPost was deleted - timeout", new java.util.Date(System.currentTimeMillis()).toString());
        } catch (InterruptedException e) {
            logger.info("\n{}: cached exception in 'run' of TempPost: "+e.getMessage(), new java.util.Date(System.currentTimeMillis()).toString());
        }
    }
}
