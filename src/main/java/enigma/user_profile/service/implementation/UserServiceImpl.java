package enigma.user_profile.service.implementation;

import enigma.user_profile.model.UserEntity;
import enigma.user_profile.repository.UserRepository;
import enigma.user_profile.service.CloudinaryService;
import enigma.user_profile.service.UserService;
import enigma.user_profile.utils.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Map;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private UserRepository userRepository;


    @Override
    public ResponseEntity<Map> createAndUpload(UserDTO request) {
        try {
            if (request.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            UserEntity user = new UserEntity();
            user.setName(request.getName());
            user.setUrl_picture(cloudinaryService.uploadFile(request.getFile(), "folder_1"));
            if(user.getUrl_picture() == null) {
                return ResponseEntity.badRequest().build();
            }
            userRepository.save(user);
            return ResponseEntity.ok().body(java.util.Map.of("url", user.getUrl_picture()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public UserEntity getOne(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(()->new RuntimeException("USER NOT FOUND"));
    }

    @Override
    public ResponseEntity<Map> changeProfile(UserDTO request, Integer id) {
        UserEntity userUpdate = this.getOne(id);
        try {
            if (request.getName().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            if (request.getFile().isEmpty()) {
                return ResponseEntity.badRequest().build();
            }
            userUpdate.setName(request.getName());
            userUpdate.setUrl_picture(cloudinaryService.uploadFile(request.getFile(), "folder_1"));
            if(userUpdate.getUrl_picture() == null) {
                return ResponseEntity.badRequest().build();
            }
            userRepository.save(userUpdate);
            return ResponseEntity.ok().body(java.util.Map.of("url", userUpdate.getUrl_picture()));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public byte[] profileToPDF(Integer id) throws IOException {
        // Get image url from database
//        System.out.println("aaa");
        UserEntity user = this.getOne(id);
        URL url = new URL(user.getUrl_picture());
        BufferedImage image = ImageIO.read(url);

        // Save BufferedImage to a temporary file
//        System.out.println("bbb");
        File tempFile = File.createTempFile("tempImage", ".jpg");
        ImageIO.write(image, "jpg", tempFile);

        // Create PDF document
        PDDocument document = new PDDocument();
        PDPage page = new PDPage();
        document.addPage(page);

        // Convert temporary file to PDImageXObject
//        System.out.println("ccc");
        PDImageXObject pdImage = PDImageXObject.createFromFile(tempFile.getAbsolutePath(), document);

        // Convert image to pdf
//        System.out.println("ddd");
        PDPageContentStream contentStream = new PDPageContentStream(document, page);
        contentStream.drawImage(pdImage, 20, 20);
        contentStream.close();

        // Convert PDF document to byte array
//        System.out.println("eee");
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        document.save(outputStream);
        document.close();

        // Delete the temporary file
        tempFile.delete();

        return outputStream.toByteArray();
    }

}
