package com.project.security.jwt.data.entity;

import lombok.Data;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

//@Getter
@Data
@Document("users")
public class CustomUser {

    @Id
    private String id;
    private String username;
    private String password;
    private String country;

    private String role;


}

