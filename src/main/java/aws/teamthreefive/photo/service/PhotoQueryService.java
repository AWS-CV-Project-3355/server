package aws.teamthreefive.photo.service;

import aws.teamthreefive.photo.entity.Photo;
import aws.teamthreefive.photo.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PhotoQueryService {

    private final PhotoRepository photoRepository;

    public List<Photo> getPhotoListNgAll() {

        List<Photo> photoList = photoRepository.findAllByPhotoNgtypeNot(0);

        return photoList;

    }

}
