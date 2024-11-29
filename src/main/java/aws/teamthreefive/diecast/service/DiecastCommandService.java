package aws.teamthreefive.diecast.service;

import aws.teamthreefive.aws.s3.AmazonS3Manager;
import aws.teamthreefive.diecast.converter.DiecastConverter;
import aws.teamthreefive.diecast.dto.request.DiecastRequestDTO;
import aws.teamthreefive.diecast.entity.Diecast;
import aws.teamthreefive.diecast.repository.DiecastRepository;
import aws.teamthreefive.diecastvideo.repository.DiecastvideoRepository;
import aws.teamthreefive.photo.entity.Photo;
import aws.teamthreefive.photo.repository.PhotoRepository;
import aws.teamthreefive.uuid.entity.Uuid;
import aws.teamthreefive.uuid.repository.UuidRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class DiecastCommandService {

    private final DiecastRepository diecastRepository;
    private final DiecastvideoRepository diecastvideoRepository;
    private final UuidRepository uuidRepository;
    private final PhotoRepository photoRepository;
    private final AmazonS3Manager s3Manager;

//    public Diecast saveDiecast(Long diecastvideoUuid, DiecastRequestDTO.DiecastDTO request) {
    public Diecast saveDiecast(Long diecastvideoUuid) {

//        Diecast diecast = DiecastConverter.toDiecast(request);
        Diecast diecast = DiecastConverter.toDiecast();

        diecast.setDiecastvideo(diecastvideoRepository.findById(diecastvideoUuid).get());

        return diecastRepository.save(diecast);

    }

    public Diecast patchNgDiecast(Long diecastUuid) {

        Diecast diecast = diecastRepository.findById(diecastUuid).get();

        List<Photo> photos = photoRepository.findAllByDiecast(diecast);

        for (Photo photo : photos) {
            if (photo.getPhotoNgtype() != 0) {
                diecast.setDiecastOkng(1);
            }
        }

        return diecastRepository.save(diecast);

    }

    public Photo savePhoto(Long diecastUuid, DiecastRequestDTO.PhotoDTO request) {

        //Photo photo = DiecastConverter.toPhoto(request);

        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(
                Uuid.builder()
                        .uuid(uuid)
                        .build()
        );

        String photoUrl = s3Manager.uploadFileWithContentType(s3Manager.generatePhotoKeyName(savedUuid), request.getPhotoFile());

        Photo photo = DiecastConverter.toPhoto(request, photoUrl);

        photo.setDiecast(diecastRepository.findById(diecastUuid).get());

        return photoRepository.save(photo);

    }

}
