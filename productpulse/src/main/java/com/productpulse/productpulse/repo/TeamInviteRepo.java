package com.productpulse.productpulse.repo;

import com.productpulse.productpulse.model.TeamInvite;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamInviteRepo extends JpaRepository <TeamInvite, Long>{

    Optional<TeamInvite> findByToken(String token);

}
