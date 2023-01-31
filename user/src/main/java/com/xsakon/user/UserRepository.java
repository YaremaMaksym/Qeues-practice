package com.xsakon.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsUserById(Integer id);
    boolean existsUserByEmail(String email);

}

