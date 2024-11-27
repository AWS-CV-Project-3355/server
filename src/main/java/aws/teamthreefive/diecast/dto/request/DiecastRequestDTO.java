package aws.teamthreefive.diecast.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;


public class DiecastRequestDTO {

    @Getter
    @Setter
    public static class DiecastDTO {
        int diecastOkng;
    }

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
