package com.github.joerodriguez.springbootexample.config.validate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.InitBinder;

@ControllerAdvice
public class Validations {

    @Autowired GlobalValidator globalValidator;

    @InitBinder
    public void bind(WebDataBinder binder) {
        binder.addValidators(globalValidator);
    }
}
