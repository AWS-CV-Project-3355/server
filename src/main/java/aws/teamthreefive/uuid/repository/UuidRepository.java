package aws.teamthreefive.uuid.repository;

import aws.teamthreefive.uuid.entity.Uuid;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UuidRepository extends JpaRepository<Uuid, Long> {

}
