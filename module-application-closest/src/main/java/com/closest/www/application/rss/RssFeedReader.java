package com.closest.www.application.rss;

import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Component
public class RssFeedReader {

    public SyndFeed readFeed(URL url) throws FailToReadFeedException {
        try {
            XmlReader reader = new XmlReader(url);
            return new SyndFeedInput().build(reader);
        } catch (FeedException | IOException e) {
            throw new FailToReadFeedException();
        }
    }
}

