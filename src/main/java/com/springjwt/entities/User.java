package com.springjwt.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("users")
@Data
public class User {


    private String name;
    @Id
    private String email;

    private String password;

}
