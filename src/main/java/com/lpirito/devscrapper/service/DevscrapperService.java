package com.lpirito.devscrapper.service;

import com.lpirito.devscrapper.entity.DocumentEntity;
import com.lpirito.devscrapper.entity.ScrapperEntity;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.lpirito.devscrapper.helper.DocumentHelper.getDocument;


@Service
public class DevscrapperService {
    Logger logger = LoggerFactory.getLogger(DevscrapperService.class);

    static final String computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador";
    static final String linkedinUrl = "https://www.linkedin.com/jobs/search/?keywords=desarrollador&location=Argentina";


    public ArrayList<ScrapperEntity> getJobList() throws IOException {

        Document computrabajo = getDocument(computrabajoUrl);
        Document linkedin = getDocument(linkedinUrl);
        DocumentEntity computrabajoOrigin = new DocumentEntity(0, "computrabajo", computrabajoUrl);
        DocumentEntity linkedinOrigin = new DocumentEntity(1, "linkedin", linkedinUrl);

        Elements computrabajoTitles = computrabajo.select("a.js-o-link.fc_base");
        Elements linkedinTitles = linkedin.select("h3.base-search-card__title");


        ArrayList<ScrapperEntity> allJobPosts = new ArrayList<>();
        sourceOfJobPosts(computrabajoTitles, computrabajoOrigin, allJobPosts);
        sourceOfJobPosts(linkedinTitles, linkedinOrigin, allJobPosts);
        allPostsHandler(allJobPosts);
        return new ArrayList<>(allJobPosts);
    }

    private void allPostsHandler(ArrayList<ScrapperEntity> allJobPosts) {
        int index = 0;
        for (ScrapperEntity jobPost : allJobPosts) {
            jobPost.setId(index++);
            jobPost.setDescription("Asdf");
        }
    }

    private void sourceOfJobPosts(Elements titles, DocumentEntity document, ArrayList<ScrapperEntity> allJobPosts) {
        for (Element unparsedTitles : titles) {
            ScrapperEntity jobPost = new ScrapperEntity();
            String parsedTitles = unparsedTitles.text();
            jobPost.setOrigin(document);
            jobPost.setJobTitle(parsedTitles);
            allJobPosts.add(jobPost);
        }
    }

}
