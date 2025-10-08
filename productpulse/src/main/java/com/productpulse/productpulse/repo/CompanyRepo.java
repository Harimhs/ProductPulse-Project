package com.productpulse.productpulse.repo;

import com.productpulse.productpulse.model.Company;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompanyRepo extends JpaRepository<Company, Long> {

    @Cacheable(value = "companyCache", key = "#name")
    Optional<Company> findByName(String name);

    @Override
    @CacheEvict(value = "companyCache", key = "#entity.name")
    <S extends Company> S save(S entity);

}
