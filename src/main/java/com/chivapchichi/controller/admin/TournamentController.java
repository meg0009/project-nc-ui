package com.chivapchichi.controller.admin;

import com.chivapchichi.model.Tournament;
import com.chivapchichi.service.api.admin.TournamentAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin-panel")
public class TournamentController {

    private final TournamentAdminService tournamentAdminService;

    @Autowired
    public TournamentController(TournamentAdminService tournamentAdminService) {
        this.tournamentAdminService = tournamentAdminService;
    }

    @GetMapping("/add-tournament")
    public String addTournament(Model model) {
        model.addAttribute("tournament", new Tournament());
        return "admin-panel/add-tournament";
    }

    @PostMapping("/add-tournament")
    public String postAddTournament(@ModelAttribute Tournament tournament, HttpServletRequest request) {
        tournamentAdminService.saveTournament(tournament, request);
        return "success-registration";
    }

    @GetMapping("/delete-tournament")
    public String deleteTournament(Model model) {
        model.addAttribute("tournaments", tournamentAdminService.getAllTournaments());
        return "admin-panel/delete-tournament";
    }

    @PostMapping("/delete-tournament")
    public String postDeleteTournament(@RequestParam(value = "id") Integer id, HttpServletRequest request) {
        tournamentAdminService.deleteTournament(id, request);
        return "redirect:/admin-panel/delete-tournament/";
    }

    @GetMapping("/change-tournament")
    public String changeTournament(Model model) {
        model.addAttribute("tournaments", tournamentAdminService.getAllTournaments());
        return "admin-panel/change-tournament";
    }

    @GetMapping("/change-tournament/{id}")
    public String changeTournamentId(@PathVariable(value = "id") Integer id, Model model) {
        Tournament tournament = tournamentAdminService.getTournamentById(id);
        if (tournament != null) {
            model.addAttribute("tournament", tournament);
            return "admin-panel/change-tournament-id";
        }
        return "admin-panel/tournament-error";
    }

    @PostMapping("/change-tournament/{id}")
    public String postChangeTournament(@PathVariable(value = "id") Integer id, @ModelAttribute Tournament tournament, HttpServletRequest request) {
        tournament.setId(id);
        tournamentAdminService.saveTournament(tournament, request);
        return "redirect:/admin-panel/change-tournament/" + id;
    }
}
