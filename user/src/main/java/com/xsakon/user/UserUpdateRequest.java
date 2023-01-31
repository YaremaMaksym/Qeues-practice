package com.xsakon.user;

public record UserUpdateRequest(
        String name,
        String email,
        Integer age
) {
}
