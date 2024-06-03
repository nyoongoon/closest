package com.closest.www.application.rss;

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
class RssFeedReaderTest {

    private static final Logger log = LoggerFactory.getLogger(RssFeedReaderTest.class);

    @Autowired
    private RssFeedReader rssFeedReader;

    @Test
    @DisplayName("")
    void test1() throws FailToReadFeedException, MalformedURLException {
        URL url = new URL("https://jojoldu.tistory.com/rss");
        SyndFeed syndFeed = rssFeedReader.readFeed(url);
//        log.info("result : {}" , syndFeed);
        List<SyndEntry> entries = syndFeed.getEntries();
        SyndEntry syndEntry = entries.get(0);
        log.info("syndEntry : {} ", syndEntry);
//        for (SyndEntry entry : entries) {
//            log.info("result : {}", entry);
//        }
//
//        log.info("size : {}", entries.size());
    }
}