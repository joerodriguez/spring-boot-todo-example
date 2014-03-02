package com.github.joerodriguez.springbootexample.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public UserEntity create(UserEntity userEntity, String password) {
        userEntity.setPasswordSalt(UUID.randomUUID().toString());
        userEntity.setPasswordHash(passwordEncoder.encode(password + userEntity.getPasswordSalt()));
        return userRepository.save(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findOneByEmail(username);
    }

    public UserEntity findAndAuthenticateUser(String email, String providedPassword) {
        UserEntity user = userRepository.findOneByEmail(email);
        if (user == null) {
            return null;
        }

        String saltedPassword = providedPassword + user.getPasswordSalt();
        if (passwordEncoder.matches(saltedPassword, user.getPasswordHash())) {
            return user;
        }

        return null;
    }
}
