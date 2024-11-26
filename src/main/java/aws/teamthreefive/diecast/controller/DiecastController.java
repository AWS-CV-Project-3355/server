package aws.teamthreefive.diecast.controller;

import aws.teamthreefive.diecast.converter.DiecastConverter;
import aws.teamthreefive.diecast.dto.request.DiecastRequestDTO;
import aws.teamthreefive.diecast.dto.response.DiecastResponseDTO;
import aws.teamthreefive.diecast.service.DiecastCommandService;
import aws.teamthreefive.photo.entity.Photo;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diecast")
public class DiecastController {

    private final DiecastCommandService diecastCommandService;

    @PostMapping(value = "/{diecastUuid}", consumes = "multipart/form-data")
    @Operation(summary = "사진 저장 API", description = "업로드된 사진 저장")
    public DiecastResponseDTO.SavePhotoResultDTO savePhoto(
            @ModelAttribute DiecastRequestDTO.PhotoDTO request,
            @PathVariable(name = "diecastUuid") Long diecastUuid
    ) {

        Photo photo = diecastCommandService.savePhoto(diecastUuid, request);

        return DiecastConverter.toSavePhotoResultDTO(photo);
    }


}
