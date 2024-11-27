package aws.teamthreefive.diecast.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class DiecastResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDiecastResultDTO {
        Long diecastUuid;
        int diecastOkng;
        LocalDateTime createdAt;
        Long diecastvideoUuid;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SavePhotoResultDTO {
        Long photoUuid;
        String photoUrl;
        int photoPosition;
        int photoNgtype;
        Float photoCroplt;
        Float photoCroprb;
        Long diecastUuid;
        LocalDateTime createdAt;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoListDTO {
        List<PhotoDTO> photoList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoDTO {
        Long photoUuid;
        String photoUrl;
        int photoPosition;
        int photoNgtype;
        Float photoCroplt;
        Float photoCroprb;
        Long diecastUuid;
        LocalDateTime createdAt;
    }



    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiecastListDTO {
        List<DiecastDTO> diecastList;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiecastDTO {
        Long diecastUuid;
        int diecastOkng;
        LocalDateTime createdAt;
        Long diecastvideoUuid;
    }



    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class DiecastGraphDTO {
        int diecastOk;
        int diecastNg;
    }

}
