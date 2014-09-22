package com.github.joerodriguez.springbootexample.todo;

import com.github.joerodriguez.springbootexample.Application;
import org.fluentlenium.core.domain.FluentWebElement;
import org.junit.Before;
import org.junit.Test;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.transaction.annotation.Transactional;
import util.AbstractAcceptanceTest;

import static org.fluentlenium.core.filter.FilterConstructor.withText;

@ContextConfiguration(classes = {Application.class})
@Transactional
public class TodoAcceptanceTest extends AbstractAcceptanceTest {

    @Before
    public void init() {
        // Create account
        navigate("/");

        fill("input[name=name]").with("John Doe");
        fill("input[name=email]").with("jdoe@aol.com");
        fill("input[name=password]").with("password12");
        fill("input[name=passwordConfirmation]").with("password12");
        click("[type=submit]");

        findFirst(".alert-success", withText("Account successfully created"));

        await().until("form[action='/login']").isPresent();

        // login
        fill("input[name=username]").with("jdoe@aol.com");
        fill("input[name=password]").with("password12");
        click("[type=submit]");

        findFirst("span", withText("jdoe@aol.com"));

    }

    @Test
    public void create() {
        fill("input[name=name]").with("this is a test");
        click("[type=submit]");

        findFirst("span", withText("this is a test"));

    }

    @Test
    public void delete() {
        // create todo to delete
        fill("input[name=name]").with("delete this");
        click("[type=submit]");
        findFirst("span", withText("delete this"));

        click("input[type=submit][value=delete]");
        findFirst(".alert-success", withText("delete this successfully completed"));

    }

}
