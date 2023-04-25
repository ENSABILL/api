package com.ensa.ecommerce_backend;

import com.ensa.ecommerce_backend.entity.UserEntity;
import com.ensa.ecommerce_backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import java.util.Arrays;

@SpringBootApplication
public class BackendApplication implements CommandLineRunner {
    @Autowired
    private UserRepository userRepository;
    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        final UserEntity user1 = new UserEntity(null,"mohcine");
        final UserEntity user2 = new UserEntity(null,"stuff");
        final UserEntity user3 = new UserEntity(null,"ghada");
        final UserEntity user4 = new UserEntity(null,"reda");
        final UserEntity user5 = new UserEntity(null,"hamza");
        final UserEntity user6 = new UserEntity(null,"mohammed");
        

        userRepository.saveAll(Arrays.asList(user1,user2,user3,user4,user5,user6));
    }
}
