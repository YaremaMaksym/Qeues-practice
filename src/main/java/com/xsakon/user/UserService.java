package com.xsakon.user;


import com.xsakon.exception.DuplicateResourseException;
import com.xsakon.exception.RecourceNotFoundException;
import com.xsakon.exception.RequestValidationException;
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
                .orElseThrow(() -> new RecourceNotFoundException(
                        "user with id [%s] not found".formatted(id)
                ));
    }

    public void addUser(UserRegistrationRequest userRegistrationRequest){
        String email = userRegistrationRequest.email();
        if(userDAO.existsUserWithEmail(email)){
            throw new DuplicateResourseException(
                    "Email already taken"
            );
        }

        User user = new User(
                userRegistrationRequest.name(),
                userRegistrationRequest.email(),
                userRegistrationRequest.age()
        );

        userDAO.insertUser(user);

    }

    public void deleteUserById(Integer id){
        if(!userDAO.existsUserWithId(id)){
            throw new RecourceNotFoundException(
                    "user with id [%s] not found".formatted(id)
            );
        }

        userDAO.deleteUserById(id);
    }

    public void updateUserById(Integer id, UserUpdateRequest updateRequest){



        User user = getUserById(id);

        boolean changes = false;


        if(!userDAO.existsUserWithId(id)){
            throw new RecourceNotFoundException(
                    "user with id [%s] not found".formatted(id)
            );
        }

        if(updateRequest.name() != null && updateRequest.name().equals(user.getName())){
            user.setName(updateRequest.name());
            changes = true;
        }


        if(updateRequest.email() != null && updateRequest.email().equals(user.getEmail())){
            if(userDAO.existsUserWithEmail(updateRequest.email())){
                throw new DuplicateResourseException(
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
