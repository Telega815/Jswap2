package ru.jswap.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.jswap.entities.FileData;
import ru.jswap.entities.FilePath;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TempPost implements Runnable{
    private Logger logger = LoggerFactory.getLogger(TempPost.class);
    private final int MILLS = 10000;
    private Map<Integer, FileData> files;
    private Map<Integer, FilePath> paths;
    private String FILE_PATH = "C:" + File.separator + "tmpFiles" + File.separator + "temp" + File.separator;
    private Thread timeoutThread;

    public TempPost() {
        files = new HashMap<>();
        paths = new HashMap<>();
    }

    public int addFile(MultipartFile multipartFile, int clientId, String sessionId, int key){
        File file = new File(FILE_PATH + sessionId + File.separator + clientId + File.separator + key);

        if(!file.exists()){
            if (!file.mkdirs()) {
                logger.info("\n{}: WARNING! = directory creation failed; path = {}", new java.util.Date(System.currentTimeMillis()).toString(), file.getAbsolutePath());
                return -1;
            }
        }

        FileData fileData = new FileData();
        fileData.setFilename(multipartFile.getOriginalFilename());
        fileData.setSize(multipartFile.getSize());
        files.put(key,fileData);

        FilePath filePath = new FilePath();
        filePath.setPath(file.getAbsolutePath());
        paths.put(key,filePath);

        try {
            multipartFile.transferTo(file);
        } catch (IOException e) {
            logger.info("\n{}: WARNING! = relocation failed; path = {}; filename = {}", new java.util.Date(System.currentTimeMillis()).toString(), file.getAbsolutePath(), multipartFile.getOriginalFilename());
            return -1;
        }

        startTimeOut();
        return key;
    }

    public void startTimeOut(){
        if (timeoutThread == null) timeoutThread = new Thread(this);

        if (timeoutThread.isAlive()) timeoutThread.interrupt();

        timeoutThread = new Thread(this);
        timeoutThread.start();
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
    }

    public boolean deleteAllFiles(){
        boolean res = true;
        for (FilePath path: paths.values()) {
            File file = new File(path.getPath());
            res &= file.delete();
        }
        files = new HashMap<>();
        paths = new HashMap<>();
        return res;
    }


    @Override
    public void run() {
        try {
            Thread.sleep(MILLS);
            deleteAllFiles();
            logger.info("\n{}: TempPost was deleted - timeout", new java.util.Date(System.currentTimeMillis()).toString());
        } catch (InterruptedException e) {
            logger.info("\n{}: cached exception in 'run' of TempPost: "+e.getMessage(), new java.util.Date(System.currentTimeMillis()).toString());
        }
    }

}
