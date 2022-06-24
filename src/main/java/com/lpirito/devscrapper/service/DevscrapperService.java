package com.lpirito.devscrapper.service;

import com.lpirito.devscrapper.entity.ComputrabajoEntity;
import com.lpirito.devscrapper.entity.DocumentEntity;
import com.lpirito.devscrapper.entity.LinkedinEntity;
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

    static final String computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador";
    static final String linkedinUrl = "https://www.linkedin.com/jobs/search/?keywords=desarrollador&location=Argentina";

    public ArrayList<ScrapperEntity> getJobList() throws IOException {

        ArrayList<ScrapperEntity> response = new ArrayList<>();
        response.addAll(computrabajoPosts());
        response.addAll(linkedinPosts());
        responseHandler(response);

        return response;
    }

    private void responseHandler(ArrayList<ScrapperEntity> response) {
        int index = 0;
        for (ScrapperEntity jobPostEntity : response) {
            jobPostEntity.setId(index++);
        }
    }


    // handles individual job postings of computrabajo
    private ArrayList<ComputrabajoEntity> computrabajoPosts() throws IOException {
        ArrayList<ComputrabajoEntity> computrabajoEntityArray = new ArrayList<>();

        Document computrabajo = getDocument(computrabajoUrl);
        DocumentEntity computrabajoOrigin = new DocumentEntity(0, "computrabajo", computrabajoUrl);
        Elements computrabajoTitles = computrabajo.select("a.js-o-link.fc_base");

        for (Element computrabajoTitle : computrabajoTitles) {
            ComputrabajoEntity computrabajoEntity = new ComputrabajoEntity();
            computrabajoEntity.setJobTitle(computrabajoTitle.text());
            computrabajoEntity.setOrigin(computrabajoOrigin);
            computrabajoEntity.setUrl("https://www.computrabajo.com.ar".concat(computrabajoTitle.attr("href")));
            computrabajoEntityArray.add(computrabajoEntity);
        }

        return computrabajoEntityArray;
    }

    // handles individual job postings of linkedin
    private ArrayList<LinkedinEntity> linkedinPosts() throws IOException {
        ArrayList<LinkedinEntity> linkedinEntityArray = new ArrayList<>();

        Document linkedin = getDocument(linkedinUrl);
        DocumentEntity linkedinOrigin = new DocumentEntity(1, "linkedin", linkedinUrl);
        Elements linkedinTitles = linkedin.select("h3.base-search-card__title");
        //TODO
        String linkedinJobsHrefs = "asd";
;
        for (Element linkedinTitle : linkedinTitles) {
            LinkedinEntity linkedinEntity = new LinkedinEntity();
            linkedinEntity.setJobTitle(linkedinTitle.text());
            linkedinEntity.setOrigin(linkedinOrigin);
            linkedinEntity.setUrl(linkedinJobsHrefs);

            linkedinEntityArray.add(linkedinEntity);
        }
        return linkedinEntityArray;
    }

}
