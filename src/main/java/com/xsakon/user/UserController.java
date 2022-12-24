package com.xsakon.user;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final UserService userService;


    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @GetMapping("{userId}")
    public User getUserById(@PathVariable("userId") Integer id){
        return  userService.getUserById(id);
    }

    @PostMapping
    public void registerUser(@RequestBody UserRegistrationRequest registrationRequest){
        userService.addUser(registrationRequest);
    }

    @DeleteMapping("{userId}")
    public void deleteUser(@PathVariable("userId") Integer id){
        userService.deleteUserById(id);
    }

    @PutMapping({"userId"})
    public void updateUser(@PathVariable("userId") Integer id,
                           UserUpdateRequest updateRequest){
        userService.updateUserById(id, updateRequest);
    }
}
