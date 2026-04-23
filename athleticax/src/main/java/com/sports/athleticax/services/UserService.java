package com.sports.athleticax.services;

import com.sports.athleticax.repository.UserRepository;
import com.sports.athleticax.entity.User;
import com.sports.athleticax.dto.RegisterRequest;
import com.sports.athleticax.dto.LoginRequest;
import org.springframework.beans.factory.annotation.Autowired;

public interface UserService {
    User registerUser(RegisterRequest request);

    User authenticateUser(LoginRequest request); // Add this method
    @Autowired
    UserRepository userRepository = null;

    public default User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

}
