package aws.teamthreefive.aws.s3;

import aws.teamthreefive.config.AmazonConfig;
import aws.teamthreefive.uuid.entity.Uuid;
import aws.teamthreefive.uuid.repository.UuidRepository;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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

}
