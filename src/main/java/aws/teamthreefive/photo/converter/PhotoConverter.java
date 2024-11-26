package aws.teamthreefive.photo.converter;

import aws.teamthreefive.photo.entity.Photo;

public class PhotoConverter {

    public static Photo toPhoto(String photoUrl, Photo photo) {
        return Photo.builder()
                .photoUrl(photoUrl)
                .photoPosition(photo.getPhotoPosition())
                .photoNgtype(photo.getPhotoNgtype())
                .photoCroplt(photo.getPhotoCroplt())
                .photoCroprb(photo.getPhotoCroprb())
                .build();
    }

}
