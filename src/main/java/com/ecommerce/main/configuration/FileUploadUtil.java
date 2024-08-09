package com.ecommerce.main.configuration;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

public class FileUploadUtil {
    public static void saveFile(String uploadDir, String fileName,
                                MultipartFile multipartFile) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if(!Files.exists(uploadPath)) Files.createDirectories(uploadPath);

        try{
            InputStream inputStream = multipartFile.getInputStream();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            throw new IOException("Could not save file: " + fileName, e);
        }
    }

    public static void cleanDir(String dir) {
        Path dirPath = Paths.get(dir);
        try (Stream<Path> paths = Files.list(dirPath)) {
            paths.forEach(file -> {
                try {
                    if (Files.isDirectory(file)) {
                        // Recursively clean and delete the directory
                        cleanDir(file.toString());
                        Files.delete(file);
                    } else {
                        Files.delete(file); // Delete the file
                    }
                } catch (IOException e) {
                    System.out.println("Could not delete file: " + file + " - " + e.getMessage());
                }
            });
        } catch (IOException ex) {
            System.out.println("Could not list directory: " + dirPath + " - " + ex.getMessage());
        }
    }

}
