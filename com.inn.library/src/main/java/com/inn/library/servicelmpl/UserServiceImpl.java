package com.inn.library.servicelmpl;

import com.inn.library.POJO.User;
import com.inn.library.dao.UserDao;
import com.inn.library.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userDao.findByEmailId(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole()) // Optionally add roles if you need them
                .build();
    }

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        String email = requestMap.get("email");
        String password = requestMap.get("password");
        String name = requestMap.get("name");
        String contactNumber = requestMap.get("contactNumber");
        String role = Optional.ofNullable(requestMap.get("role")).orElse("USER"); // Default to USER if not provided

        if (email == null || password == null || name == null) {
            return new ResponseEntity<>("Required fields are missing", HttpStatus.BAD_REQUEST);
        }

        // Check if user already exists
        if (userDao.findByEmailId(email) != null) {
            return new ResponseEntity<>("User with this email already exists", HttpStatus.BAD_REQUEST);
        }

        // Create new user
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Encrypt the password
        user.setContactNumber(contactNumber);
        user.setRole(role);
        user.setStatus("ACTIVE"); // Default status

        // Save user to the database
        userDao.save(user);

        return new ResponseEntity<>("User registered successfully", HttpStatus.CREATED);
    }
}
