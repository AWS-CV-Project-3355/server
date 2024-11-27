package aws.teamthreefive.diecast.service;

import aws.teamthreefive.diecast.converter.DiecastConverter;
import aws.teamthreefive.diecast.dto.response.DiecastResponseDTO;
import aws.teamthreefive.diecast.entity.Diecast;
import aws.teamthreefive.diecast.repository.DiecastRepository;
import aws.teamthreefive.photo.entity.Photo;
import aws.teamthreefive.photo.repository.PhotoRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiecastQueryService {

    private final DiecastRepository diecastRepository;
    private final PhotoRepository photoRepository;

    public List<Photo> getPhotoList(Long diecastUuid) {

        Diecast diecast = diecastRepository.findById(diecastUuid).get();

        List<Photo> photoList = photoRepository.findAllByDiecast(diecast);

        return photoList;

    }

    public List<Diecast> getDiecastList() {

        List<Diecast> diecastList = diecastRepository.findAll();

        return diecastList;

    }

    public List<Diecast> getDiecastListNg() {

        List<Diecast> diecastList = diecastRepository.findAllByDiecastOkngNot(0);

        return diecastList;

    }

    public DiecastResponseDTO.DiecastGraphDTO getDiecastGraphOkng() {

        List<Diecast> diecastList = diecastRepository.findAll();

        int diecastOk = (int) diecastList.stream().filter(d -> d.getDiecastOkng() == 0).count();
        int diecastNg = (int) diecastList.stream().filter(d -> d.getDiecastOkng() == 1).count();

        DiecastResponseDTO.DiecastGraphDTO diecastGraphDTO = DiecastConverter.diecastGraphDTO(diecastOk, diecastNg);

        return diecastGraphDTO;

    }

}
