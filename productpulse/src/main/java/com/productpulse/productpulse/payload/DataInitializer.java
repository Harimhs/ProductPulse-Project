package com.productpulse.productpulse.payload;

import com.productpulse.productpulse.model.Role;
import com.productpulse.productpulse.repo.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role("CEO"));
            roleRepository.save(new Role("CFO"));
            roleRepository.save(new Role("CTO"));
            roleRepository.save(new Role("Business Analyst"));
            roleRepository.save(new Role("Product Manager"));
            roleRepository.save(new Role("QA Engineer"));
            roleRepository.save(new Role("Project Manager"));
            roleRepository.save(new Role("Team Lead"));
            roleRepository.save(new Role("Intern"));
            roleRepository.save(new Role("Software Developer"));
            roleRepository.save(new Role("Tester"));
            roleRepository.save(new Role("Manager Director"));
        }
    }
}
