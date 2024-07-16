package com.closest.www.domain.feed;

import com.closest.www.domain.feed.exception.RssFeedReaderException;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;

@Repository
public class RssFeedReader implements FeedRespository{

    public

    public SyndFeed readFeed(URL url) {
        try {
            XmlReader reader = new XmlReader(url);
            return new SyndFeedInput().build(reader);
        } catch (FeedException | IOException e) {
            throw new RssFeedReaderException();
        }
    }
}

