package aws.teamthreefive.diecast.converter;

import aws.teamthreefive.diecast.dto.request.DiecastRequestDTO;
import aws.teamthreefive.diecast.dto.response.DiecastResponseDTO;
import aws.teamthreefive.photo.entity.Photo;

public class DiecastConverter {

    public static Photo toPhoto(DiecastRequestDTO.PhotoDTO request) {
        return Photo.builder()
                .photoPosition(request.getPhotoPosition())
                .photoNgtype(request.getPhotoNgtype())
                .photoCroplt(request.getPhotoCroplt())
                .photoCroprb(request.getPhotoCroprb())
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
                .build();
    }

}
