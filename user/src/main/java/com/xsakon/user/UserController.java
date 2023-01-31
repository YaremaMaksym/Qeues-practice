package com.xsakon.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        log.info("fetching all users");
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public User getUserById(@PathVariable("userId") Integer id){
        log.info("fetching user with id {}", id);
        return  userService.getUserById(id);
    }

    @PostMapping
    public void registerUser(@RequestBody UserRegistrationRequest registrationRequest){
        log.info("new user registration {}", registrationRequest);
        userService.addUser(registrationRequest);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Integer id){
        log.info("deleting user with id {}", id);
        userService.deleteUserById(id);
    }

    @PutMapping("{userId}")
    public void updateUser(@PathVariable("userId") Integer id,
                           @RequestBody UserUpdateRequest updateRequest){
        log.info("updating user with id {}, new data: {}", id, updateRequest);
        userService.updateUserById(id, updateRequest);
    }
}
