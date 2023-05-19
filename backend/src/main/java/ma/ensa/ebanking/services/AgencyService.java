package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgencyDTO;
import ma.ensa.ebanking.dto.ServiceDTO;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.Service;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.AgencyRepository;
import ma.ensa.ebanking.repositories.ServiceRepository;

import java.util.List;
import java.util.Optional;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;

    private final ServiceRepository serviceRepository;

    // ------------ agency CRUD ------------

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
        // check auth
        User user = Auths.getUser();

        if(user instanceof Agent){
            throw new PermissionException();
        }

        Optional<Agency> agency = agencyRepository.findById(imm);

        if(agency.isEmpty()){
            throw new RecordNotFoundException("agency not found");
        }

        Agency agency1 = agency.get();

        if(user instanceof Client){
            agency1.showActiveServicesOnly();
        }

        return agency1;
    }

    public List<Agency> getAllAgencies() throws Exception{
        // check auth
        User user = Auths.getUser();

        if(user instanceof Agent){
            throw new PermissionException();
        }

        List<Agency> agencies = agencyRepository.findAll();

        if(user instanceof Client){
            agencies.forEach(Agency::showActiveServicesOnly);
        }

        return agencies;
    }

    // ------------ service CRUD ------------

    public String addService(String imm, ServiceDTO dto) throws Exception{

        // check the permission
        Auths.getAdmin();

        // get the agency
        Optional<Agency> agency = agencyRepository.findById(imm);
        if(agency.isEmpty()){
            throw new RecordNotFoundException("record not found");
        }

        // add the service
        final Service service = Service.builder()
                .agency(agency.get())
                .name(dto.getName())
                .type(dto.getType())
                .build();
        // return the new id
        return serviceRepository
                .save(service)
                .getId();
    }

    public void disableService(String id) throws Exception{
        // check the admin
        Auths.getAdmin();

        // disable the service
        if(!serviceRepository.disableService(id)){
            throw new RecordNotFoundException("service not found");
        }

    }

    public void enableService(String id) throws Exception{

        Auths.getAdmin();

        // disable the service
        if(!serviceRepository.enableService(id)){
            throw new RecordNotFoundException("service not found");
        }

    }

    // ------------ payment service CRUD ------------

}