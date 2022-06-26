package com.lpirito.devscrapper.helper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentConnector {

    public static Document getDocument(String url) {
        Connection conn = Jsoup.connect(url);
        Document doc = null;
        try {
            doc = conn.get();
        } catch (IOException e) {
            e.printStackTrace();
            // handle error
        }
        return doc;
    }
}
