package com.inn.library.controller;

import com.inn.library.models.AppUser;
import com.inn.library.models.LoginDto;
import com.inn.library.models.RegisterDto;
import com.inn.library.repositories.AppUserRepository;
import com.inn.library.services.AppUserService;
import com.nimbusds.jose.jwk.source.ImmutableSecret;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@RestController
@RequestMapping("/account")
public class AccountController {
    private final AppUserService userService;
    @Value("${security.jwt.secret-key}")
    private String jwtSecretKey;

    @Value("${security.jwt.issuer}")
    private String jwtIssuer;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public AccountController(AppUserService userService) {
        this.userService = userService;
    }

    @GetMapping("/profile")
    public ResponseEntity<Object> profile(Authentication auth) {

        var response = new HashMap<String, Object>();
        response.put("Username", auth.getName());
        response.put("Authorities", auth.getAuthorities());

        var appUser = appUserRepository.findByUsername(auth.getName());
        response.put("User", appUser);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/all")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        List<AppUser> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    // API to update a user by ID
    @PutMapping("/update/{id}")
    public ResponseEntity<AppUser> updateUser(@PathVariable Long id, @RequestBody AppUser updatedUser) {
        AppUser user = userService.updateUser(id, updatedUser);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<Object> register(
            @Valid @RequestBody RegisterDto registerDto,
            BindingResult result) {

        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();
            for (org.springframework.validation.ObjectError objectError : errorsList) {
                var error = (FieldError) objectError;
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }
            return ResponseEntity.badRequest().body(errorsMap);
        }

        AppUser existingUserByUsername = appUserRepository.findByUsername(registerDto.getUsername());
        if (existingUserByUsername != null) {
            return ResponseEntity.badRequest().body("Username already exists");
        }

        AppUser existingUserByEmail = appUserRepository.findByEmail(registerDto.getEmail());
        if (existingUserByEmail != null) {
            return ResponseEntity.badRequest().body("Email already exists");
        }

        var bCryptEncoder = new BCryptPasswordEncoder();
        AppUser appUser = new AppUser();
        appUser.setFirstName(registerDto.getFirstName());
        appUser.setLastName(registerDto.getLastName());
        appUser.setUsername(registerDto.getUsername());
        appUser.setEmail(registerDto.getEmail());
        appUser.setPassword(bCryptEncoder.encode(registerDto.getPassword()));
        appUser.setCreatedAt(new Date());
        appUserRepository.save(appUser);

        String jwtToken = createJwtToken(appUser);
        var response = new HashMap<String, String>();
        response.put("token", jwtToken);
        response.put("user", appUser.getUsername());
        return ResponseEntity.ok(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Object> login(
            @Valid @RequestBody LoginDto loginDto,
            BindingResult result) {
        if (result.hasErrors()) {
            var errorsList = result.getAllErrors();
            var errorsMap = new HashMap<String, String>();
            for (org.springframework.validation.ObjectError objectError : errorsList) {
                var error = (FieldError) objectError;
                errorsMap.put(error.getField(), error.getDefaultMessage());
            }

            return ResponseEntity.badRequest().body(errorsMap);
        }
        try {
            authenticationManager.authenticate(
                    new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(
                            loginDto.getUsername(),
                            loginDto.getPassword()
                    )
            );
            AppUser appUser = appUserRepository.findByUsername(loginDto.getUsername());
            String jwtToken = createJwtToken(appUser);
            var response = new HashMap<String, String>();
            response.put("token", jwtToken);
            response.put("user", String.valueOf(appUser));
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            System.out.println("There is an exception: ");
            e.printStackTrace();
        }
        return ResponseEntity.badRequest().body("Bad Username or Password");
    }

    private String createJwtToken(AppUser appUser) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(jwtIssuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(24 * 3600))
                .subject(appUser.getUsername())
                .claim("role", appUser.getRole())
                .build();

        var encoder = new NimbusJwtEncoder(
                new ImmutableSecret<>(jwtSecretKey.getBytes()));
        var params = JwtEncoderParameters.from(
                JwsHeader.with(MacAlgorithm.HS256).build(), claims);

        return encoder.encode(params).getTokenValue();
    }

}