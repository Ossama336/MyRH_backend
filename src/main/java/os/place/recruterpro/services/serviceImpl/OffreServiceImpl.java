package os.place.recruterpro.services.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import os.place.recruterpro.Enum.StatusOffre;
import os.place.recruterpro.dtos.OffreDTO;
import os.place.recruterpro.dtos.offre.request.RequestOffre;
import os.place.recruterpro.dtos.offre.request.RequestSearchOffreDTO;
import os.place.recruterpro.dtos.offre.request.RequestValidationDTO;
import os.place.recruterpro.entities.Offre;
import os.place.recruterpro.entities.Societe;
import os.place.recruterpro.exceptions.exception.AccessOffreException;
import os.place.recruterpro.exceptions.exception.NotExist;
import os.place.recruterpro.exceptions.exception.OffreCreateException;
import os.place.recruterpro.mappers.OffreMapper;
import os.place.recruterpro.mappers.PostuleMapper;
import os.place.recruterpro.repositories.OffreRepository;
import os.place.recruterpro.repositories.PostuleRepository;
import os.place.recruterpro.repositories.SocieteRepository;
import os.place.recruterpro.services.OffreService;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

@Service
public class OffreServiceImpl implements OffreService {
    private final String uploadDirectory = "src/main/resources/static/images";
    private final OffreRepository offreRepository;
    private final PostuleRepository postuleRepository;
    private final SocieteRepository societeRepository;
    private final OffreMapper offreMapper;
    private final PostuleMapper postuleMapper;

    @Autowired
    public OffreServiceImpl(OffreRepository offreRepository, PostuleRepository postuleRepository, SocieteRepository societeRepository) {
        this.offreRepository = offreRepository;
        this.postuleRepository = postuleRepository;
        this.societeRepository = societeRepository;
        this.postuleMapper = PostuleMapper.INSTANCE;
        this.offreMapper = OffreMapper.INSTANCE;
    }

    @Override
    public OffreDTO createOffre(OffreDTO offreDTO) throws OffreCreateException {
        Offre offre = offreMapper.toEntity(offreDTO);
        offre.setStatus(StatusOffre.WAITING);
        offre = this.offreRepository.save(offre);
        return offreMapper.toDTO(offre);
    }
    @Override
    public OffreDTO storeOffre(RequestOffre requestOffre) {
        Offre offre = offreMapper.toEntity(requestOffre.getOffreDTO());
        Optional<Societe> societeOpt = societeRepository.findById(requestOffre.getSocieteId());
        if (societeOpt.isPresent()){
            Societe societe = societeOpt.get();
            offre.setSociete(societe);
            offreRepository.save(offre);
            return offreMapper.toDTO(offre);
        }else throw new NotExist("the societe don't exist");

    }

    @Override
    public OffreDTO validationOffre(RequestValidationDTO validationDTO) {
        Optional<Offre> offreOpt = offreRepository.findById(validationDTO.getIdOffre());
        if (offreOpt.isPresent()){
           Offre offre = offreOpt.get();
           offre.setStatus(StatusOffre.valueOf(validationDTO.getStatus()));
           offreRepository.save(offre);
           return offreMapper.toDTO(offre);
        }else throw new AccessOffreException(" the offre is not exist");
    }

    @Override
    public Page<Offre> listOffrePageable(Map<String, Integer> query) {
        int pageInitial = 0;
        int sizeInitial = 10;
        if (query.get("size") < 10 || query.get("page") < 0){
            return offreRepository.findAll(PageRequest.of(pageInitial, sizeInitial));
        }else {
            return offreRepository.findAll(PageRequest.of(query.get("page"), query.get("size")));
        }
    }

    @Override
    public List<OffreDTO> SearchOffre(RequestSearchOffreDTO requestSearchOffreDTO) {
        Specification<Offre> sec = Specification.where(null);
        String nvEduction = requestSearchOffreDTO.getNiveau_etude();
        String profile = requestSearchOffreDTO.getProfile();
        String status = String.valueOf(StatusOffre.ACCEPTED);
        if( profile != null || !profile.isEmpty() ){
            sec = sec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("profile"),profile)));
        }
        if ( nvEduction != null && nvEduction.isEmpty()){
            sec = sec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("niveau_etude"), nvEduction)));
        }


        sec = sec.and(((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status)));
        List<Offre> listOffre = offreRepository.findAll(sec);

        List<OffreDTO> offreDTOS = this.offreMapper.toDtoList(listOffre);
        return offreDTOS;
    }

}
