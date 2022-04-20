package com.chivapchichi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class Home {

    @GetMapping("/")
    public String homepage() {
        return "redirect:/tournament/registration";
    }
}
