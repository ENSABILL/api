package ma.ensa.ebanking;


import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.models.user.Admin;
import ma.ensa.ebanking.models.user.User;
import ma.ensa.ebanking.repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@RequiredArgsConstructor
public class MainApplication implements CommandLineRunner {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {

        if(
                userRepository.existsByUsername("badusername")
        ) return;

        String encodedPassword =
                passwordEncoder.encode("admin123");

        User admin = Admin.builder()
                .fullName("bad admin")
                .email("admin@badbank.com")
                .password(encodedPassword)
                .username("badusername")
                .phoneNumber("+212704261627")
                .build();

        userRepository.save(admin);

    }
}
