package com.lpirito.devscrapper.controller;

import com.lpirito.devscrapper.entity.ComputrabajoEntity;
import com.lpirito.devscrapper.entity.LinkedinEntity;
import com.lpirito.devscrapper.entity.JobPostEntity;
import com.lpirito.devscrapper.service.DevscrapperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;

@RequestMapping("/")
@RestController
public class DevscrapperController {

    @Autowired
    DevscrapperService service;


    @GetMapping(value = "/jobs/arg", produces = "application/json")
    public ResponseEntity<ArrayList<JobPostEntity>> getJobList() throws IOException {
        return new ResponseEntity<>(service.getJobList(), HttpStatus.OK);
    }

    @GetMapping(value = "/jobs/arg/computrabajo", produces = "application/json")
    public ArrayList<ComputrabajoEntity> getComputrabajoJobList() throws IOException {
        return service.computrabajoPosts();
    }

    @GetMapping(value = "/jobs/arg/linkedin", produces = "application/json")
    public ResponseEntity<ArrayList<LinkedinEntity>> getLinkedinJobList() throws IOException {
        return new ResponseEntity<>(service.linkedinPosts(), HttpStatus.OK);
    }
}
