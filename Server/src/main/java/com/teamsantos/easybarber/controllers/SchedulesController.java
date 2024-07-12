package com.teamsantos.easybarber.controllers;

import org.springframework.stereotype.Controller;

import com.teamsantos.easybarber.services.SchedulesService;

@Controller
public class SchedulesController {
    private final SchedulesService schedulesService;

    public SchedulesController(SchedulesService schedulesService) {
        this.schedulesService = schedulesService;
    }
}
