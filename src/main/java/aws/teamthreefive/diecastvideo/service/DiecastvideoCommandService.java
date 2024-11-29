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
import java.util.*;
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

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(
                Uuid.builder()
                        .uuid(uuid)
                        .build()
        );

        String diecastvideoUrl = s3Manager.uploadFileWithContentType(s3Manager.generateVideoKeyName(savedUuid), request.getDiecastvideo());

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
            // 필터 설정: 4.2초 주기 동안 2.463초 동안 5개의 이미지를 추출하고, 나머지 1.n초는 건너뛰기, fps=5%2.463
            String filter = "select='lt(mod(t,4.4),2.43)',fps=2.057";
            //2.47, 2.475, 2.472, 2.468, 2.463(best), 2.461(best), 2.462(최악), 2.45(최악), 2.464(최악)
            //2.465
            //4.4초 주기 동안 0.48초 간격(총 2.44초 동안 5개 이미지 추출)
//            String filter = "select='lt(mod(t,4.4),2.44)',fps=2.049";


            ProcessBuilder processBuilder = new ProcessBuilder(
                    "ffmpeg", "-i", videoFile.getAbsolutePath(),
                    "-vf", filter,
                    "-fps_mode", "vfr",
                    outputPath
            );

            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();

            // 표준 출력 확인
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    log.info("ffmpeg output: {}", line);
                }
            }

            // 프로세스 완료 대기
            int exitCode = process.waitFor();
            log.info("ffmpeg command finished with exit code: {}", exitCode);

            if (exitCode != 0) {
                throw new RuntimeException("FFmpeg 실행 오류 발생");
            }

            // 프레임 이미지 파일 리스트 추출
//            File[] files = outputDir.listFiles((dir, name) -> name.startsWith("frame_") && name.endsWith(".jpg"));

            File[] files = outputDir.listFiles((dir, name) -> name.endsWith(".jpg"));
            Arrays.sort(files, Comparator.comparing(File::getName));

            if (files != null) {
                Arrays.sort(files, Comparator.comparing(File::getName));
                log.info("Frames extracted: {}", files.length);

                List<File> frameBatch = new ArrayList<>();
                int frameCount = 0;
                int frameIndex = 1;
                int objectIndex = 1;
                int cameraIndex = 1;

                for (File file : files) {
                    frameBatch.add(file);

                    // 9개 프레임 묶음 처리
                    if (frameBatch.size() == 9 || frameBatch.size() + frameCount == files.length) {
                        log.info("Processing batch of 9 frames...");

                        // 앞 5개는 업로드
                        for (int i = 0; i < 5 && i < frameBatch.size(); i++) {
                            File frameFile = frameBatch.get(i);
                            MultipartFile multipartFile = convertToMultipartFile(frameFile);

                            // S3 업로드
                            String frameUrl = s3Manager.uploadFileWithContentType("framestest13/" + String.format("frame_%d_%d_%03d.jpg", objectIndex, cameraIndex, frameIndex), multipartFile);

                            frameUrls.add(frameUrl);

                            // 프레임 메타데이터 저장
                            Frame frame = new Frame();
                            frame.setVideoId(videoId);
                            frame.setPhotoUuid(UUID.randomUUID().toString());
                            frame.setPhotoUrl(frameUrl);
                            frame.setPhotoPosition(frameIndex);
                            frameRepository.save(frame);

                            // 로컬 파일 삭제
                            frameFile.delete();

                            frameIndex++;
                            cameraIndex++;
                        }

                        // 뒤 4개는 삭제
                        for (int i = 5; i < frameBatch.size(); i++) {
                            File frameFile = frameBatch.get(i);
                            log.info("Deleting frame: {}", frameFile.getName());
                            frameFile.delete();
                        }

                        objectIndex++;
                        cameraIndex = 1;

                        // 묶음 초기화
                        frameBatch.clear();
                    }
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