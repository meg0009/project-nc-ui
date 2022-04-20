package com.chivapchichi.controller.admin;

import com.chivapchichi.model.Members;
import com.chivapchichi.model.Users;
import com.chivapchichi.service.api.admin.UserMemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin-panel")
public class UsersMembersController {

    private final UserMemService userMemService;

    @Autowired
    public UsersMembersController(UserMemService userMemService) {
        this.userMemService = userMemService;
    }

    @GetMapping("/get-user")
    public String deleteUser(Model model, HttpServletRequest request) {
        model.addAttribute("users", userMemService.getUsers(request));
        return "admin-panel/get-user";
    }

    @GetMapping("/change-user/{username}")
    public String changeUserId(@PathVariable("username") String username, Model model, HttpServletRequest request) {
        ResponseEntity<Users> user = userMemService.getUserByUserName(username, request);
        if (user.getBody() != null) {
            model.addAttribute("user", user.getBody());
            ResponseEntity<Members> members = userMemService.getMemberByUserName(username, request);
            if (members.getBody() != null) {
                model.addAttribute("member", members.getBody());
            } else {
                model.addAttribute("member", new Members());
            }
            return "admin-panel/change-user-id";
        }
        return "admin-panel/user-error";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@ModelAttribute Users user, HttpServletRequest request) {
        userMemService.deleteUserMember(user.getUserName(), request);
        return "redirect:/admin-panel/get-user/";
    }

    @PostMapping("/change-user")
    public String postChangeUser(@ModelAttribute Users user, @ModelAttribute Members member, HttpServletRequest request) {
        userMemService.updateUserMember(user, member, request);
        return "redirect:/admin-panel/change-user/" + user.getUserName();
    }
}
