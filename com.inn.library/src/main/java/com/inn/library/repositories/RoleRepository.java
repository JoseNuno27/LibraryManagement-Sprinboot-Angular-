package com.inn.library.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.inn.library.models.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByName(String name);
}
