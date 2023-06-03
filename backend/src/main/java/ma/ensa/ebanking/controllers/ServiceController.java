package ma.ensa.ebanking.controllers;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.dto.ServiceDTO;
import ma.ensa.ebanking.enums.ServiceType;
import ma.ensa.ebanking.request.AddDonationRequest;
import ma.ensa.ebanking.request.AddFactureRequest;
import ma.ensa.ebanking.request.AddRechargeRequest;
import ma.ensa.ebanking.request.PayBillRequest;
import ma.ensa.ebanking.services.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/creanciers")
@AllArgsConstructor
public class ServiceController {

    private ServicesService servicesService;

    @GetMapping
    public List<ServiceDTO> findServiceByIdOrNameAndType(@RequestParam("keyword") String keyword, @RequestParam("type") ServiceType type){
        return servicesService.findServiceByIdOrNameAndType(keyword, type);
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/paidBills")
    public List<OperationDto> getClientPaidBills(){
        return servicesService.getClientPaidBills();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/unpaidBills")
    public List<OperationDto> getClientUnpaidBills(){
        return servicesService.getClientUnpaidBills();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/donation")
    public OperationDto addDonation(@RequestBody AddDonationRequest donationRequest){
        return servicesService.addDonation(donationRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/recharge")
    public OperationDto addRecharge(@RequestBody AddRechargeRequest rechargeRequest){
        return servicesService.addRecharge((rechargeRequest));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bill")
    public OperationDto addBill(@RequestBody AddFactureRequest addFactureRequest){
        return servicesService.addBill(addFactureRequest);
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/bill")
    public OperationDto payBill(PayBillRequest payBillRequest){
        return servicesService.payBill(payBillRequest);
    }

}
