package com.github.joerodriguez.springbootexample.config.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class GlobalValidator implements Validator {
    @Autowired
    AutowireCapableBeanFactory autowireCapableBeanFactory;

    @Override
    public boolean supports(Class<?> clazz) {
        return true;
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (target instanceof Validatable) {
            InfoValidator validator = ((Validatable) target).validator();
            autowireCapableBeanFactory.autowireBean(validator);

            validator.validate(target, errors);
        }
    }
}
