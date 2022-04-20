package com.chivapchichi.controller;

import com.chivapchichi.model.AuthenticationRequestDTO;
import com.chivapchichi.service.api.LoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.HttpClientErrorException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class LoginController {

    private final LoginService loginService;

    @Autowired
    public LoginController(LoginService loginService) {
        this.loginService = loginService;
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PostMapping("/login")
    public String makeLogin(Model model, @ModelAttribute AuthenticationRequestDTO request, HttpServletResponse response) {
        try {
            loginService.login(request, response);
        } catch (HttpClientErrorException.Forbidden e) {
            model.addAttribute("loginError", e.getResponseBodyAsString());
            return "login";
        }
        return "redirect:/tournament/registration/";
    }

    @PostMapping("/makelogout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        loginService.logout(request, response);
        return "redirect:/tournament/registration/";
    }
}
