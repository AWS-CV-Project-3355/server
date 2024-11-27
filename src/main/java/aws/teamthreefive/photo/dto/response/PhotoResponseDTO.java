package aws.teamthreefive.photo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class PhotoResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoListDTO {
        List<PhotoResponseDTO.PhotoDTO> photoList;
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
        LocalDateTime createdAt;
        Long diecastUuid;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoGraphDTO {
        int photoNgtypeOne;
        int photoNgtypeTwo;
        int photoNgtypeThree;
        int photoNgtypeFour;
    }

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class PhotoGraphPositionDTO {
        int photoNgtypeOne;
        int photoNgtypeTwo;
        int photoNgtypeThree;
        int photoNgtypeFour;
    }

}
