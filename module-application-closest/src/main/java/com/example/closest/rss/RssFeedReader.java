package com.example.closest.rss;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;

@Component
public class RssFeedReader {

    public SyndFeed readFeed(String rssUrl) throws FailToReadFeedException {
        try {
            URL url = new URL(rssUrl);
            XmlReader reader = new XmlReader(url);
            return new SyndFeedInput().build(reader);
        } catch (FeedException | IOException e) {
            throw new FailToReadFeedException();
        }
    }
}

