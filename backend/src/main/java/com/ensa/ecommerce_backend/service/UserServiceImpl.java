package com.ensa.ecommerce_backend.service;

import com.ensa.ecommerce_backend.entity.UserEntity;
import com.ensa.ecommerce_backend.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService{
    private UserRepository userRepository;
    @Override
    public List<UserEntity> findAll() {
        return userRepository.findAll();
    }
}
