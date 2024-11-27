package aws.teamthreefive.photo.converter;

import aws.teamthreefive.photo.dto.response.PhotoResponseDTO;
import aws.teamthreefive.photo.entity.Photo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PhotoConverter {

    public static PhotoResponseDTO.PhotoDTO photoDTO(Photo photo) {
        return PhotoResponseDTO.PhotoDTO.builder()
                .photoUuid(photo.getPhotoUuid())
                .photoUrl(photo.getPhotoUrl())
                .photoPosition(photo.getPhotoPosition())
                .photoNgtype(photo.getPhotoNgtype())
                .photoCroplt(photo.getPhotoCroplt())
                .photoCroprb(photo.getPhotoCroprb())
                .createdAt(photo.getCreatedAt())
                .diecastUuid(photo.getDiecast().getDiecastUuid())
                .build();
    }

    public static PhotoResponseDTO.PhotoListDTO photoListDTO(List<Photo> photoList) {

        List<PhotoResponseDTO.PhotoDTO> photoDTOList = photoList.stream()
                .map(PhotoConverter::photoDTO).collect(Collectors.toList());

        return PhotoResponseDTO.PhotoListDTO.builder()
                .photoList(photoDTOList)
                .build();

    }

    public static PhotoResponseDTO.PhotoGraphDTO photoGraphDTO(int photoNgtypeOne, int photoNgtypeTwo, int photoNgtypeTree, int photoNgtypeFour) {
        return PhotoResponseDTO.PhotoGraphDTO.builder()
                .photoNgtypeOne(photoNgtypeOne)
                .photoNgtypeTwo(photoNgtypeTwo)
                .photoNgtypeThree(photoNgtypeTree)
                .photoNgtypeFour(photoNgtypeFour)
                .build();
    }

}
