package com.example.store.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Slf4j
@Component
public class FileUploadUtil implements FileUpload{

    @NonNull
    public String uploadFile(@NonNull String directory,@NonNull MultipartFile file) {

        Path uploadPath = Paths.get("src/main/resources" + directory);

        if (!Files.exists(uploadPath)) {
            try {
                Files.createDirectories(uploadPath);
            } catch (Exception e) {
                log.error(e.getMessage());
                throw new IllegalArgumentException("Failed to create directory");
            }
        }


        if (file.isEmpty()) {
            throw new IllegalArgumentException("Image not found");
        }

        if (file.getOriginalFilename() == null) {
            throw new IllegalArgumentException("Image not found");
        }

        String fileName = UUID.randomUUID().toString();

        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !originalFilename.contains(".")) {
            throw new IllegalArgumentException("The file does not have a valid extension");
        }

        String extension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));

        try(InputStream inputStream = file.getInputStream()){
            Path filePath = uploadPath.resolve(fileName+extension);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            log.error(e.getMessage());
            throw new IllegalArgumentException("Failed to copy file");
        }

        return fileName+extension;
    }
}
