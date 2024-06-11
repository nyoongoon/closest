package com.closest.www.application.member_management;

import com.closest.www.application.member_management.response.BlogView;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
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
    public void memberSubscriptsBlog(String userEmail, URL url) {
        Member member = memberDomain.findMemberByUserEmail(userEmail);

        Blog blog;
        if (blogDomain.existsByUrl(url)) {
            blog = blogDomain.findBlogByUrl(url);
        } else {
            SyndFeed syndFeed = rssFeedReader.readFeed(url);
            blog = blogDomain.saveByUrlAndAuthor(url, syndFeed.getAuthor());
        }

        Subscription.of(member, blog); //persistence cascade

        SyndFeed syndFeed = rssFeedReader.readFeed(blog.getUrl());
        putAllPostsOfBlog(blog, syndFeed);
    }

    @Transactional
    public Blog putAllPostsOfBlog(Blog blog, SyndFeed syndFeed) {
        blogDomain.clearPosts(blog);
        List<SyndEntry> entries = syndFeed.getEntries();

        entries.stream()
                .map(e -> Post.of(e.getTitle(), e.getLink(), blog)) //Blog-Post 연관관계등록
                .toList();

        LocalDateTime localDateTime = getLastPublishdLocalDateTime(syndFeed);
        blog.updateLastPublishedDate(localDateTime);
        return blog;
    }

    @Transactional
    public List<BlogView> getBlogViewsByMember(Member member) throws  MalformedURLException {
        List<BlogView> blogViews = new ArrayList<>(); //result
        List<Subscription> subscriptions = member.getSubscriptions();
        List<Blog> blogs = subscriptions.stream()
                .map(Subscription::getBlog).toList();

        for (Blog blog : blogs) {
            SyndFeed syndFeed = rssFeedReader.readFeed(blog.getUrl());
            LocalDateTime lastPublishdLocalDateTime = getLastPublishdLocalDateTime(syndFeed);
            boolean isUpdated = blog.isUpdated(lastPublishdLocalDateTime);
            blog = isUpdated ? putAllPostsOfBlog(blog, syndFeed) : blog;

            String author = blog.getAuthor();
            URL url = blog.getUrl();
            URL lastPublishedUrl = getLastPublishdUrl(syndFeed);

            blogViews.add(
                    BlogView.of(author, url, isUpdated, lastPublishedUrl)
            );
        }

        return blogViews;
    }


    private LocalDateTime getLastPublishdLocalDateTime(SyndFeed syndFeed) {
        Date publishedDate = syndFeed.getEntries().get(0).getPublishedDate(); //정렬이 되어 있음
        //Date to LocalDateTiem
        return publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }

    private URL getLastPublishdUrl(SyndFeed syndFeed) throws MalformedURLException {
        String link = syndFeed.getEntries().get(0).getLink();//정렬이 되어 있음
        return new URL(link);
    }
}
