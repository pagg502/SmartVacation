package com.backend.ecommercespringbootbackend.upgrades;

import org.springframework.security.crypto.bcrypt.BCrypt;

public class Hash {
    //Hashing and salting password to store securely
    public String hashPassword(String password) {
        String salt = BCrypt.gensalt();
        return BCrypt.hashpw(password, salt);
    }
}