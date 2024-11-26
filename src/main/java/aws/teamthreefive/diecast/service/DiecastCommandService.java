package aws.teamthreefive.diecast.service;

import aws.teamthreefive.aws.s3.AmazonS3Manager;
import aws.teamthreefive.diecast.converter.DiecastConverter;
import aws.teamthreefive.diecast.dto.request.DiecastRequestDTO;
import aws.teamthreefive.diecast.repository.DiecastRepository;
import aws.teamthreefive.photo.converter.PhotoConverter;
import aws.teamthreefive.photo.entity.Photo;
import aws.teamthreefive.photo.repository.PhotoRepository;
import aws.teamthreefive.uuid.entity.Uuid;
import aws.teamthreefive.uuid.repository.UuidRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class DiecastCommandService {

    private final DiecastRepository diecastRepository;
    private final UuidRepository uuidRepository;
    private final PhotoRepository photoRepository;
    private final AmazonS3Manager s3Manager;

    public Photo savePhoto(Long diecastUuid, DiecastRequestDTO.PhotoDTO request) {

        Photo photo = DiecastConverter.toPhoto(request);

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(
                Uuid.builder()
                        .uuid(uuid)
                        .build()
        );

        String fileUrl = s3Manager.uploadFile(s3Manager.generatePhotoKeyName(savedUuid), request.getPhotoFile());

        photo.setDiecast(diecastRepository.findById(diecastUuid).get());

        return photoRepository.save(PhotoConverter.toPhoto(fileUrl, photo));

    }

}
