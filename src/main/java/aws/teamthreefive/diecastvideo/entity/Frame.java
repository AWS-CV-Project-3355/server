package aws.teamthreefive.diecastvideo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

@Table(name = "frame")
public class Frame {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private Long videoId;  // Diecastvideo와 연관된 ID
        private String photoUuid;
        private String photoUrl;
        private int photoPosition;

        // Getters, Setters, Constructors

}
