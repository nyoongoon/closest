package com.closest.www.client.rss;

import com.closest.www.domain.feed.Feed;
import com.closest.www.domain.feed.FeedItem;
import com.closest.www.domain.feed.RssFeedReader;
import com.closest.www.domain.feed.exception.FeedNotFoundException;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

@SpringBootTest
class RssFeedImplReaderTest {

    private static final Logger log = LoggerFactory.getLogger(RssFeedImplReaderTest.class);

    @Autowired
    private RssFeedReader rssFeedReader;

    @Test
    @DisplayName("RssFeed 테스트")
    void test1() throws MalformedURLException {
        URL url = new URL("https://jojoldu.tistory.com/rss");
        Feed feed = rssFeedReader.findByUrl(url).orElseThrow(FeedNotFoundException::new);
        List<FeedItem> items = feed.getFeedItems();

        for (FeedItem feedItem : items) {
            log.info("URL : {}", feedItem.getUrl());
            log.info("title : {}", feedItem.getTitle());
            log.info("publishedDate : {}", feedItem.getPublishedDate());
        }
    }
}