package com.example.eindopdracht_backend_ipmroved.repository;

import com.example.eindopdracht_backend_ipmroved.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByUsername(String username);
}
