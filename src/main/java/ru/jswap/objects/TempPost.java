package ru.jswap.objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;
import ru.jswap.entities.FileData;
import ru.jswap.entities.FilePath;
import ru.jswap.objects.JSON.ClientIdInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TempPost implements Runnable{

    private Logger logger = LoggerFactory.getLogger(TempPost.class);
    private final int MILLS = 180000;
    private Map<Integer, FileData> files;
    private Map<Integer, FilePath> paths;
    private String FILE_PATH = "\\\\DESKTOP-JJNRSE9"+File.separator+"folder_for_swapy" + File.separator + "tmpFiles" + File.separator + "temp" + File.separator;
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

        this.startTimeOut();
        return key;
    }

    /**
     * @return ClientIdInfo if client have any files
     */
    public ClientIdInfo getClientIdInfo(){
        ClientIdInfo info = new ClientIdInfo();
        if (files.size() != 0) {
            info.setFileIds(files.keySet().toArray(new Integer[0]));
            List<String> filenames= new ArrayList<>();
            List<Long> filesizes = new ArrayList<>();
            for (FileData file:files.values()) {
                filenames.add(file.getFilename());
                filesizes.add(file.getSize());
            }
            info.setFilenames(filenames.toArray(new String[0]));
            info.setFileSizes(filesizes.toArray(new Long[0]));
            info.setHaveFiles(true);
        } else {
            info.setHaveFiles(false);
        }
        return info;
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

    public boolean deleteFile(int key){
        boolean res = true;
        this.startTimeOut();
        File file = new File(paths.get(key).getPath());
        if(!file.delete()){
            logger.info("\n{}: WARNING!: deleteFile of Temp Post Failed!", new java.util.Date(System.currentTimeMillis()).toString());
            res = false;
        }
        files.remove(key);
        paths.remove(key);
        return res;
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

    /**
     * @param newLocation permanent location of new post
     * @param filesToRelocate indexes of files to relocate
     * @return true if succeeded
     */
    private boolean relocateFiles(File newLocation, int[] filesToRelocate){
        boolean res = true;
        for (int key : filesToRelocate) {
            File file = new File(paths.get(key).getPath());
            res &= file.renameTo(newLocation);
        }
        return res;
    }
}
