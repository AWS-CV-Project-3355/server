package aws.teamthreefive.diecast.converter;

import aws.teamthreefive.diecast.dto.request.DiecastRequestDTO;
import aws.teamthreefive.diecast.dto.response.DiecastResponseDTO;
import aws.teamthreefive.photo.entity.Photo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class DiecastConverter {

    public static Photo toPhoto(DiecastRequestDTO.PhotoDTO request, String photoUrl) {
        return Photo.builder()
                .photoUrl(photoUrl)
                .photoPosition(request.getPhotoPosition())
                .photoNgtype(request.getPhotoNgtype())
                .photoCroplt(request.getPhotoCroplt())
                .photoCroprb(request.getPhotoCroprb())
                .createdAt(LocalDateTime.now())
                .build();
    }

    public static DiecastResponseDTO.SavePhotoResultDTO toSavePhotoResultDTO(Photo photo) {
        return DiecastResponseDTO.SavePhotoResultDTO.builder()
                .photoUuid(photo.getPhotoUuid())
                .photoUrl(photo.getPhotoUrl())
                .photoPosition(photo.getPhotoPosition())
                .photoNgtype(photo.getPhotoNgtype())
                .photoCroplt(photo.getPhotoCroplt())
                .photoCroprb(photo.getPhotoCroprb())
                .diecastUuid(photo.getDiecast().getDiecastUuid())
                .createdAt(photo.getCreatedAt())
                .build();
    }

    public static DiecastResponseDTO.PhotoDTO photoDTO(Photo photo) {
        return DiecastResponseDTO.PhotoDTO.builder()
                .photoUuid(photo.getPhotoUuid())
                .photoUrl(photo.getPhotoUrl())
                .photoPosition(photo.getPhotoPosition())
                .photoNgtype(photo.getPhotoNgtype())
                .photoCroplt(photo.getPhotoCroplt())
                .photoCroprb(photo.getPhotoCroprb())
                .diecastUuid(photo.getDiecast().getDiecastUuid())
                .createdAt(photo.getCreatedAt())
                .build();
    }

    public static DiecastResponseDTO.PhotoListDTO photoListDTO(List<Photo> photoList) {

        List<DiecastResponseDTO.PhotoDTO> photoDTOList = photoList.stream()
                .map(DiecastConverter::photoDTO).collect(Collectors.toList());

        return DiecastResponseDTO.PhotoListDTO.builder()
                .photoList(photoDTOList)
                .build();

    }

}
