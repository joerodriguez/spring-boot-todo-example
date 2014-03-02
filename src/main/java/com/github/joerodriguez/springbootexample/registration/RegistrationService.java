package com.github.joerodriguez.springbootexample.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    @Autowired UserService userService;

    public UserEntity register(RegistrationInfo registrationInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registrationInfo.getName());
        userEntity.setEmail(registrationInfo.getEmail());

        return userService.create(userEntity, registrationInfo.getPassword());
    }
}
