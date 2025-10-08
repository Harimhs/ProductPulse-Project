package com.productpulse.productpulse.controller;

import com.productpulse.productpulse.model.Role;
import com.productpulse.productpulse.repo.RoleRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleController {
    private final RoleRepository roleRepository;

    public RoleController(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Cacheable(value = "rolesCache", key = "#query")
    @GetMapping("/search")
    public List<Role> searchRoles(@RequestParam("query") String query) {
        System.out.println("CACHE MISS: querying database for " + query);
        return roleRepository.findByNameContainingIgnoreCase(query);
    }
}
