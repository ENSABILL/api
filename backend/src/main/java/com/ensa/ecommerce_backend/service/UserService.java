package com.ensa.ecommerce_backend.service;

import com.ensa.ecommerce_backend.entity.UserEntity;

import java.util.List;

public interface UserService {
    List<UserEntity> findAll();
}
