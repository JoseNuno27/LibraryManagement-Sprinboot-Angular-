package com.inn.library.restImpl;

import com.inn.library.constents.LibraryConstant;
import com.inn.library.rest.UserRest;
import com.inn.library.service.UserService;
import com.inn.library.utlis.LibraryUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class UserRestImpl implements UserRest {

    @Autowired
    UserService userService;


    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
      try{
          return userService.signUp(requestMap);
      }catch (Exception ex){
         ex.printStackTrace();
      }
      return LibraryUtils.getResponseEntity(LibraryConstant.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
