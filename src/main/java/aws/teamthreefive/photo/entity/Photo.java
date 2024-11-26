package aws.teamthreefive.photo.entity;

import aws.teamthreefive.diecast.entity.Diecast;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@DynamicUpdate
@DynamicInsert
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Photo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "photo_uuid", nullable = false, columnDefinition = "bigint")
    private Long photoUuid;

    @Column(name = "photo_url", nullable = false, columnDefinition = "varchar(500)")
    private String photoUrl;

    @Column(name = "photo_position", nullable = true, columnDefinition = "int")
    private int photoPosition;

    @Column(name = "photo_ngtype", nullable = false, columnDefinition = "int")
    @ColumnDefault("0")
    private int photoNgtype;

    @Column(name = "photo_croplt", nullable = true, columnDefinition = "decimal")
    private BigDecimal photoCroplt;

    @Column(name = "photo_croprb", nullable = true, columnDefinition = "decimal")
    private BigDecimal photoCroprb;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diecast_uuid")
    private Diecast diecast;

    public void setDiecast(Diecast diecast) {
        if (this.diecast != null) {
            diecast.getPhoto().remove(this);
        }

        this.diecast = diecast;

        diecast.getPhoto().add(this);
    }

}
