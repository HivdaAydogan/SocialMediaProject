package com.socialmedia.repository;

import com.socialmedia.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IAuthRepository extends JpaRepository<Auth,Long> {
    Boolean existsByUsernameAndPassword(String username, String password);
    Optional<Auth> findOptionalByEmailAndPassword(String email, String password);
    Optional<Auth> findOptionalByEmail(String email);
}
