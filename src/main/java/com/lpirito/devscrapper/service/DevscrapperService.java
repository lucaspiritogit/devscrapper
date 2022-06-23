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

        Elements computrabajoJobsHref = computrabajo.select("a.js-o-link.fc_base[href]");
        Elements linkedinJobsHref = linkedinTitles;


        // entities to send in the response and recognize which pages had the job postings
        DocumentEntity computrabajoOrigin = new DocumentEntity(0, "computrabajo", computrabajoUrl);
        DocumentEntity linkedinOrigin = new DocumentEntity(1, "linkedin", linkedinUrl);


        ArrayList<ScrapperEntity> response = new ArrayList<>();
        jobPosts(computrabajoTitles, computrabajoOrigin, response, computrabajoJobsHref);
        jobPosts(linkedinTitles, linkedinOrigin, response, linkedinJobsHref);
        responseHandler(response);
        return response;
    }

    // handles all the posts, mainly for indexing
    private void responseHandler(ArrayList<ScrapperEntity> response) {
        int index = 0;
        for (ScrapperEntity jobPost : response) {
            jobPost.setId(index++);
        }
    }

    // handles individual job postings
    private void jobPosts(ArrayList<Element> jobTitles, DocumentEntity document, ArrayList<ScrapperEntity> response, Elements links) {
        for (Element jobTitle : jobTitles) {
            ScrapperEntity jobPost = new ScrapperEntity();
            jobPost.setOrigin(document);
            jobPost.setJobTitle(jobTitle.text());
            String hrefs = null;
            for (Element link : links) {
                hrefs = "https://www.computrabajo.com.ar".concat(link.attr("href"));
                jobPost.setUrl(hrefs);
                boolean isFromComputrabajo = jobPost.getOrigin().getSource().equals("computrabajo");
                if (!isFromComputrabajo) {
                    jobPost.setUrl("not found");
                }
            }
            response.add(jobPost);
        }
    }

}
