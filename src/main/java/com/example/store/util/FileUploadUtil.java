package com.example.store.util;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

public class FileUploadUtil {

    public static String uploadFile(String directory, MultipartFile file) {

        Path uploadPath = Paths.get("src/main/resources" + directory);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (file.isEmpty()) {
            throw new IllegalArgumentException("Imagen no encontrada");
        }

        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Imagen no encontrada");
        }

        String fileName = UUID.randomUUID().toString();

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        try(InputStream inputStream = file.getInputStream()){
            Path filePath = uploadPath.resolve(fileName+extension);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            e.printStackTrace();
        }

        return fileName+extension;
    }
}
