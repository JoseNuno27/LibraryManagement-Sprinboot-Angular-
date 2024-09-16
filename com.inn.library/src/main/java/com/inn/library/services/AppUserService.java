package com.inn.library.services;

import com.inn.library.models.AppUser;
import com.inn.library.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository repo;

    @Autowired
    public AppUserService(AppUserRepository repo) {
        this.repo = repo;
    }

    public List<AppUser> getAllUsers() {
        return repo.findAll();  // Retrieves all users from the database
    }

    public AppUser updateUser(Long id, AppUser updatedUser) {
        return repo.findById(Math.toIntExact(id)).map(user -> {
            user.setUsername(updatedUser.getUsername());
            user.setPassword(updatedUser.getPassword()); // Handle password encoding if needed
            // Set other fields as needed
            return repo.save(user);
        }).orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + id));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = repo.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}
