package com.lpirito.devscrapper.service;

import com.lpirito.devscrapper.entity.ComputrabajoEntity;
import com.lpirito.devscrapper.entity.DocumentEntity;
import com.lpirito.devscrapper.entity.JobPostEntity;
import com.lpirito.devscrapper.entity.LinkedinEntity;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import static com.lpirito.devscrapper.helper.DocumentConnector.getDocument;

@Service
public class DevscrapperService {

    static String computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador?p=";
    static String linkedinUrl = "https://www.linkedin.com/jobs/search/?keywords=desarrollador";

    public ArrayList<JobPostEntity> getJobList() {

        ArrayList<JobPostEntity> response = new ArrayList<>();
        response.addAll(computrabajoPosts());
        response.addAll(linkedinPosts());

        return response;
    }


    // handles individual job postings of computrabajo
    public ArrayList<ComputrabajoEntity> computrabajoPosts() {
        ArrayList<ComputrabajoEntity> computrabajoJobPostsArray = new ArrayList<>();
        int numOfPages = 0;
        while (numOfPages <= 5) {
            computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador?p=" + numOfPages;
            numOfPages++;
            Element computrabajoHtmlDocument = getDocument(computrabajoUrl);
            Elements computrabajoJobPosts = computrabajoHtmlDocument.select(".w100.bClick");

            // where the job post came from
            DocumentEntity computrabajoOrigin = new DocumentEntity(0, "computrabajo", computrabajoUrl);

            for (Element computrabajoJobPost : computrabajoJobPosts) {
                ComputrabajoEntity computrabajoEntity = new ComputrabajoEntity();

                String jobPostTitle = computrabajoJobPost.select("a.js-o-link.fc_base").text();
                computrabajoEntity.setJobTitle(jobPostTitle);

                String jobPostHref = computrabajoJobPost.select("a.js-o-link.fc_base").attr("href");
                String url = "https://www.computrabajo.com.ar".concat(jobPostHref);
                computrabajoEntity.setUrl(url);

                String wasPosted = computrabajoJobPost.select(".fs13.fc_aux").text();
                computrabajoEntity.setWasPosted(wasPosted);

                computrabajoEntity.setOrigin(computrabajoOrigin);

                computrabajoJobPostsArray.add(computrabajoEntity);
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
