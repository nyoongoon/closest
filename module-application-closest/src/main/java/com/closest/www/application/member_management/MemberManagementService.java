package com.closest.www.application.member_management;

import com.closest.www.application.rss.FailToReadFeedException;
import com.closest.www.application.rss.RssFeedReader;
import com.closest.www.domain.Post.Post;
import com.closest.www.domain.Subscription.Subscription;
import com.closest.www.domain.blog.Blog;
import com.closest.www.domain.blog.BlogDomain;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberDomain;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

@Service
public class MemberManagementService {
    private final RssFeedReader rssFeedReader;
    private final MemberDomain memberDomain;
    private final BlogDomain blogDomain;

    public MemberManagementService(RssFeedReader rssFeedReader,
                                   MemberDomain memberDomain,
                                   BlogDomain blogDomain) {
        this.rssFeedReader = rssFeedReader;
        this.memberDomain = memberDomain;
        this.blogDomain = blogDomain;
    }

    @Transactional
    public void memberSubscriptsBlog(String userEmail, URL url) throws FailToReadFeedException {
        Member member = memberDomain.findMemberByUserEmail(userEmail);

        Blog blog = blogDomain.existsByUrl(url) ?
                blogDomain.findBlogByUrl(url) : blogDomain.saveByUrl(url);

        Subscription.of(member, blog); //persistence cascade
        updateBlogPosts(blog);
    }

    @Transactional
    public void updateBlogPosts(Blog blog) throws FailToReadFeedException {
        SyndFeed syndFeed = rssFeedReader.readFeed(blog.getUrl());
        List<SyndEntry> entries = syndFeed.getEntries();

        entries.stream().map(e -> Post.of(e.getTitle(), e.getLink(), blog))
                .toList();

        Date publishedDate = entries.get(0).getPublishedDate();
        //Date to LocalDateTiem
        LocalDateTime localDateTime = publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        blog.updateLastPublishedDate(localDateTime);
    }
}
