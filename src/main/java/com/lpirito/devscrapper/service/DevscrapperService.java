package com.lpirito.devscrapper.service;

import com.lpirito.devscrapper.entity.ComputrabajoEntity;
import com.lpirito.devscrapper.entity.DocumentEntity;
import com.lpirito.devscrapper.entity.LinkedinEntity;
import com.lpirito.devscrapper.entity.ScrapperEntity;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;

import static com.lpirito.devscrapper.helper.DocumentHelper.getDocument;

@Service
public class DevscrapperService {

    static String computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador?p=";
    static String linkedinUrl = "https://www.linkedin.com/jobs/search/?keywords=desarrollador&location=Argentina";

    public ArrayList<ScrapperEntity> getJobList() {

        ArrayList<ScrapperEntity> response = new ArrayList<>();
        response.addAll(computrabajoPosts());
        response.addAll(linkedinPosts());
        responseIndexer(response);

        Collections.shuffle(response);
        return response;
    }

    private void responseIndexer(ArrayList<ScrapperEntity> response) {
        int index = 0;
        for (ScrapperEntity jobPostEntity : response) {
            jobPostEntity.setId(index++);
        }
    }

    // handles individual job postings of computrabajo
    public ArrayList<ComputrabajoEntity> computrabajoPosts() {
        ArrayList<ComputrabajoEntity> computrabajoJobPostsArray = new ArrayList<>();
        int numOfPages = 0;
        while (numOfPages < 5) {
            numOfPages++;
            computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador?p=" + numOfPages;
            Element computrabajoHtmlDocument = getDocument(computrabajoUrl);
            Elements computrabajoJobPostsTitles = computrabajoHtmlDocument.select("a.js-o-link.fc_base");
            // where the job post came from
            DocumentEntity computrabajoOrigin = new DocumentEntity(0, "computrabajo", computrabajoUrl);

            for (Element computrabajoTitle : computrabajoJobPostsTitles) {
                ComputrabajoEntity computrabajoJobPost = new ComputrabajoEntity();
                computrabajoJobPost.setJobTitle(computrabajoTitle.text());
                String linkToJobPost = computrabajoTitle.attr("href");
                String url = "https://www.computrabajo.com.ar".concat(linkToJobPost);
                computrabajoJobPost.setUrl(url);
                computrabajoJobPost.setOrigin(computrabajoOrigin);
                computrabajoJobPostsArray.add(computrabajoJobPost);
            }
        }
        return computrabajoJobPostsArray;
    }

    // handles individual job postings of linkedin
    public ArrayList<LinkedinEntity> linkedinPosts() {
        ArrayList<LinkedinEntity> linkedinEntityArray = new ArrayList<>();

        Document linkedin = getDocument(linkedinUrl);
        DocumentEntity linkedinOrigin = new DocumentEntity(1, "linkedin", linkedinUrl);
        Elements linkedinTitles = linkedin.select("a.base-card__full-link.absolute");

        for (Element linkedinTitle : linkedinTitles) {
            LinkedinEntity linkedinEntity = new LinkedinEntity();
            linkedinEntity.setJobTitle(linkedinTitle.text());
            linkedinEntity.setOrigin(linkedinOrigin);
            linkedinEntity.setUrl(linkedinTitle.attr("href"));

            linkedinEntityArray.add(linkedinEntity);
        }
        return linkedinEntityArray;
    }

}
