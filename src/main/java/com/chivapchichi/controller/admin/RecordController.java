package com.chivapchichi.controller.admin;

import com.chivapchichi.model.Record;
import com.chivapchichi.service.api.admin.RecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("admin-panel")
public class RecordController {

    private final RecordService recordService;

    @Autowired
    public RecordController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("/get-all-records")
    public String getAllRecords(Model model) {
        model.addAttribute("records", recordService.getAll());
        return "admin-panel/all-records";
    }

    @GetMapping("/get-records-by-tournament/{id}")
    public String getRecordByIdTournament(@PathVariable("id") Integer idTournament, Model model) {
        model.addAttribute("records", recordService.getByTournament(idTournament));
        return "admin-panel/all-records";
    }

    @GetMapping("/get-record/{id}")
    public String getRecordById(@PathVariable("id") Integer id, Model model) {
        Record record = recordService.getRecordById(id);
        model.addAttribute("isPresent", record != null);
        if (record != null) {
            model.addAttribute("record", record);
        }
        return "admin-panel/record";
    }

    @PostMapping("/delete-record")
    public String deleteRecord(@ModelAttribute Record record, HttpServletRequest request) {
        recordService.deleteById(record.getId(), request);
        return "redirect:/admin-panel/get-all-records/";
    }
}
