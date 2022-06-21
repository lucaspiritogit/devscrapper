package com.lpirito.devscrapper.service;

import com.lpirito.devscrapper.entity.DocumentEntity;
import com.lpirito.devscrapper.entity.ScrapperEntity;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

import static com.lpirito.devscrapper.helper.DocumentHelper.getDocument;

// urls

@Service
public class DevscrapperService {
    static final String computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador";
    static final String linkedinUrl = "https://www.linkedin.com/jobs/search/?keywords=desarrollador&location=Argentina";


    Logger logger = LoggerFactory.getLogger(DevscrapperService.class);


    public ArrayList<ScrapperEntity> getJobList() throws IOException {

        ArrayList<DocumentEntity> documents = new ArrayList<>();
        DocumentEntity computrabajoDocument = new DocumentEntity(0, "computrabajo", computrabajoUrl);
        documents.add(computrabajoDocument);
        DocumentEntity linkedinDocument = new DocumentEntity(1, "linkedin", linkedinUrl);
        documents.add(linkedinDocument);


        Elements jobTitlesComputrabajo = getDocument(computrabajoUrl).select("a.js-o-link.fc_base");
        Elements jobTitlesLinkedin = getDocument(linkedinUrl).select("h3.base-search-card__title");

        ArrayList<ScrapperEntity> allJobPosts = new ArrayList<>();

        for (int i = 0; i < jobTitlesComputrabajo.size(); i++) {
            ScrapperEntity jobPostCt = new ScrapperEntity();
            String parsedTitles = jobTitlesComputrabajo.get(i).text();
            jobPostCt.setId(i);
            jobPostCt.setOrigin(computrabajoDocument);
            jobPostCt.setJobTitle(parsedTitles);
            allJobPosts.add(jobPostCt);

        }
        for (int j = 0; j < jobTitlesLinkedin.size(); j++) {
            ScrapperEntity jobPostLd = new ScrapperEntity();
            String parsedTitles = jobTitlesLinkedin.get(j).text();
            jobPostLd.setId(j);
            jobPostLd.setOrigin(linkedinDocument);
            jobPostLd.setJobTitle(parsedTitles);
            allJobPosts.add(jobPostLd);
        }

        int index = 0;
        for (ScrapperEntity jobPost : allJobPosts) {
            jobPost.setId(index++);
        }

        return new ArrayList<>(allJobPosts);
    }

}
