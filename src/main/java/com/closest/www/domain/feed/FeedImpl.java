package com.closest.www.domain.feed;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class FeedImpl implements Feed {

    private final SyndFeed syndFeed;

    protected FeedImpl(SyndFeed syndFeed) {
        this.syndFeed = syndFeed;
    }

    @Override
    public String getAuthor() {
        return syndFeed.getAuthor();
    }

    @Override
    public Date getLastPublishdDate() {
        return syndFeed.getEntries().get(0).getPublishedDate(); //정렬이 되어 있음
        //Date to LocalDateTiem
        return publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    @Override
    public List<FeedItem> getFeedItems() {
        List<SyndEntry> entries = this.syndFeed.getEntries();

        return entries.stream()
                .map(FeedItemImpl::new).collect(Collectors.toList());
    }

    @Override
    public FeedItem getLastPublishedFeedItem() {
        return new FeedItemImpl(this.syndFeed.getEntries().getFirst()); //정렬되어 있음
    }
}


