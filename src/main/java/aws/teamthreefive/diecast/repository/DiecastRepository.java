package aws.teamthreefive.diecast.repository;

import aws.teamthreefive.diecast.entity.Diecast;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiecastRepository extends JpaRepository<Diecast, Long> {

}
