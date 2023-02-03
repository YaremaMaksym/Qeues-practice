package com.xsakon.user;

import java.util.List;
import java.util.Optional;

public interface UserDao {
    List<User> selectAllUsers();
    Optional<User> selectUserById(Integer id);
    boolean existsUserWithEmail(String email);
    boolean existsUserWithId(Integer id);
    void insertUser(User user);
    void insertUserAndFlush(User user);
    void deleteUserById(Integer id);

    void updateUser(User user);
}

