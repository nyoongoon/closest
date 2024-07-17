package com.closest.www.domain.feed;

import com.closest.www.domain.feed.exception.FeedNotFoundException;
import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.WireFeedInput;
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
            WireFeed wireFeed = new WireFeedInput().build(reader);
            return Optional.ofNullable(new Feed(wireFeed));
        } catch (FeedException | IOException e) {
            throw new FeedNotFoundException();
        }
    }
}

