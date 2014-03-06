package com.github.joerodriguez.springbootexample.registration;

import com.github.joerodriguez.springbootexample.config.validate.InfoValidator;
import com.github.joerodriguez.springbootexample.config.validate.Validateable;
import lombok.Data;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;

import javax.validation.constraints.Size;

@Data
public class RegistrationInfo implements Validateable<RegistrationInfo> {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotBlank(message = "Email must not be blank")
    @Email(message = "Email must be valid")
    private String email;

    @Size(min = 6, max = 50, message = "Password must be at least 6 characters long")
    private String password;

    private String passwordConfirmation;

    @Override
    public InfoValidator<RegistrationInfo> validator() {
        return new InfoValidator<RegistrationInfo>() {
            @Autowired
            UserRepository userRepository;

            @Override
            public void validate(RegistrationInfo target, Errors errors) {
                if (!target.getPassword().equals(target.getPasswordConfirmation())) {
                    errors.rejectValue("password", "", "Password must match password confirmation");
                }

                if (userRepository.findOneByEmail(target.getEmail()) != null) {
                    errors.rejectValue("email", "", "Email address is already taken");
                }
            }
        };
    }
}
