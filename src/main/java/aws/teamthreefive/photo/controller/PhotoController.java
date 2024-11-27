package aws.teamthreefive.photo.controller;

import aws.teamthreefive.photo.converter.PhotoConverter;
import aws.teamthreefive.photo.dto.response.PhotoResponseDTO;
import aws.teamthreefive.photo.entity.Photo;
import aws.teamthreefive.photo.service.PhotoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/photo")
public class PhotoController {

    private final PhotoQueryService photoQueryService;

    @GetMapping("/list/ng/all")
    @Operation(summary = "하단 전체 NG 사진 리스트", description = "전체 NG 사진 리스트 ALL")
    public PhotoResponseDTO.PhotoListDTO getPhotoListNgAll() {

        List<Photo> photoList = photoQueryService.getPhotoListNgAll();

        return PhotoConverter.photoListDTO(photoList);

    }

    @GetMapping("/list/ng/{photoPosition}")
    @Operation(summary = "하단 카메라별 NG 사진 리스트", description = "카메라별 NG 사진 리스트 1 2 3 4 5")
    public PhotoResponseDTO.PhotoListDTO getPhotoListNgCamera(
            @PathVariable(name = "photoPosition") int photoPosition
    ) {

        List<Photo> photoList = photoQueryService.getPhotoListNgCamera(photoPosition);

        return PhotoConverter.photoListDTO(photoList);

    }

}
