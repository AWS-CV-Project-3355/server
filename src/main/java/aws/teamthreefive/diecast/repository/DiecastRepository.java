package aws.teamthreefive.diecast.repository;

import aws.teamthreefive.diecast.entity.Diecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface DiecastRepository extends JpaRepository<Diecast, Long> {

//    @Query("SELECT d.diecastOkng, COUNT(d) FROM Diecast d GROUP BY d.diecastOkng")
//    List<Object[]> countByDiecastOkng();

}
