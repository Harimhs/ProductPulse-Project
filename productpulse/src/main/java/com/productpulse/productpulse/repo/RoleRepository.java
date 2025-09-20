package com.productpulse.productpulse.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.productpulse.productpulse.model.Role;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByNameContainingIgnoreCase(String query);
}
