package com.shashi.service.user;

import com.shashi.dataaccess.entity.UserEntity;

public interface UserService {
    UserEntity registerUser(UserEntity user);
    UserEntity findUserByEmail(String email);
    boolean isValidUser(String email, String password);
}
