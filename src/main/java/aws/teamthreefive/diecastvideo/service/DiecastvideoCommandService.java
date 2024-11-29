package aws.teamthreefive.diecastvideo.service;

import aws.teamthreefive.aws.s3.AmazonS3Manager;
import aws.teamthreefive.diecastvideo.converter.DiecastvideoConverter;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoResponseDTO;
import aws.teamthreefive.diecastvideo.entity.Frame;
import aws.teamthreefive.diecastvideo.repository.DiecastvideoRepository;
import aws.teamthreefive.diecastvideo.repository.FrameRepository;
import aws.teamthreefive.uuid.entity.Uuid;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoRequestDTO;
import aws.teamthreefive.diecastvideo.entity.Diecastvideo;
import aws.teamthreefive.uuid.repository.UuidRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DiecastvideoCommandService {

    private final DiecastvideoRepository diecastvideoRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;
    private final FrameRepository frameRepository;
    private static final Logger log = LoggerFactory.getLogger(DiecastvideoCommandService.class);


    public Diecastvideo saveDiecastvideo(DiecastvideoRequestDTO.DiecastvideoDTO request) {
        //Diecastvideo diecastvideo = DiecastvideoConverter.toDiecastvideo(request);

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(
                Uuid.builder()
                        .uuid(uuid)
                        .build()
        );

        String diecastvideoUrl = s3Manager.uploadFile(s3Manager.generateVideoKeyName(savedUuid), request.getDiecastvideo());

        return diecastvideoRepository.save(DiecastvideoConverter.toDiecastvideo(diecastvideoUrl));
    }

    public DiecastvideoResponseDTO.FrameResultDTO extractFrames(Long videoId) {
        List<Frame> frames = frameRepository.findByVideoId(videoId);
        log.info("Frames found for video ID {}: {}", videoId, frames.size());  // 프레임 개수 확인

        if (frames.isEmpty()) {
            log.warn("No frames found for video ID: {}", videoId);
        }

        List<DiecastvideoResponseDTO.FrameDTO> frameDTOs = frames.stream()
                .map(frame -> DiecastvideoResponseDTO.FrameDTO.builder()
                        .photoUuid(frame.getPhotoUuid())
                        .photoUrl(frame.getPhotoUrl())
                        .photoPosition(frame.getPhotoPosition())
                        .build())
                .collect(Collectors.toList());

        log.info("FrameDTOs created for video ID {}: {}", videoId, frameDTOs.size());  // 변환된 DTO 확인

        return DiecastvideoResponseDTO.FrameResultDTO.builder()
                .objectId(videoId)
                .frame(frameDTOs)
                .build();
    }

    public List<String> extractAndUploadFrames(File videoFile, Long videoId) {
        List<String> frameUrls = new ArrayList<>();
        try {
            if (!videoFile.exists() || !videoFile.isFile()) {
                log.error("Video file is invalid: {}", videoFile.getAbsolutePath());
                throw new IllegalArgumentException("유효하지 않은 비디오 파일 경로: " + videoFile.getAbsolutePath());
            }

            log.info("Video file found: {}, Size: {} bytes", videoFile.getAbsolutePath(), videoFile.length());

            // 프레임 추출 디렉토리 생성
            File outputDir = new File("/tmp/frames-" + videoId);
            outputDir.mkdirs();

            String outputPath = outputDir.getAbsolutePath() + "/frame_%03d.jpg";
            // 필터 설정: 4.2초 주기 동안 2.4초 동안 5개의 이미지를 추출하고, 나머지 1.8초 동안 대기
            // 정상 동작하는 필터 (그러나 5번 이미지가 5장씩 나옴)
            String filter = "select='if(gte(mod(t,4.2),0),if(lt(mod(t,4.2),2.4),1,0),0)',fps=2.083";

            // 아래는 시도해본 흔적들... 필터를 수정하시면 됩니다
//            String filter = "select='if(lt(mod(t,4.2),2.4),1,0)',fps=2.083";
//            String filter = "select='if(lt(mod(t,4.2),2.4),1,if(lt(mod(t,4.2),4.2),0,1))',fps=2.083";
//            String filter = "select='lt(mod(t,4.2),2.4)',fps=2.083";

            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg", "-i", videoFile.getAbsolutePath(),
                    "-vf", filter,
                    "-vsync", "vfr",
                    outputPath
            );

            // 정상 작동 확인하면 log 코드 전부 삭제 후 아래 두 줄 활성화
//            processBuilder.redirectErrorStream(true);
//            Process process = processBuilder.start();

            // 표준 오류와 표준 출력을 로그로 출력 (정상 작동 시에 삭제 해야 할 코드)
            processBuilder.redirectErrorStream(false);
            Process process = processBuilder.start();
            InputStream errorStream = process.getErrorStream();
            InputStreamReader errorStreamReader = new InputStreamReader(errorStream);
            BufferedReader errorReader = new BufferedReader(errorStreamReader);
            String line;
            while ((line = errorReader.readLine()) != null) {
                log.error("ffmpeg error: {}", line);
            }

            // 프로세스 완료 대기
            int exitCode = process.waitFor();
            log.info("ffmpeg command finished with exit code: {}", exitCode);

            if (exitCode != 0) {
                throw new RuntimeException("ffmpeg 실행 오류: Exit Code " + exitCode);
            }

            // 프레임 이미지 파일 리스트 추출 및 업로드
            File[] files = outputDir.listFiles((dir, name) -> name.startsWith("frame-") && name.endsWith(".jpg"));

            if (files != null) {
                log.info("Frames extracted: {}", files.length);
                for (int i = 0; i < files.length; i++) {
                    File file = files[i];
                    log.info("Processing frame file: {}", file.getName());

                    MultipartFile multipartFile = convertToMultipartFile(file);

                    // S3 업로드
                    String frameUrl = s3Manager.uploadFile("frames/" + file.getName(), multipartFile);
                    frameUrls.add(frameUrl);

                    // 프레임 메타데이터 저장 (db에서 확인하려고 한건데 사실 필요없어서 삭제하셔도 됩니다)
                    Frame frame = new Frame();
                    frame.setVideoId(videoId);
                    frame.setPhotoUuid(UUID.randomUUID().toString());
                    frame.setPhotoUrl(frameUrl);
                    frame.setPhotoPosition(i + 1);
                    log.info("Saving frame metadata: {}", frame);
                    frameRepository.save(frame);

                    // 로컬 파일 삭제
                    file.delete();
                }

                // 임시 디렉토리 삭제
                outputDir.delete();
            } else {
                log.warn("No frames extracted for video ID {} in output directory.", videoId);
            }
        } catch (Exception e) {
            log.error("Error during frame extraction for video ID {}: {}", videoId, e.getMessage(), e);
            throw new RuntimeException("프레임 추출 중 오류 발생: " + e.getMessage(), e);
        }

        return frameUrls;
    }

    // Utility method to convert File to MultipartFile
    private MultipartFile convertToMultipartFile(File file) throws IOException {
        Path path = file.toPath();
        String name = file.getName();
        String originalFileName = name;
        String contentType = Files.probeContentType(path);
        byte[] content = Files.readAllBytes(path);

        return new MultipartFile() {
            @Override
            public String getName() { return name; }
            @Override
            public String getOriginalFilename() { return originalFileName; }
            @Override
            public String getContentType() { return contentType; }
            @Override
            public boolean isEmpty() { return content == null || content.length == 0; }
            @Override
            public long getSize() { return content.length; }
            @Override
            public byte[] getBytes() throws IOException { return content; }
            @Override
            public InputStream getInputStream() throws IOException { return new ByteArrayInputStream(content); }
            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {
                Files.write(dest.toPath(), content);
            }
        };
    }

    public String getVideoUrlById(Long videoId) {
        Diecastvideo diecastvideo = diecastvideoRepository.findById(videoId)
                .orElseThrow(() -> {
                    log.error("No video found for video ID: {}", videoId);
                    return new IllegalArgumentException("해당 영상이 존재하지 않습니다.");
                });
        log.info("Video URL retrieved for video ID {}: {}", videoId, diecastvideo.getDiecastvideoUrl());
        return diecastvideo.getDiecastvideoUrl();
    }

}
