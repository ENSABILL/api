package ma.ensa.ebanking.controllers;


import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.LevelDto;
import ma.ensa.ebanking.dto.TransferDto;
import ma.ensa.ebanking.services.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/account/")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService service;

    @PutMapping("/upgrade")
    @ResponseStatus(HttpStatus.OK)
    public String upgradeAccount(@RequestBody LevelDto dto) throws Exception{
        service.upgradeAccount(dto.getLevel());
        return "account upgraded successfully";
    }

    @PutMapping("/feed")
    public String feed(@RequestBody TransferDto dto) throws Exception{
        service.feed(dto);
        return null;
    }
}