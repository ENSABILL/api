package ma.ensa.ebanking.services;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgencyDTO;
import ma.ensa.ebanking.dto.PaymentServiceDto;
import ma.ensa.ebanking.exceptions.PermissionException;
import ma.ensa.ebanking.exceptions.RecordNotFoundException;
import ma.ensa.ebanking.mapper.ServiceMapper;
import ma.ensa.ebanking.models.Agency;
import ma.ensa.ebanking.models.CreditCard;
import ma.ensa.ebanking.models.Service;
import ma.ensa.ebanking.models.ServicePayment;
import ma.ensa.ebanking.models.user.Agent;
import ma.ensa.ebanking.models.user.Client;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.AgencyRepository;
import ma.ensa.ebanking.repositories.CreditCardRepository;
import ma.ensa.ebanking.repositories.ServicePaymentRepository;
import ma.ensa.ebanking.repositories.ServiceRepository;

import java.util.List;
import java.util.stream.Collectors;

@org.springframework.stereotype.Service
@RequiredArgsConstructor
public class AgencyService {

    private final AgencyRepository agencyRepository;

    private final ServiceRepository serviceRepository;

    private final ServicePaymentRepository servicePaymentRepository;

    private final PaymentService paymentService;

    private final CreditCardRepository creditCardRepository;

    // ------------ agency CRUD ------------

    public void addAgency(AgencyDTO dto) throws Exception {

        // the admin is the only one who has this permission
        if (!AuthService.Auths.checkAdmin()) throw new PermissionException();

        // check if the imm is not exist in the repo
        if (agencyRepository.existsById(dto.getImmId())) {
            throw new Exception();
        }

        CreditCard creditCard = CreditCard.builder()
                .balance(1_000_000)
                .build();

        creditCardRepository.save(creditCard);


        // add the agency
        Agency agency = Agency.builder()
                .imm(dto.getImmId())
                .name(dto.getName())
                .patentId(dto.getPatentId())
                .creditCard(creditCard)
                .image(dto.getImage())
                .build();

        agencyRepository.save(agency);
    }

    public AgencyDTO getAgency(String imm) throws Exception {
        // check auth
        User user = AuthService.Auths.getUser();

        if (user instanceof Agent) {
            throw new PermissionException();
        }

        Agency agency = agencyRepository.findById(imm).orElseThrow(
                () -> new RecordNotFoundException("agency not found")
        );

        if (user instanceof Client) {
            agency.showActiveServicesOnly();
        }

        return AgencyDTO.builder()
                .immId(agency.getImm())
                .name(agency.getName())
                .patentId(agency.getPatentId())
                .image(agency.getImage())
                .services(agency.getServices().stream().map(ServiceMapper::toDto).collect(Collectors.toList()))
                .build();
    }

    public List<AgencyDTO> getAllAgencies() throws Exception {
        // check auth
        User user = AuthService.Auths.getUser();

        if (user instanceof Agent) {
            throw new PermissionException();
        }

        List<Agency> agencies = agencyRepository.findAll();

        if (user instanceof Client) {
            agencies.forEach(Agency::showActiveServicesOnly);
        }

        return agencies
                .stream()
                .map(
                        a -> AgencyDTO.builder()
                                .name(a.getName())
                                .patentId(a.getPatentId())
                                .immId(a.getImm())
                                .image(a.getImage())
                                .services(a.getServices().stream().map(ServiceMapper::toDto).collect(Collectors.toList()))
                                .build()
                ).toList();
    }

    // ------------ payment service CRUD ------------

    public void payService(PaymentServiceDto dto) throws Exception {

        // get auth client
        Client client = AuthService.Auths.getClient();

        // get service
        Service service = serviceRepository
                .findById(dto.getServiceId())
                .orElseThrow(
                        () -> new RecordNotFoundException("service not found")
                );

        // TODO: transfer to agency credit card (need a method, ...)
        paymentService.transfer(
                service.getAgency(),
                dto.getAmount()
        );

        ServicePayment servicePayment =
                ServicePayment.builder()
                        .service(service)
                        .client(client)
                        .amount(dto.getAmount())
                        //.data(dto.getData())
                        .build();

        servicePaymentRepository.save(servicePayment);

    }

    public void deleteAgency(String id) {
        if (!AuthService.Auths.checkAdmin()) throw new PermissionException();
        agencyRepository.findById(id).orElseThrow(() -> new RecordNotFoundException("Agency not found"));
        agencyRepository.deleteById(id);
    }

}