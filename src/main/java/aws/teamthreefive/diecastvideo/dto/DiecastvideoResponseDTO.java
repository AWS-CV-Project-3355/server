package aws.teamthreefive.diecastvideo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

public class DiecastvideoResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDiecastvideoResultDTO {
        Long diecastvideoUuid;
        String diecastvideoUrl;
        LocalDateTime createdAt;
    }

}
