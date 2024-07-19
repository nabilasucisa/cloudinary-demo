package enigma.user_profile.controller;

import enigma.user_profile.repository.UserRepository;
import enigma.user_profile.service.UserService;
import enigma.user_profile.utils.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/cloudinary/user")
@RequiredArgsConstructor
public class UserController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final UserRepository userRepository;

    @PostMapping("/create")
    private ResponseEntity<Map> createUpload(UserDTO request) {
        return userService.createAndUpload(request);
    }

    @PutMapping("/update/{id}")
    private ResponseEntity<Map> update(UserDTO request,
                                       @PathVariable Integer id) {
        return userService.changeProfile(request, id);
    }

    @GetMapping("/pdf/{id}")
    private ResponseEntity<byte[]> profileToPDF(@PathVariable Integer id) {
        try {
            byte[] pdfContent = userService.profileToPDF(id);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=image.pdf");
            headers.add(HttpHeaders.CONTENT_TYPE, "application/pdf");

            return new ResponseEntity<>(pdfContent, headers, HttpStatus.OK);
        } catch (Exception e) {
            // If failed, it will show internal error on java
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
