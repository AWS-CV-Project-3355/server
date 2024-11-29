package aws.teamthreefive.diecastvideo.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class DiecastvideoRequestDTO {

    @Getter
    @Setter
    public static class DiecastvideoDTO {
        @NotNull
        MultipartFile diecastvideo;
//        String videoType;
    }

}
