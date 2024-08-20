package com.inn.library.servicelmpl;

import com.inn.library.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class UserServiceImpl implements UserService {


    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {

        return null;
    }

    private boolean validateSignUp(Map<String, String> requestMap) {
        return requestMap.containsKey("name") && requestMap.containsKey("contactNumber") && requestMap.containsKey("password") && requestMap.containsKey("email");
    }
}
