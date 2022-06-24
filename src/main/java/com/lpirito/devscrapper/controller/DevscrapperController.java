package com.lpirito.devscrapper.controller;

import com.lpirito.devscrapper.entity.ScrapperEntity;
import com.lpirito.devscrapper.service.DevscrapperService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RestController
@RequestMapping("/")
public class DevscrapperController {

    Logger logger = LoggerFactory.getLogger(DevscrapperController.class);

    @Autowired
    DevscrapperService service;

    @GetMapping(value = "/jobs/arg", produces = "application/json")
    public ArrayList<ScrapperEntity> listOfJobs() throws IOException {
        return service.getJobList();
    }
}
