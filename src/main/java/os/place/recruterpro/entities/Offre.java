package os.place.recruterpro.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import os.place.recruterpro.Enum.NiveauEtude;
import os.place.recruterpro.Enum.StatusOffre;

import java.util.List;
@Entity
@Data @AllArgsConstructor @NoArgsConstructor
public class Offre {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String titre;
    private String description;
    private String profile;
    private float salaire;
    private String profil;

    @Enumerated(EnumType.STRING)
    private NiveauEtude niveau_etude;

    @Enumerated(EnumType.STRING)
    private StatusOffre status;

    @OneToMany(mappedBy="offre")
    private List<Postule> postule;

    @ManyToOne
    @JoinColumn(name = "societeId")
    private Societe societe;

    @PrePersist
    @PreUpdate
    private void checkStatusValue(){
        if (this.status == null || this.status.describeConstable().isEmpty()){
            this.status = StatusOffre.WAITING;
        }
    }
}
