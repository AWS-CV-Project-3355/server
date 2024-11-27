package aws.teamthreefive.photo.controller;

import aws.teamthreefive.photo.converter.PhotoConverter;
import aws.teamthreefive.photo.dto.response.PhotoResponseDTO;
import aws.teamthreefive.photo.entity.Photo;
import aws.teamthreefive.photo.service.PhotoQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}
