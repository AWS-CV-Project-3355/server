package aws.teamthreefive.photo.repository;

import aws.teamthreefive.diecast.entity.Diecast;
import aws.teamthreefive.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

    List<Photo> findAllByDiecast(Diecast diecast);

    List<Photo> findAllByPhotoNgtypeNot(int photoNgtype);

    List<Photo> findAllByPhotoPositionAndPhotoNgtypeNot(int photoPosition, int photoNgtype);

    List<Photo> findAllByPhotoPosition(int photoPosition);

}
