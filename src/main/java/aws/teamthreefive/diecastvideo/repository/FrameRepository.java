package aws.teamthreefive.diecastvideo.repository;

import aws.teamthreefive.diecastvideo.entity.Frame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FrameRepository extends JpaRepository<Frame, Long> {
    List<Frame> findByVideoId(Long videoId);
}
