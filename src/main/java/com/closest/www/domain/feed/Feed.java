package com.closest.www.domain.feed;

import java.util.Date;
import java.util.List;

public interface Feed {
    String getAuthor();

    Date getLastPublishdDate();

    List<FeedItem> getFeedItems();

    FeedItem getLastPublishedFeedItem();
}
