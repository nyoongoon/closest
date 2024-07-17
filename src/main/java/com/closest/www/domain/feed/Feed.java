package com.closest.www.domain.feed;

import com.rometools.rome.feed.WireFeed;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.feed.synd.SyndFeedImpl;
import com.rometools.utils.Lists;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

public class Feed extends SyndFeedImpl {

    public Feed(WireFeed wireFeed) {
        super(wireFeed, false);
    }

    public LocalDateTime getLastPublishdLocalDateTime() {
        Date publishedDate = this.getEntries().get(0).getPublishedDate(); //정렬이 되어 있음
        //Date to LocalDateTiem
        return publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    public List<FeedItem> getFeedItems(){
//        List<SyndEntry> entries = this.getEntries();
        return List.of();
    }
}
