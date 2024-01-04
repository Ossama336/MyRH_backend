package os.place.recruterpro.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import os.place.recruterpro.dtos.offre.request.RequestPostuleOffre;
import os.place.recruterpro.services.PostuleService;

@RestController
@CrossOrigin(value = "*")
@RequestMapping("api/v1/postule")
public class PostuleController {
    private PostuleService postuleService;

    public PostuleController(PostuleService postuleService) {
        this.postuleService = postuleService;
    }

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> postuleOffre(   @RequestParam("offreId") Long offreId,
                                                  @RequestParam("nom_complet") String nomComplet,
                                                  @RequestParam("tel") int tel,
                                                  @RequestParam("cv") MultipartFile cv,
                                                  @RequestParam("motivation") MultipartFile motivation){
        RequestPostuleOffre requestPostuleOffre = RequestPostuleOffre.builder().offreId(offreId)
                .cv(cv)
                .motivation(motivation)
                .nom_complet(nomComplet)
                .tel(tel)
                .build();
        this.postuleService.potuleOffre(requestPostuleOffre);
        return ResponseEntity.status(HttpStatus.OK).body("ok");
    }
}
