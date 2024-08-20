package com.inn.library.utlis;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;

public class LibraryUtils {

    private LibraryUtils() {

    }
    public static ResponseEntity<String> getResponseEntity(String responseMessage, HttpStatus httpstatus) {
        return new ResponseEntity<String>(Arrays.toString(new String[]{"\"message\":\"" + responseMessage + "\""}), httpstatus);
    }
}
