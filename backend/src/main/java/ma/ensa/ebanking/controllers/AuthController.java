package ma.ensa.ebanking.controllers;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.dto.auth.AuthRequest;
import ma.ensa.ebanking.dto.auth.AuthResponse;
import ma.ensa.ebanking.dto.auth.ResetPasswordDto;
import ma.ensa.ebanking.dto.auth.VerifyCodeDto;
import ma.ensa.ebanking.services.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService service;

    @PostMapping("/login")
    @ResponseStatus(HttpStatus.OK)
    public AuthResponse login(@RequestBody AuthRequest request){
        return service.authenticate(request);
    }

    @PostMapping("/get-otp")
    @ResponseStatus(HttpStatus.CREATED)
    public String resetPassword() {
        return service.sendVerificationCode();
    }

    @PostMapping("/reset-password")
    @ResponseStatus(HttpStatus.OK)
    public String resetPassword(@RequestBody ResetPasswordDto dto) throws Exception {
        service.resetPassword(dto);
        return "password reset successfully";
    }

    @PostMapping("/verify-otp")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String verify(@RequestBody VerifyCodeDto dto) throws Exception {
        // TODO: ... into DTO
        service.verifyCode(
                dto.getToken(),
                dto.getCode()
        );
        return "the code has been verified successfully";
    }

}
