package com.lpirito.devscrapper.helper;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class DocumentConnector {

    public static Document getDocument(String url) throws IOException {
        Connection conn = Jsoup.connect(url);
        return conn.get();
    }
}
