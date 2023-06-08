package ma.ensa.ebanking.controllers;


import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.ClientDto;
import ma.ensa.ebanking.dto.auth.ClientRequest;
import ma.ensa.ebanking.request.UpdateClientRequest;
import ma.ensa.ebanking.request.UpdatePasswordRequest;
import ma.ensa.ebanking.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
@CrossOrigin
public class ClientController {

    private final ClientService clientService;

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public String createAccount(@RequestBody ClientRequest dto) throws Exception {
        clientService.createClient(dto, false);
        return null;
    }

    @PutMapping("/{username}/verify")
    @ResponseStatus(HttpStatus.OK)
    public String verifyAccount(@PathVariable String username) throws Exception {
        clientService.verifyAccount(username);
        return "account verified successfully";
    }

    @PostMapping("/add-account")
    @ResponseStatus(HttpStatus.CREATED)
    public String addAccount(@RequestBody ClientRequest dto) throws Exception {
        clientService.createClient(dto, true);
        return "account registered successfully";
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public ClientDto getClient(@PathVariable String username) throws Exception{
        return clientService
                .getClient(username);
    }

    @GetMapping("/balance")
    @ResponseStatus(HttpStatus.OK)
    public double checkBalance() throws Exception {
        return clientService.checkBalance();
    }

    @GetMapping("/all")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDto> getAllClients(){
        return clientService.getAllClients();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    public void deleteClients(@RequestBody String id){
        clientService.deleteClient(id);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.OK)
    public ClientDto updateClient(@RequestBody UpdateClientRequest updateClientRequest) throws Exception {
        return clientService.updateClient(updateClientRequest);
    }

    @PutMapping("/password")
    @ResponseStatus(HttpStatus.OK)
    public void updatePassword(@RequestBody UpdatePasswordRequest updatePasswordRequest) throws Exception {
        clientService.updatePassword(updatePasswordRequest);
    }

    @GetMapping("/nonVerifiedClients")
    @ResponseStatus(HttpStatus.OK)
    public List<ClientDto> getNonVerifiedClients(){
        return clientService.getNonVerifiedClients();
    }


}