package ma.ensa.ebanking.controllers;

import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.request.AddFactureRequest;
import ma.ensa.ebanking.request.PayBillsRequest;
import ma.ensa.ebanking.request.PayDonationRequest;
import ma.ensa.ebanking.request.PayRechargeRequest;
import ma.ensa.ebanking.services.OperationService;
import ma.ensa.ebanking.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/operation")
@AllArgsConstructor
public class OperationController {

    private OperationService operationService;
    private PaymentService paymentService;


    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<OperationDto> getAllClientsBills() throws Exception {
        return operationService.getAllClientsBills();
    }


    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/paidBills")
    public List<OperationDto> getClientPaidBills() {
        return operationService.getClientPaidBills();
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/unpaidBills")
    public List<OperationDto> getClientUnpaidBills(@RequestParam(name = "serviceId", required = false) String serviceId) {
        return operationService.getClientUnpaidBills(serviceId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/pay-donation")
    public OperationDto payDonation(@RequestBody PayDonationRequest donationRequest) {
        return paymentService.payDonation(donationRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/pay-recharge")
    public OperationDto payRecharge(@RequestBody PayRechargeRequest rechargeRequest) {
        return paymentService.payRecharge((rechargeRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/pay-bills")
    public List<OperationDto> payBills(@RequestBody PayBillsRequest payBillsRequest) {
        return paymentService.payBills(payBillsRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bill")
    public OperationDto addBill(@RequestBody AddFactureRequest addFactureRequest) {
        return operationService.addBill(addFactureRequest);
    }

}
