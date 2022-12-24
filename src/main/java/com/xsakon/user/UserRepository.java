package com.xsakon.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserById(Integer id);
    boolean existsUserByEmail(String email);

}

