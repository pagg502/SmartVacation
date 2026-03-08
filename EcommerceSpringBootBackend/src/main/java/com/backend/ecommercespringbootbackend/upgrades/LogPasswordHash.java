package com.backend.ecommercespringbootbackend.upgrades;

public class LogPasswordHash extends Hash {
    //Implements inheritance and override method
    @Override
    public String hashPassword(String password) {
        return super.hashPassword(password);
    }
}
