package aws.teamthreefive.diecast.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

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
        BigDecimal photoCroplt;
        BigDecimal photoCroprb;
        Long diecastUuid;
    }

}
