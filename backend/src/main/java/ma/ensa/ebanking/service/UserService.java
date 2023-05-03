package ma.ensa.ebanking.service;

import lombok.RequiredArgsConstructor;
import ma.ensa.ebanking.models.User;
import ma.ensa.ebanking.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private UserRepository userRepository;

    public List<User> findAll() {
        return userRepository.findAll();
    }
}
