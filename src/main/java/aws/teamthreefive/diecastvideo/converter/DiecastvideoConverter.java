package aws.teamthreefive.diecastvideo.converter;

import aws.teamthreefive.diecastvideo.dto.DiecastvideoRequestDTO;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoResponseDTO;
import aws.teamthreefive.diecastvideo.entity.Diecastvideo;

public class DiecastvideoConverter {

    public static Diecastvideo toDiecastvideo(String diecastvideoUrl) {
        return Diecastvideo.builder()
                .diecastvideoUrl(diecastvideoUrl)
                .build();
    }

    public static DiecastvideoResponseDTO.SaveDiecastvideoResultDTO toSaveDiecastvideoResultDTO(Diecastvideo diecastvideo) {
        return DiecastvideoResponseDTO.SaveDiecastvideoResultDTO.builder()
                .diecastvideoUrl(diecastvideo.getDiecastvideoUrl())
                .build();
    }

}
