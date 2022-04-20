package com.chivapchichi.controller;

import com.chivapchichi.model.Members;
import com.chivapchichi.model.UserAndMember;
import com.chivapchichi.model.Users;
import com.chivapchichi.service.GetUriService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.client.RestTemplate;

import javax.validation.Valid;
import java.util.Map;

@Controller
public class RegistrationController {

    private final RestTemplate restTemplate;

    private final GetUriService getUriService;

    @Autowired
    public RegistrationController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
        this.getUriService = new GetUriService();
    }

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("users", new Users());
        model.addAttribute("members", new Members());

        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute @Valid Users users, BindingResult result, @ModelAttribute Members members, Model model) {
        if (result.hasErrors()) {
            return "registration";
        }

        UserAndMember u = new UserAndMember();
        u.setUsers(users);
        u.setMembers(members);

        Map<String, String> post = restTemplate.postForObject(getUriService.getUri("/registration/api"), u, Map.class);

        if (post != null) {
            String r = post.get("result");
            if ("user exists".equals(r)) {
                model.addAttribute("usernameError", "Пользователь с таким именем существует");
                return "registration";
            }
        }

        return "redirect:/tournament/registration";
    }
}
