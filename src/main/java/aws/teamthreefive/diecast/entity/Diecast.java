package aws.teamthreefive.diecast.entity;

import aws.teamthreefive.photo.entity.Photo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@DynamicInsert
// insert와 update 시 null 인 경우는 그냥 쿼리를 보내지 않도록
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Diecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "diecast_uuid", nullable = false, columnDefinition = "bigint")
    private Long diecastUuid;

    @Column(name = "diecast_okng", nullable = false, columnDefinition = "int")
    @ColumnDefault("0")
    private int diecastOkng;



    @OneToMany(mappedBy = "diecast", cascade = CascadeType.ALL)
    //@OneToMany(mappedBy = "diecast", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photo = new ArrayList<Photo>();

}
