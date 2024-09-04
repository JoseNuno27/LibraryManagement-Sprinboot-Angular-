package com.inn.library.service;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Map;

public interface UserService {

    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    ResponseEntity<String> signUp(Map<String, String> requestMap);
}
