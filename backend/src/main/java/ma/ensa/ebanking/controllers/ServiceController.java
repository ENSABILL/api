package ma.ensa.ebanking.controllers;

import lombok.AllArgsConstructor;
import ma.ensa.ebanking.dto.OperationDto;
import ma.ensa.ebanking.dto.ServiceDto;
import ma.ensa.ebanking.enums.ServiceType;
import ma.ensa.ebanking.request.AddDonationRequest;
import ma.ensa.ebanking.request.AddFactureRequest;
import ma.ensa.ebanking.request.AddRechargeRequest;
import ma.ensa.ebanking.request.PayBillRequest;
import ma.ensa.ebanking.services.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service")
@AllArgsConstructor
public class ServiceController {

    private ServicesService servicesService;
    

    @GetMapping
    public List<ServiceDto> getAllServices(
            @RequestParam(value = "keyword", required = false) String keyword,
            @RequestParam(value = "type", required = false) ServiceType type
    ){
        return servicesService.getAllServices(keyword, type);
    }

    @PostMapping("/{id}")
    public ServiceDto getService(@PathVariable("id") String serviceId){
        return servicesService.getService(serviceId);
    }



}
