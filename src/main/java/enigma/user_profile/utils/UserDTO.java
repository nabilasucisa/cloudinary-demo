package enigma.user_profile.utils;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder

public class UserDTO {
    private String name;

    private MultipartFile file;
}
