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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {Application.class})
@WebAppConfiguration
public class RegistrationControllerTest {

    private MockMvc mockMvc;

    @InjectMocks
    RegistrationController registrationController;

    @Mock
    RegistrationService registrationService;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(registrationController).build();
    }

    @Test
    public void register_withInvalidData() throws Exception {
        mockMvc.perform(
                post("/register")
                        .param("name", "")
                        .param("email", "john at aol dot com")
                        .param("password", "john1234")
                        .param("passwordConfirmation", "john123")
        )
                .andExpect(model().attribute("registrationInfo", hasProperty("password", equalTo("john1234"))))
                .andExpect(view().name("registration/form"));

        verify(registrationService, never()).register(any(RegistrationInfo.class));
    }

    @Test
    public void register_withValidData() throws Exception {
        mockMvc.perform(
                post("/register")
                        .param("name", "John Doe")
                        .param("email", "john.doe@aol.com")
                        .param("password", "john1234")
                        .param("passwordConfirmation", "john1234")
        )
                .andExpect(redirectedUrl("/login"))
                .andExpect(flash().attribute("alertSuccess", "Account successfully created"));

        ArgumentCaptor<RegistrationInfo> registrationInfoArgumentCaptor = ArgumentCaptor.forClass(RegistrationInfo.class);
        verify(registrationService).register(registrationInfoArgumentCaptor.capture());

        RegistrationInfo registrationInfo = registrationInfoArgumentCaptor.getValue();
        assertThat(registrationInfo.getName(), equalTo("John Doe"));
        assertThat(registrationInfo.getEmail(), equalTo("john.doe@aol.com"));
        assertThat(registrationInfo.getPassword(), equalTo("john1234"));
    }

}
