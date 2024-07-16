package com.closest.www.api.service.blog;

import com.closest.www.api.controller.blog.response.BlogResponse;
import com.closest.www.api.service.auth.exception.MemberNotFoundException;
import com.closest.www.api.service.blog.exception.BlogNotFoundException;
import com.closest.www.domain.feed.RssFeedReader;
import com.closest.www.domain.blog.Blog;
import com.closest.www.domain.blog.BlogRepository;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberRepository;
import com.closest.www.domain.post.Post;
import com.closest.www.domain.subscription.Subscription;
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
public class BlogService {
    private final RssFeedReader rssFeedReader;
    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;

    public BlogService(RssFeedReader rssFeedReader,
                       MemberRepository memberRepository,
                       BlogRepository blogRepository) {
        this.rssFeedReader = rssFeedReader;
        this.memberRepository = memberRepository;
        this.blogRepository = blogRepository;
    }

    @Transactional
    public void memberSubscriptsBlog(String userEmail, URL url) {
        Member member = memberRepository.findByUserEmail(userEmail)
                .orElseThrow(MemberNotFoundException::new);

        Blog blog;
        if (blogRepository.existsByUrl(url)) {
            blog = blogRepository.findByUrl(url)
                    .orElseThrow(BlogNotFoundException::new);
        } else {
            SyndFeed syndFeed = rssFeedReader.readFeed(url);
            Blog.create(
                    url,
                    syndFeed.getAuthor(),
                    syndFeed.getPublishedDate()
            )
//            blog = blogDomain.saveByUrlAndAuthor(url, syndFeed.getAuthor());
        }

        Subscription.create(member, blog); //persistence cascade

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
    public List<BlogResponse> getBlogViewsByMember(Member member) throws MalformedURLException {
        List<BlogResponse> blogResponses = new ArrayList<>(); //result
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

            blogResponses.add(
                    BlogResponse.of(author, url, isUpdated, lastPublishedUrl)
            );
        }

        return blogResponses;
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
