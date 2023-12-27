package os.place.recruterpro.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import os.place.recruterpro.dtos.OffreDTO;
import os.place.recruterpro.dtos.PostuleDto;
import os.place.recruterpro.dtos.offre.request.RequestOffre;
import os.place.recruterpro.dtos.offre.request.RequestPostuleOffre;
import os.place.recruterpro.services.OffreService;

@RestController
@RequestMapping("api/v1/offre")
public class OffreController {
    private OffreService offreService;

    @Autowired
    public OffreController(OffreService offreService) {
        this.offreService = offreService;
    }


    @PostMapping("/create")
    public ResponseEntity<String> createOffre(@RequestBody RequestOffre requestOffre) {
        OffreDTO offreDTO= offreService.storeOffre(requestOffre);
        return new ResponseEntity<>("Post created successfully", HttpStatus.CREATED);
    }

    @PostMapping("/valide")
    public ResponseEntity<String> validationOffre(@RequestBody OffreDTO offreDTO) {

        return new ResponseEntity<>("Validation realised successfully", HttpStatus.CREATED);
    }


    @PostMapping("/postuler")
    public ResponseEntity<String> postuleOffre(@RequestBody RequestPostuleOffre requestPostuleOffre){
//        PostuleDto postuleDto = this.offreService.potuleOffre(requestPostuleOffre);
        return new ResponseEntity<>("the postule was add successfully " , HttpStatus.CREATED);
    }
}
