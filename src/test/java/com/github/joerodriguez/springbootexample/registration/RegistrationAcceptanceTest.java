package com.github.joerodriguez.springbootexample.registration;

import com.github.joerodriguez.springbootexample.Application;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import util.AbstractAcceptanceTest;

import static org.fluentlenium.core.filter.FilterConstructor.withText;

@ContextConfiguration(classes = {Application.class})
@Transactional
public class RegistrationAcceptanceTest extends AbstractAcceptanceTest {

    @Test
    public void register() {
        // Create account
        navigate("/");

        fill("input[name=name]").with("John Doe");
        fill("input[name=email]").with("jdoe@aol.com");
        fill("input[name=password]").with("password12");

        click("[type=submit]");

        findFirst("p", withText("Passwords must match"));

        fill("input[name=passwordConfirmation]").with("password12");

        click("[type=submit]");

        findFirst(".alert-success", withText("Account successfully created"));

        // Login
        fill("input[name=username]").with("jdoe@aol.com");
        fill("input[name=password]").with("password12");

        click("[type=submit]");

        findFirst("span", withText("jdoe@aol.com"));
    }

}
