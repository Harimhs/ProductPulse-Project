package com.productpulse.productpulse.repo;

import com.productpulse.productpulse.model.Company;
import com.productpulse.productpulse.model.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepo extends JpaRepository<Company, Long> {
    Optional<Company> findByName(String name);
}
