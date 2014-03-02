package com.github.joerodriguez.springbootexample.registration;

import com.github.joerodriguez.springbootexample.Application;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class RegistrationServiceTest {

    @InjectMocks
    RegistrationService registrationService;

    @Mock
    UserService userService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void register() {
        RegistrationInfo registrationInfo = new RegistrationInfo();
        registrationInfo.setName("John Doe");
        registrationInfo.setEmail("jdoe@aol.com");
        registrationInfo.setPassword("password 1");

        registrationService.register(registrationInfo);

        ArgumentCaptor<UserEntity> userEntityArgumentCaptor = ArgumentCaptor.forClass(UserEntity.class);
        verify(userService).create(userEntityArgumentCaptor.capture(), eq("password 1"));

        UserEntity user = userEntityArgumentCaptor.getValue();
        assertThat(user.getName(), equalTo("John Doe"));
        assertThat(user.getEmail(), equalTo("jdoe@aol.com"));
    }
}
