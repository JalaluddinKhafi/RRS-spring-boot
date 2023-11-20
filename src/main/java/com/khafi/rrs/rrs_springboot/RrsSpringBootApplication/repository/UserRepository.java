package com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.repository;

import com.khafi.rrs.rrs_springboot.RrsSpringBootApplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User,Long> {
    User findByUsername(String username);
    User findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByUsername(String username);
}
