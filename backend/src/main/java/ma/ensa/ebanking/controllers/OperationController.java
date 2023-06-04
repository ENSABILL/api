package ma.ensa.ebanking.controllers;

import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.request.AddDonationRequest;
import ma.ensa.ebanking.request.AddFactureRequest;
import ma.ensa.ebanking.request.AddRechargeRequest;
import ma.ensa.ebanking.request.PayBillRequest;
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
    public List<OperationDto> getClientUnpaidBills() {
        return operationService.getClientUnpaidBills();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/pay-donation")
    public OperationDto payDonation(@RequestBody AddDonationRequest donationRequest) {
        return paymentService.payDonation(donationRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/pay-recharge")
    public OperationDto payRecharge(@RequestBody AddRechargeRequest rechargeRequest) {
        return paymentService.payRecharge((rechargeRequest));
    }

    @ResponseStatus(HttpStatus.OK)
    @PostMapping("/pay-bill")
    public OperationDto payBill(PayBillRequest payBillRequest) {
        return paymentService.payBill(payBillRequest);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/bill")
    public OperationDto addBill(@RequestBody AddFactureRequest addFactureRequest) {
        return operationService.addBill(addFactureRequest);
    }

}
