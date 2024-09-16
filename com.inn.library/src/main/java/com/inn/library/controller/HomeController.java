package com.inn.library.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @GetMapping("/")
    public String getHome() {
        return "Homepage";
    }

    @GetMapping("/library")
    public String Library() {
        return "Library";
    }

    @GetMapping("/admin")
    public String admin() {
        return "Admin";
    }
    @GetMapping("/client/home")
    public String getClientHome() {
        return "Client Home";
    }
}
