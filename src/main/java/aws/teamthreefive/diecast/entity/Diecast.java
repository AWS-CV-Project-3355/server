package aws.teamthreefive.diecast.entity;

import aws.teamthreefive.diecastvideo.entity.Diecastvideo;
import aws.teamthreefive.photo.entity.Photo;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
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

    @CreatedDate
    @Column(name = "created_at", nullable = false, columnDefinition = "dateTime")
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diecastvideo_uuid")
    private Diecastvideo diecastvideo;



    @Builder.Default
    @OneToMany(mappedBy = "diecast", cascade = CascadeType.ALL)
    //@OneToMany(mappedBy = "diecast", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Photo> photo = new ArrayList<Photo>();



    public void setDiecastvideo(Diecastvideo diecastvideo) {
        if (this.diecastvideo != null) {
            diecastvideo.getDiecast().remove(this);
        }

        this.diecastvideo = diecastvideo;

        diecastvideo.getDiecast().add(this);
    }

    public void setDiecastOkng(int diecastOkng) {
        this.diecastOkng = diecastOkng;
    }

}
