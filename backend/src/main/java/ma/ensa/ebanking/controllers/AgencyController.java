package ma.ensa.ebanking.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgencyDTO;
import ma.ensa.ebanking.dto.PaymentServiceDto;
import ma.ensa.ebanking.dto.ServiceDto;
import ma.ensa.ebanking.services.AgencyService;
import ma.ensa.ebanking.services.ServicesService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agency")
@RequiredArgsConstructor
@CrossOrigin
public class AgencyController {

    private final AgencyService service;
    private final ServicesService servicesService;

    // get all agencies
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AgencyDTO> getAllAgencies() throws Exception {
        return service.getAllAgencies();
    }

    // get agency given by imm
    @GetMapping("/{imm}")
    @ResponseStatus(HttpStatus.OK)
    public AgencyDTO getAgency(@PathVariable String imm) throws Exception {
        return service.getAgency(imm);
    }

    // add an agency
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String addAgency(@RequestBody AgencyDTO dto) throws Exception{
        service.addAgency(dto);
        return "agency added successfully";
    }

    // add a service to the agency

    @PostMapping("/{imm}/service")
    public String addService(
            @PathVariable String imm,
            @RequestBody ServiceDto dto
    ) throws Exception{
        String sid = servicesService.addService(imm, dto);
        return String.format("service added successfully\nid: %s", sid);
    }

    @PutMapping ("/service/{id}/toggle")
    public String toggleService(
            @PathVariable String id
    ) throws Exception{
        servicesService.toggleService(id);
        return "service disabled successfully";
    }

    @PostMapping("/pay")
    @ResponseStatus(HttpStatus.OK)
    public String payService(@RequestBody PaymentServiceDto dto) throws Exception{
        service.payService(dto);
        return "transferred successfully";
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteAgency(@RequestBody String id){
        service.deleteAgency(id);
    }

}