package aws.teamthreefive.diecast.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

public class DiecastRequestDTO {

    @Getter
    @Setter
    public static class PhotoDTO {
        int photoPosition;
        int photoNgtype;
        Float photoCroplt;
        Float photoCroprb;
        MultipartFile photoFile;
    }

}
