package aws.teamthreefive.aws.s3;

import aws.teamthreefive.config.AmazonConfig;
import aws.teamthreefive.uuid.entity.Uuid;
import aws.teamthreefive.uuid.repository.UuidRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Slf4j
@Component
@RequiredArgsConstructor
public class AmazonS3Manager {

    private final AmazonS3 amazonS3;
    private final AmazonConfig amazonConfig;

    public String uploadFileWithContentType(String KeyName, MultipartFile file){
        System.out.println(KeyName);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType(file.getContentType());
        metadata.setContentLength(file.getSize());

        try {
            amazonS3.putObject(new PutObjectRequest(amazonConfig.getBucket(), KeyName, file.getInputStream(), metadata));
        }catch (IOException e){
            log.error("error at AmazonS3Manager uploadFile : {}", (Object) e.getStackTrace());
        }

        return amazonS3.getUrl(amazonConfig.getBucket(), KeyName).toString();
    }

    public String generateVideoKeyName(Uuid uuid){
        return amazonConfig.getVideoPath() + '/' + uuid.getUuid();
    }

    public String generatePhotoKeyName(Uuid uuid){
        return amazonConfig.getPhotoPath() + '/' + uuid.getUuid();
    }

    // 새로 추가된 메서드들
    public File downloadFileToTemp(String fileUrl) throws IOException {
        try {
            // URL에서 버킷과 키 추출
            URL url = new URL(fileUrl);
            String bucket = amazonConfig.getBucket();
            String key = extractKeyFromUrl(url);

            // 임시 파일 생성
            Path tempDir = Files.createTempDirectory("s3-downloads-");
            File tempFile = Files.createTempFile(tempDir, "download-", ".mp4").toFile();
            tempFile.deleteOnExit();

            // S3에서 객체 다운로드
            S3Object s3Object = amazonS3.getObject(new GetObjectRequest(bucket, key));
            try (InputStream inputStream = s3Object.getObjectContent();
                 FileOutputStream outputStream = new FileOutputStream(tempFile)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            log.info("Downloaded file from S3: {} to temporary location: {}", fileUrl, tempFile.getAbsolutePath());
            return tempFile;
        } catch (Exception e) {
            log.error("Error downloading file from S3: {}", fileUrl, e);
            throw new IOException("S3 파일 다운로드 중 오류 발생", e);
        }
    }

    // S3 URL에서 키 추출하는 헬퍼 메서드
    private String extractKeyFromUrl(URL url) {
        String path = url.getPath();
        // URL path가 "/버킷이름/video/키" 형식일 경우
        String[] parts = path.split("/", 4); // path를 "/" 기준으로 분리
        return parts.length >= 4 ? parts[3] : path.substring(1); // 4번째 파트를 반환 (키)
    }


    // MultipartFile을 File로 변환하는 유틸리티 메서드 (필요한 경우)
    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convertedFile = File.createTempFile(
                UUID.randomUUID().toString(),
                ".tmp"
        );
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }
}