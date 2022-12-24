package com.xsakon.user;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository("jpa")
public class UserJPADataAcessService implements UserDao {

    private final UserRepository userRepository;

    public UserJPADataAcessService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    @Override
    public List<User> selectAllUsers(){
        return userRepository.findAll();
    }

    @Override
    public Optional<User> selectUserById(Integer id){
        return userRepository.findById(id);
    }

    @Override
    public boolean existsUserWithEmail(String email){
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public boolean existsUserWithId(Integer id){
        return userRepository.existsUserById(id);
    }

    @Override
    public void insertUser(User user){
        userRepository.save(user);
    }

    @Override
    public void deleteUserById(Integer id) {
        userRepository.deleteById(id);
    }



    @Override
    public void updateUser(User user){
        userRepository.save(user);
    }


}
