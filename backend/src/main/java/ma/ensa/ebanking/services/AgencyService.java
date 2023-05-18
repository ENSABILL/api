package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgencyDTO;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.repositories.AgencyRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;

    public void addAgency(AgencyDTO dto) throws Exception{

        // the admin is the only one who has this permission
        Auths.getAdmin();

        // check if the imm is not exist in the repo
        if(agencyRepository.existsById(dto.getImmId())){
            throw new Exception();
        }

        // add the agency
        Agency agency = Agency.builder()
                .imm(dto.getImmId())
                .name(dto.getName())
                .patentId(dto.getPatentId())
                .build();

        agencyRepository.save(agency);
    }

    public Agency getAgency(String imm) throws Exception{

        Optional<Agency> agency =  agencyRepository.findById(imm);

        if(agency.isEmpty()){
            throw new RecordNotFoundException("agency not found");
        }

        return agency.get();
    }

    public List<Agency> getAllAgencies(){
        return null;
    }

}
