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

// static import of Helper class to conect to the HTML document with JSoup
import static com.lpirito.devscrapper.helper.DocumentHelper.getDocument;


@Service
public class DevscrapperService {
    Logger logger = LoggerFactory.getLogger(DevscrapperService.class);

    static final String computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador";
    static final String linkedinUrl = "https://www.linkedin.com/jobs/search/?keywords=desarrollador&location=Argentina";


    public ArrayList<ScrapperEntity> getJobList() throws IOException {

        Document computrabajo = getDocument(computrabajoUrl);
        Document linkedin = getDocument(linkedinUrl);

        Elements computrabajoTitles = computrabajo.select("a.js-o-link.fc_base");
        Elements linkedinTitles = linkedin.select("h3.base-search-card__title");

        Elements links = computrabajo.select("#p_orders, article, div, h1, a.js-o-link.fc_base");


        ArrayList<Element> jobTitles = new ArrayList<>();
        jobTitles.addAll(computrabajoTitles);
        jobTitles.addAll(linkedinTitles);


        // storing all jobposts here for indexing and later feat
        ArrayList<ScrapperEntity> allJobPosts = new ArrayList<>();

        // entities to send in the response and recognize which pages had the job postings
        DocumentEntity computrabajoOrigin = new DocumentEntity(0, "computrabajo", computrabajoUrl);
        DocumentEntity linkedinOrigin = new DocumentEntity(1, "linkedin", linkedinUrl);


        jobPosts(computrabajoTitles, computrabajoOrigin, allJobPosts, links);
        jobPosts(linkedinTitles, linkedinOrigin, allJobPosts, links);
        allPostsHandler(allJobPosts);
        return new ArrayList<>(allJobPosts);
    }

    // handles all the posts, mainly for indexing
    private void allPostsHandler(ArrayList<ScrapperEntity> allJobPosts) {
        int index = 0;
        for (ScrapperEntity jobPost : allJobPosts) {
            jobPost.setId(index++);
        }
    }

    // handles individual job postings
    private void jobPosts(ArrayList<Element> jobTitles, DocumentEntity document, ArrayList<ScrapperEntity> allJobPosts, Elements links) {
        for (Element jobTitle : jobTitles) {
            ScrapperEntity jobPost = new ScrapperEntity();
            String parsedTitles = jobTitle.text();
            jobPost.setOrigin(document);
            jobPost.setJobTitle(parsedTitles);
            String hrefs = null;
            for (Element link : links) {
                hrefs = link.attr("href");
            }
            jobPost.setUrl(hrefs);
            allJobPosts.add(jobPost);
        }
    }

}
