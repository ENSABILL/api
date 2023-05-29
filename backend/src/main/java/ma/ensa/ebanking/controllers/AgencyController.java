package ma.ensa.ebanking.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgencyDTO;
import ma.ensa.ebanking.dto.ServiceDTO;
import ma.ensa.ebanking.services.AgencyService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agency")
@RequiredArgsConstructor
public class AgencyController {

    private final AgencyService service;

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

    @PostMapping("/{imm}")
    public String addService(
            @PathVariable String imm,
            @RequestBody ServiceDTO dto
    ) throws Exception{
        String sid = service.addService(imm, dto);
        return String.format("service added successfully\nid: %s", sid);
    }

    @PutMapping ("/service/{id}/toggle")
    public String toggleService(
            @PathVariable String id
    ) throws Exception{
        service.toggleService(id);
        return "service disabled successfully";
    }

}