package com.closest.www.domain.feed;

import com.closest.www.domain.feed.exception.UrlException;
import com.rometools.rome.feed.synd.SyndEntry;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class FeedItemImpl implements FeedItem {

    private final SyndEntry syndEntry;

    public FeedItemImpl(SyndEntry syndEntry) {
        this.syndEntry = syndEntry;
    }

    @Override
    public String getTitle() {
        return syndEntry.getTitle();
    }

    @Override
    public URL getUrl() {
        try {
            return new URL(syndEntry.getLink());
        } catch (MalformedURLException e) {
            throw new UrlException();
        }
    }

    @Override
    public Date getPublishedDate() {
        return syndEntry.getPublishedDate();
    }
}
