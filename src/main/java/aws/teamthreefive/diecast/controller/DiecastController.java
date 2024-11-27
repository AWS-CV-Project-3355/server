package aws.teamthreefive.diecast.controller;

import aws.teamthreefive.diecast.converter.DiecastConverter;
import aws.teamthreefive.diecast.dto.request.DiecastRequestDTO;
import aws.teamthreefive.diecast.dto.response.DiecastResponseDTO;
import aws.teamthreefive.diecast.entity.Diecast;
import aws.teamthreefive.diecast.service.DiecastCommandService;
import aws.teamthreefive.diecast.service.DiecastQueryService;
import aws.teamthreefive.photo.entity.Photo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/diecast")
public class DiecastController {

    // POST
    private final DiecastCommandService diecastCommandService;
    // GET
    private final DiecastQueryService diecastQueryService;

    @PostMapping(value = "/{diecastUuid}", consumes = "multipart/form-data")
    @Operation(summary = "사진 저장 API", description = "업로드된 사진 저장")
    public DiecastResponseDTO.SavePhotoResultDTO savePhoto(
            @ModelAttribute DiecastRequestDTO.PhotoDTO request,
            @PathVariable(name = "diecastUuid") Long diecastUuid
    ) {

        Photo photo = diecastCommandService.savePhoto(diecastUuid, request);

        return DiecastConverter.toSavePhotoResultDTO(photo);

    }

    @GetMapping(value = "/{diecastUuid}/photo/list")
    @Operation(summary = "객체별 사진 5개 API", description = "객체별 사진 5개 리스트")
    public DiecastResponseDTO.PhotoListDTO getPhotoList(@PathVariable(name = "diecastUuid") Long diecastUuid) {

        List<Photo> photoList = diecastQueryService.getPhotoList(diecastUuid);

        return DiecastConverter.photoListDTO(photoList);

    }

    @GetMapping(value = "/list")
    @Operation(summary = "객체(오브젝트) 리스트 API", description = "좌측 객체 오브젝트 리스트 조회")
    public DiecastResponseDTO.DiecastListDTO getDiecastList() {

        List<Diecast> diecastList = diecastQueryService.getDiecastList();

        return DiecastConverter.diecastListDTO(diecastList);

    }



}
