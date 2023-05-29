package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgencyDTO;
import ma.ensa.ebanking.dto.PaymentServiceDto;
import ma.ensa.ebanking.dto.ServiceDTO;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.Service;
import ma.ensa.ebanking.models.ServicePayment;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.AgencyRepository;
import ma.ensa.ebanking.repositories.ServicePaymentRepository;
import ma.ensa.ebanking.repositories.ServiceRepository;

import java.util.List;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;

    private final ServiceRepository serviceRepository;

    private final ServicePaymentRepository servicePaymentRepository;

    // ------------ agency CRUD ------------

    public void addAgency(AgencyDTO dto) throws Exception{

        // the admin is the only one who has this permission
        AuthService.Auths.checkAdmin();

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

    public AgencyDTO getAgency(String imm) throws Exception{
        // check auth
        User user = AuthService.Auths.getUser();

        if(user instanceof Agent){
            throw new PermissionException();
        }

        Agency agency = agencyRepository.findById(imm).orElseThrow(
                () -> new RecordNotFoundException("agency not found")
        );

        if(user instanceof Client){
            agency.showActiveServicesOnly();
        }

        return AgencyDTO.builder()
                .immId(agency.getImm())
                .name(agency.getName())
                .patentId(agency.getPatentId())
                .services(agency.getServices())
                .build();
    }

    public List<AgencyDTO> getAllAgencies() throws Exception{
        // check auth
        User user = AuthService.Auths.getUser();

        if(user instanceof Agent){
            throw new PermissionException();
        }

        List<Agency> agencies = agencyRepository.findAll();

        if(user instanceof Client){
            agencies.forEach(Agency::showActiveServicesOnly);
        }

        return agencies
            .stream()
            .map(
                a -> AgencyDTO.builder()
                    .name(a.getName())
                    .patentId(a.getPatentId())
                    .immId(a.getImm())
                    .services(a.getServices())
                    .build()
            ).toList();
    }

    // ------------ service CRUD ------------

    public String addService(String imm, ServiceDTO dto) throws Exception{

        // check the permission
        AuthService.Auths.checkAdmin();

        // get the agency
        Agency agency = agencyRepository.findById(imm)
                .orElseThrow(
                        () -> new RecordNotFoundException("record not found")
                );

        // add the service
        final Service service = Service.builder()
                .agency(agency)
                .name(dto.getName())
                .type(dto.getType())
                .build();

        // return the new id
        return serviceRepository
                .save(service)
                .getId();
    }

    public void toggleService(String id) throws Exception{

        AuthService.Auths.checkAdmin();

        if(!serviceRepository.toggleService(id)){
            throw new RecordNotFoundException("service not found");
        }

    }

    // ------------ payment service CRUD ------------

    public void payService(PaymentServiceDto dto) throws Exception {

        Client client = AuthService.Auths.getClient();

        Service service = serviceRepository
                .findById(dto.getServiceId())
                .orElseThrow(
                        () -> new RecordNotFoundException("service not found")
                );




        ServicePayment servicePayment =
                ServicePayment.builder()
                        .service(service)
                        .client(client)
                        .amount(dto.getAmount())
                        .data(dto.getData())
                    .build();


        servicePaymentRepository.save(servicePayment);

    }

}