package ma.ensa.ebanking.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.AgentDto;
import ma.ensa.ebanking.dto.auth.AgentRequest;
import ma.ensa.ebanking.services.AgentService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public String createAgent(@RequestBody AgentRequest request) throws Exception{
        service.createAgent(request);
        return "agent created successfully";
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<AgentDto> getAllAgents(){
        return service.getAllAgents();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteAgent(@PathVariable String id){
        service.deleteAgent(id);
    }

}
