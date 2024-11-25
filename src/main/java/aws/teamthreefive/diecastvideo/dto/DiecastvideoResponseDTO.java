package aws.teamthreefive.diecastvideo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class DiecastvideoResponseDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SaveDiecastvideoResultDTO {
        String diecastvideoUrl;
    }

}
