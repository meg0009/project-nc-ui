package com.chivapchichi.controller;

import com.chivapchichi.service.api.TournamentRegService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("tournament")
public class TournamentRegistrationController {

    private final TournamentRegService tournamentRegService;

    @Autowired
    public TournamentRegistrationController(TournamentRegService tournamentRegService) {;
        this.tournamentRegService = tournamentRegService;
    }

    @GetMapping("/registration")
    public String homepage(Model model) {
        tournamentRegService.homepage(model);
        return "redirect:/tournament/registration/tournaments";
    }

    @GetMapping("/registration/tournaments")
    public String getDates(Model model, HttpServletRequest request) {
        tournamentRegService.tournaments(model);
        return "tournaments";
    }

    @GetMapping("/registration/tournaments/{division}")
    public String getDates(@PathVariable(value = "division") String division, Model model, HttpServletRequest request) {
        tournamentRegService.tournamentsWithDivision(model, division);
        return "tournaments";
    }

    @GetMapping("/registration/{idTournament}")
    public String formRegistration(@PathVariable("idTournament") Integer id, Model model, HttpServletRequest request) {
        tournamentRegService.formRegistration(id, model, request);
        return "tournament-registration";
    }

    @PostMapping("/registration/{idTournament}")
    public String registered(@PathVariable("idTournament") Integer id, HttpServletRequest request) {
        tournamentRegService.registerOnTournament(id, request);
        return "redirect:/tournament/registration/" + id;
    }

    @PostMapping("/unregistration/{idTournament}")
    public String unregistered(@PathVariable("idTournament") Integer id, HttpServletRequest request) {
        tournamentRegService.unregisterOnTournament(id, request);
        return "redirect:/tournament/registration/" + id;
    }
}
