package com.github.joerodriguez.springbootexample.config.validate;

import org.springframework.validation.Validator;

public interface Validatable {
    public InfoValidator validator();
}
