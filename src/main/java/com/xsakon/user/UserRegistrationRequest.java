package com.xsakon.user;

public record UserRegistrationRequest(
        String name,
        String email,
        Integer age
) {
}
