package aws.teamthreefive.photo.repository;

import aws.teamthreefive.photo.entity.Photo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PhotoRepository extends JpaRepository<Photo, Long> {

}
