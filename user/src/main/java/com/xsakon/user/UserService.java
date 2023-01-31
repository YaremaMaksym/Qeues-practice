package com.xsakon.user;


import com.xsakon.user.exception.DuplicateResourceException;
import com.xsakon.user.exception.RequestValidationException;
import com.xsakon.user.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    private final UserDao userDAO;

    public UserService(@Qualifier("jpa") UserDao userDAO) {
        this.userDAO = userDAO;
    }

    public List<User> getAllUsers(){
        return userDAO.selectAllUsers();
    }

    public User getUserById(Integer id){
        return userDAO.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "user with id [%s] not found".formatted(id)
                ));
    }

    public void addUser(UserRegistrationRequest registrationRequest){
        String email = registrationRequest.email();
        if(userDAO.existsUserWithEmail(email)){
            throw new DuplicateResourceException(
                    "Email already taken"
            );
        }

        User user = new User(
                registrationRequest.name(),
                registrationRequest.email(),
                registrationRequest.age()
        );

        userDAO.insertUser(user);

    }

    public void deleteUserById(Integer id){
        if(!userDAO.existsUserWithId(id)){
            throw new ResourceNotFoundException(
                    "user with id [%s] not found".formatted(id)
            );
        }

        userDAO.deleteUserById(id);
    }

    public void updateUserById(Integer id, UserUpdateRequest updateRequest){
        User user = userDAO.selectUserById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "user with id [%s] not found".formatted(id)
                ));

        boolean changes = false;

        if(updateRequest.name() != null && !updateRequest.name().equals(user.getName())){
            user.setName(updateRequest.name());
            changes = true;
        }

        if(updateRequest.age() != null && !updateRequest.age().equals(user.getAge())){
            user.setAge(updateRequest.age());
            changes = true;
        }

        if(updateRequest.email() != null && !updateRequest.email().equals(user.getEmail())){
            if(userDAO.existsUserWithEmail(updateRequest.email())){
                throw new DuplicateResourceException(
                        "email already taken"
                );
            }
            user.setEmail(updateRequest.email());
            changes = true;
        }
        
        if(!changes){
            throw new RequestValidationException("no data changes found");
        }

        userDAO.updateUser(user);
    }
}
