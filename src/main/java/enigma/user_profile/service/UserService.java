package enigma.user_profile.service;

import enigma.user_profile.model.UserEntity;
import enigma.user_profile.utils.UserDTO;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.util.Map;

public interface UserService {
    ResponseEntity<Map> createAndUpload(UserDTO request);
    UserEntity getOne(Integer id);
    ResponseEntity<Map> changeProfile(UserDTO request, Integer id);
    byte[] profileToPDF(Integer id) throws IOException;
}