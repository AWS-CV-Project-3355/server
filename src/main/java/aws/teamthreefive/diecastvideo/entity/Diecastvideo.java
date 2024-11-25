package aws.teamthreefive.diecastvideo.entity;

import aws.teamthreefive.diecast.entity.Diecast;
import jakarta.persistence.*;
import lombok.*;

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



    @OneToMany(mappedBy = "diecastvideo", cascade = CascadeType.ALL)
    //@OneToMany(mappedBy = "diecastvideo", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Diecast> diecast = new ArrayList<Diecast>();

}
