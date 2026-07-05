package com.sports.athleticax.services;

import com.sports.athleticax.dto.LoginRequest;
import com.sports.athleticax.dto.RegisterRequest;
import com.sports.athleticax.entity.User;

public interface UserService {

    User registerUser(RegisterRequest request);

    User authenticateUser(LoginRequest request);

    User findUserByEmail(String email);

    boolean emailExists(String email);

    void updatePassword(String email, String newPassword); 
}
