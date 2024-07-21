package com.closest.www.domain.feed;

import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.springframework.stereotype.Repository;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;

@Repository
public class RssFeedReader implements FeedRepository {

    public Optional<Feed> findByUrl(URL url) {
        try {
            XmlReader reader = new XmlReader(url);
            return Optional.of(new FeedImpl(new SyndFeedInput().build(reader)));
        } catch (FeedException | IOException e) {
            return Optional.empty();
        }
    }
}

