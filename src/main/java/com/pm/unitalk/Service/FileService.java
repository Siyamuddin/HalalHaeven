package com.pm.unitalk.Service;

import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public interface FileService {
    String uploadImage(String path, MultipartFile file) throws IOException;
    InputStream getSource(String path, String fileName) throws FileNotFoundException;
    String deleteImage(String path,String fileName);
}
