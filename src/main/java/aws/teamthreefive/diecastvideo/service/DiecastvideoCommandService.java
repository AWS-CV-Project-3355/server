package aws.teamthreefive.diecastvideo.service;

import aws.teamthreefive.aws.s3.AmazonS3Manager;
import aws.teamthreefive.diecastvideo.converter.DiecastvideoConverter;
import aws.teamthreefive.diecastvideo.repository.DiecastvideoRepository;
import aws.teamthreefive.uuid.entity.Uuid;
import aws.teamthreefive.diecastvideo.dto.DiecastvideoRequestDTO;
import aws.teamthreefive.diecastvideo.entity.Diecastvideo;
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
public class DiecastvideoCommandService {

    private final DiecastvideoRepository diecastvideoRepository;
    private final UuidRepository uuidRepository;
    private final AmazonS3Manager s3Manager;

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


}
