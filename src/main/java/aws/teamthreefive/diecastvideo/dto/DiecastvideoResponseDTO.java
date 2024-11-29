package aws.teamthreefive.diecastvideo.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

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


        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class FrameDTO {
            private String photoUuid;
            private String photoUrl;
            private int photoPosition;
        }

        @Builder
        @Getter
        @NoArgsConstructor
        @AllArgsConstructor
        public static class FrameResultDTO {
            private Long objectId;
            private List<FrameDTO> frame;
        }


}
