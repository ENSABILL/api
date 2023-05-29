package ma.ensa.ebanking.controllers;


import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.auth.ClientRequest;
import ma.ensa.ebanking.services.ClientService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/client")
@RequiredArgsConstructor
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

    @GetMapping
    @ResponseStatus
    public ClientDto getClient(@RequestBody ClientDto dto){
        return null; // TODO: to complete
    }

}