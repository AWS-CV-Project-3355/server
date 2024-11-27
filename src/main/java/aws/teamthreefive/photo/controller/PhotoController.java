package aws.teamthreefive.photo.controller;

import aws.teamthreefive.diecast.dto.response.DiecastResponseDTO;
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

    @GetMapping(value = "/graph/ng/type")
    @Operation(summary = "전체 불량 유형 그래프 조회", description = "좌측 불량 유형 통계 그래프에 불량 유형 개수 보여주기")
    public PhotoResponseDTO.PhotoGraphDTO getPhotoGraphNgType() {

        PhotoResponseDTO.PhotoGraphDTO photoGraphDTO = photoQueryService.getPhotoGraphNgType();

        return photoGraphDTO;

    }

    @GetMapping(value = "/graph/ng/type/{photoPosition}")
    @Operation(summary = "카메라별 불량 유형 그래프 조회", description = "카메라별로 통계 그래프에 불량 유형 개수 보여주기")
    public PhotoResponseDTO.PhotoGraphDTO getPhotoGraphNgTypePhotoPosition(
            @PathVariable(name = "photoPosition") int photoPosition
    ) {

        PhotoResponseDTO.PhotoGraphDTO photoGraphDTO = photoQueryService.getPhotoGraphNgTypePhotoPosition(photoPosition);

        return photoGraphDTO;

    }

    @GetMapping(value = "/{photoUuid}")
    @Operation(summary = "NG 사진 세부 조회", description = "NG 사진 세부 조회")
    public PhotoResponseDTO.PhotoDTO getPhoto(
            @PathVariable(name = "photoUuid") Long photoUuid
    ) {

        Photo photo = photoQueryService.getPhoto(photoUuid);

        return PhotoConverter.photoDTO(photo);

    }

}
