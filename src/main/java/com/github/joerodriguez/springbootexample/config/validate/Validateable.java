package com.github.joerodriguez.springbootexample.config.validate;

public interface Validateable<This> {
    public InfoValidator<This> validator();
}
