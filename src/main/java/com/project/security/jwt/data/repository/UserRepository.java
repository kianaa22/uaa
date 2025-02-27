package com.project.security.jwt.data.repository;

import com.project.security.jwt.data.entity.CustomUser;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<CustomUser, String> {
    Optional<CustomUser> findByUsername(String username);
}
