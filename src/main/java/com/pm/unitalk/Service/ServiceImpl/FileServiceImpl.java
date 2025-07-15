package com.pm.unitalk.Service.ServiceImpl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.pm.unitalk.Model.CloudinaryImage;
import com.pm.unitalk.Service.FileService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service

public class FileServiceImpl implements FileService {
    private Cloudinary cloudinary;

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    @Value("${cloudinary.api-key}")
    private String apiKey;

    @Value("${cloudinary.api-secret}")
    private String apiSecrete;

    @PostConstruct
    public void initCloudinary() {
        this.cloudinary = new Cloudinary(ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecrete
        ));
    }
    //cloudinary
    public CloudinaryImage uploadImage(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String randomID = UUID.randomUUID().toString();
        String uniqueFilename = randomID.concat(originalFilename.substring(originalFilename.lastIndexOf(".")));

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", "ecommerce/" + uniqueFilename,
                "folder", "ecommerce"
        ));

        String url = (String) uploadResult.get("secure_url");
        String publicId = (String) uploadResult.get("public_id");

        return new CloudinaryImage(url, publicId);
    }


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





    //cloudinary
    public InputStream getSource(String imageUrl) throws IOException {
        try {
            URL url = new URL(imageUrl); // Will fail if no protocol
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            return connection.getInputStream();
        } catch (MalformedURLException e) {
            log.error("Invalid image URL provided: {}", imageUrl, e);
            throw new RuntimeException("Image URL is invalid: " + imageUrl);
        }
    }


    @Override
    public InputStream getSource(String path, String fileName) throws FileNotFoundException {
        String fullPath=path+File.separator+fileName;
        InputStream is=new FileInputStream(fullPath);
        return is;
    }





    //cloudinary
    public boolean deleteImage(String publicId) {
        try {
            Map result = cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
            return "ok".equals(result.get("result"));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
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
