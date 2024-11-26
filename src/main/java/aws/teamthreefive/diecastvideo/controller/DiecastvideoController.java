package aws.teamthreefive.diecastvideo.controller;

import aws.teamthreefive.diecastvideo.converter.DiecastvideoConverter;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoRequestDTO;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoResponseDTO;
import aws.teamthreefive.diecastvideo.entity.Diecastvideo;
import aws.teamthreefive.diecastvideo.service.DiecastvideoCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class DiecastvideoController {

    private final DiecastvideoCommandService diecastvideoCommandService;

    // 영상 저장
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "영상 저장 API", description = "업로드된 영상 저장")
    public DiecastvideoResponseDTO.SaveDiecastvideoResultDTO saveDiecastvideo(
            @ModelAttribute DiecastvideoRequestDTO.DiecastvideoDTO request
    ) {

        Diecastvideo diecastvideo = diecastvideoCommandService.saveDiecastvideo(request);

        return DiecastvideoConverter.toSaveDiecastvideoResultDTO(diecastvideo);

    }

}
