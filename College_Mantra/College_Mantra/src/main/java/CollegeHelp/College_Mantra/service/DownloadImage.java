package CollegeHelp.College_Mantra.service;

import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Service
public class DownloadImage {

    private String folderPath = "D:\\Java-Backend-Projects\\College_Mantra\\College_Mantra\\src\\main\\resources\\profilePhotos\\";
    public static byte[] downloadImage(String path) throws IOException {

        byte[] images = Files.readAllBytes(new File(path).toPath());

        return images;

    }

    public byte[] getImageByName(String name) throws IOException {
        String path = folderPath + name;
        return downloadImage(path);
    }

    public static String generateImgUri(String name){
        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/user/profile/")
                .path(name)
                .toUriString();

        return fileDownloadUri;
    }
}
