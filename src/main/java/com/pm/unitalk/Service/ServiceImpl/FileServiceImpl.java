package com.pm.unitalk.Service.ServiceImpl;

import com.pm.unitalk.Service.FileService;
import lombok.extern.slf4j.Slf4j;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service

public class FileServiceImpl implements FileService {


    @Override
    public String uploadImage(String path, MultipartFile file) throws IOException {
        //File name
        String name= file.getOriginalFilename();

        //generate a random name
        String randomID= UUID.randomUUID().toString();
        String fileName1=randomID.concat(name.substring(name.lastIndexOf(".")));
        //Full path
        String filePath=path+File.separator+fileName1;
        //create folder if not created
        File f=new File(path);
        if(!f.exists()){
            f.mkdir();
        }

        //copy file
        Files.copy(file.getInputStream(),Paths.get(filePath));
        return fileName1;
    }




    @Override
    public InputStream getSource(String path, String fileName) throws FileNotFoundException {
        String fullPath=path+File.separator+fileName;
        InputStream is=new FileInputStream(fullPath);
        return is;
    }

    @Override
    public String deleteImage(String path, String fileName) {
        String fullPath=path+File.separator+fileName;
        try {
            Files.deleteIfExists(Path.of(fullPath));
        } catch (IOException e) {
            log.error("There is a problem while deleting the file. Here is the description: "+e);
            throw new RuntimeException("Coulden't delete the file."+e);
        }
        return fileName+" is deleted successfully";
    }
}
