package com.lpirito.devscrapper.service;

import com.lpirito.devscrapper.entity.ComputrabajoEntity;
import com.lpirito.devscrapper.entity.DocumentEntity;
import com.lpirito.devscrapper.entity.JobPostEntity;
import com.lpirito.devscrapper.entity.LinkedinEntity;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;

import static com.lpirito.devscrapper.helper.DocumentConnector.getDocument;

@Service
public class DevscrapperService {


    public ArrayList<JobPostEntity> getJobList() throws IOException {

        ArrayList<JobPostEntity> response = new ArrayList<>();
        response.addAll(computrabajoPosts());
        response.addAll(linkedinPosts());


        return response;
    }


    // handles individual job postings of computrabajo
    public ArrayList<ComputrabajoEntity> computrabajoPosts() throws IOException {
        ArrayList<ComputrabajoEntity> computrabajoJobPostsArray = new ArrayList<>();

        int numOfPages = 1;
        while (numOfPages <= 2) {
            String computrabajoUrl = "https://www.computrabajo.com.ar/trabajo-de-desarrollador?p=" + numOfPages;
            Document computrabajoHtmlDocument = getDocument(computrabajoUrl);
            Elements computrabajoJobPosts = computrabajoHtmlDocument.select(".w100.bClick");
            // where the job post came from
            DocumentEntity computrabajoOrigin = new DocumentEntity(numOfPages, "computrabajo", computrabajoUrl);
            numOfPages++;

            for (Element computrabajoJobPost : computrabajoJobPosts) {
                ComputrabajoEntity computrabajoEntity = new ComputrabajoEntity();

                String jobPostTitle = computrabajoJobPost.select("a.js-o-link.fc_base").text();
                computrabajoEntity.setJobTitle(jobPostTitle);

                String jobPostHref = computrabajoJobPost.select("a.js-o-link.fc_base").attr("href");
                String url = "https://www.computrabajo.com.ar".concat(jobPostHref);
                computrabajoEntity.setUrl(url);

                String company = computrabajoJobPost.select(".fs16.fc_base.mt5.mb10 > a").text();
                if (company.equals("")) {
                    company = computrabajoJobPost.select(".fs16.fc_base.mt5.mb10").text();
                }
                computrabajoEntity.setCompany(company);

                String location = getDocument(computrabajoEntity.getUrl()).select("body > main > div.box_border.menu_top.dFlex > div > div.fr.pt10.box_resume.hide_m > div > p.fs16").text();
                computrabajoEntity.setLocation(location);

                String wasPosted = computrabajoJobPost.select(".fs13.fc_aux").text();
                computrabajoEntity.setWasPosted(wasPosted);

                computrabajoEntity.setOrigin(computrabajoOrigin);

                computrabajoJobPostsArray.add(computrabajoEntity);
            }
        }

        return computrabajoJobPostsArray;
    }

    // handles individual job postings of linkedin
    /*
    After making some requests per day, linkedin blocks incoming requests by
    throwing HTTP error Status=999. I think that i need to log in before doing more requests.
    Jsoup can log in through a POST request, so ill be trying that if i can 👻
    */
    public ArrayList<LinkedinEntity> linkedinPosts() throws IOException {
        ArrayList<LinkedinEntity> linkedinEntityArray = new ArrayList<>();

        int numOfPages = 25;
        while (numOfPages <= 50) {
            String linkedinUrl = "https://www.linkedin.com/jobs/search/?keywords=desarrollador&start=" + numOfPages;
            Document linkedinHtmlDocument = getDocument(linkedinUrl);
            Elements linkedinTitles = linkedinHtmlDocument.select("a.base-card__full-link.absolute");
            // where the job post came from
            DocumentEntity linkedinOrigin = new DocumentEntity(numOfPages, "linkedin", linkedinUrl);
            numOfPages += 25;

            for (Element linkedinTitle : linkedinTitles) {
                LinkedinEntity linkedinEntity = new LinkedinEntity();
                linkedinEntity.setJobTitle(linkedinTitle.text());
                linkedinEntity.setOrigin(linkedinOrigin);
                linkedinEntity.setUrl(linkedinTitle.attr("href"));

                linkedinEntityArray.add(linkedinEntity);
            }
        }
        return linkedinEntityArray;
    }

}
