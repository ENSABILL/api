package ma.ensa.ebanking.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.auth.*;
import ma.ensa.ebanking.services.AgentService;
import ma.ensa.ebanking.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(
            @RequestBody AuthRequest request
    ) throws Exception{
        return service.authenticate(request);
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public String resetPassword(@RequestBody ResetPasswordDto dto) throws Exception{
        service.resetPassword(
                dto.getToken(),
                dto.getNewPassword()
        );
        return "password reset successfully";
    }

    @PostMapping("/verify")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public AuthResponse verify(@RequestBody VerifyCodeDto dto) throws Exception {
        return service.verifyCode(
                dto.getToken(),
                dto.getCode()
        );
    }


}
