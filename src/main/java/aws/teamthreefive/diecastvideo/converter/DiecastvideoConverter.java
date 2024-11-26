package aws.teamthreefive.diecastvideo.converter;

import aws.teamthreefive.diecastvideo.dto.DiecastvideoRequestDTO;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoResponseDTO;
import aws.teamthreefive.diecastvideo.entity.Diecastvideo;

import java.time.LocalDateTime;

public class DiecastvideoConverter {

    public static Diecastvideo toDiecastvideo(String diecastvideoUrl) {
        return Diecastvideo.builder()
                .diecastvideoUrl(diecastvideoUrl)
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static DiecastvideoResponseDTO.SaveDiecastvideoResultDTO toSaveDiecastvideoResultDTO(Diecastvideo diecastvideo) {
        return DiecastvideoResponseDTO.SaveDiecastvideoResultDTO.builder()
                .diecastvideoUuid(diecastvideo.getDiecastvideoUuid())
                .diecastvideoUrl(diecastvideo.getDiecastvideoUrl())
                .createdAt(diecastvideo.getCreatedAt())
                .build();
    }

}
