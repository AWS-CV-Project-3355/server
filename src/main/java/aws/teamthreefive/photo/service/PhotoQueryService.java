package aws.teamthreefive.photo.service;

import aws.teamthreefive.photo.converter.PhotoConverter;
import aws.teamthreefive.photo.dto.response.PhotoResponseDTO;
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

    public List<Photo> getPhotoListNgCamera(int photoPosition) {

        List<Photo> photoList = photoRepository.findAllByPhotoPositionAndPhotoNgtypeNot(photoPosition, 0);

        return photoList;

    }

    public PhotoResponseDTO.PhotoGraphDTO getPhotoGraphNgType() {

        List<Photo> photoList = photoRepository.findAll();

        int photoNgtypeOne = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 1).count();
        int photoNgtypeTwo = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 2).count();
        int photoNgtypeThree = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 3).count();
        int photoNgtypeFour = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 4).count();

        PhotoResponseDTO.PhotoGraphDTO photoGraphDTO = PhotoConverter.photoGraphDTO(photoNgtypeOne, photoNgtypeTwo, photoNgtypeThree, photoNgtypeFour);

        return photoGraphDTO;

    }

    public PhotoResponseDTO.PhotoGraphDTO getPhotoGraphNgTypePhotoPosition(int photoPosition) {

        List<Photo> photoList = photoRepository.findAllByPhotoPosition(photoPosition);

        int photoNgtypeOne = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 1).count();
        int photoNgtypeTwo = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 2).count();
        int photoNgtypeThree = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 3).count();
        int photoNgtypeFour = (int) photoList.stream().filter(p -> p.getPhotoNgtype() == 4).count();

        PhotoResponseDTO.PhotoGraphDTO photoGraphDTO = PhotoConverter.photoGraphDTO(photoNgtypeOne, photoNgtypeTwo, photoNgtypeThree, photoNgtypeFour);

        return photoGraphDTO;

    }

    public Photo getPhoto(Long photoUuid) {

        Photo photo = photoRepository.findById(photoUuid).get();

        return photo;

    }

}
