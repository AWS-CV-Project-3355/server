package aws.teamthreefive.diecastvideo.entity;

import aws.teamthreefive.diecast.entity.Diecast;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diecastvideo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diecastvideo_uuid", nullable = false, columnDefinition = "bigint")
    private Long diecastvideoUuid;

    @Column(name = "diecastvideo_url", nullable = false, columnDefinition = "varchar(500)")
    private String diecastvideoUrl;

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "dateTime")
    private LocalDateTime createdAt;



    @OneToMany(mappedBy = "diecastvideo", cascade = CascadeType.ALL)
    //@OneToMany(mappedBy = "diecastvideo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diecast> diecast = new ArrayList<Diecast>();

}
