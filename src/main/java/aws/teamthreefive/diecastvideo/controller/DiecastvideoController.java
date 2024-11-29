package aws.teamthreefive.diecastvideo.controller;

import aws.teamthreefive.aws.s3.AmazonS3Manager;
import aws.teamthreefive.diecastvideo.converter.DiecastvideoConverter;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoRequestDTO;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoResponseDTO;
import aws.teamthreefive.diecastvideo.entity.Diecastvideo;
import aws.teamthreefive.diecastvideo.repository.FrameRepository;
import aws.teamthreefive.diecastvideo.service.DiecastvideoCommandService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;


// DiecastvideoController.java
@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/video")
public class DiecastvideoController {

    private final DiecastvideoCommandService diecastvideoCommandService;

    private final AmazonS3Manager s3Manager;
    private static final Logger log = LoggerFactory.getLogger(DiecastvideoCommandService.class);

    // 영상 저장
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "영상 저장 API", description = "업로드된 영상 저장")
    public DiecastvideoResponseDTO.SaveDiecastvideoResultDTO saveDiecastvideo(
            @ModelAttribute DiecastvideoRequestDTO.DiecastvideoDTO request
    ) {
        Diecastvideo diecastvideo = diecastvideoCommandService.saveDiecastvideo(request);
        return DiecastvideoConverter.toSaveDiecastvideoResultDTO(diecastvideo);
    }

    @GetMapping("/frames")
    @Operation(summary = "MP4 영상의 프레임 추출 API", description = "MP4 영상에서 초 단위로 이미지를 추출")
    public DiecastvideoResponseDTO.FrameResultDTO extractFramesFromVideo(
            @RequestParam("videoId") Long videoId
    ) {
        // 영상 정보 조회
        String videoUrl = diecastvideoCommandService.getVideoUrlById(videoId);

        // 실제 프레임 추출 로직 추가
        try {
            // 1. Download the video from S3 to a temporary file
            File videoFile = s3Manager.downloadFileToTemp(videoUrl);

            // 2. Extract frames and upload them
            List<String> frameUrls = diecastvideoCommandService.extractAndUploadFrames(videoFile, videoId);

            // 3. Return the extracted frames
            return diecastvideoCommandService.extractFrames(videoId);
        } catch (Exception e) {
            log.error("Frame extraction failed for video ID {}: {}", videoId, e.getMessage(), e);
            throw new RuntimeException("프레임 추출에 실패했습니다.", e);
        }
    }
}