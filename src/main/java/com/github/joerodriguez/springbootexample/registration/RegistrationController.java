package com.github.joerodriguez.springbootexample.registration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    RegistrationService registrationService;

    @RequestMapping("/")
    public String form(Model model) {
        model.addAttribute("registrationInfo", new RegistrationInfo());
        return "registration/form";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(
            @Valid RegistrationInfo registrationInfo,
            BindingResult bindingResult,
            Model model,
            RedirectAttributes redirectAttributes
    ) {

        if (!bindingResult.hasErrors()) {
            try {
                registrationService.register(registrationInfo);
                redirectAttributes.addFlashAttribute("alertSuccess", "Account successfully created");
                return "redirect:/login";
            } catch (DataIntegrityViolationException e) {
                bindingResult.rejectValue("email", "EmailTaken", "Email already taken");
            }
        }

        model.addAttribute("registrationInfo", registrationInfo);
        return "registration/form";
    }
}
