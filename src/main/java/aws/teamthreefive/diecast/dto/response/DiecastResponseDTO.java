package aws.teamthreefive.diecast.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class DiecastResponseDTO {

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

}
