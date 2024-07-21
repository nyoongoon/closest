package com.closest.www.api.service.blog;

import com.closest.www.api.controller.blog.response.BlogResponse;
import com.closest.www.api.service.auth.exception.MemberNotFoundException;
import com.closest.www.api.service.blog.exception.BlogNotFoundException;
import com.closest.www.domain.blog.Blog;
import com.closest.www.domain.blog.BlogRepository;
import com.closest.www.domain.feed.Feed;
import com.closest.www.domain.feed.FeedItem;
import com.closest.www.domain.feed.FeedRepository;
import com.closest.www.domain.feed.exception.FeedNotFoundException;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberRepository;
import com.closest.www.domain.post.Post;
import com.closest.www.domain.post.PostRepository;
import com.closest.www.domain.subscription.Subscription;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlogService {
    private final FeedRepository feedRepository;
    private final MemberRepository memberRepository;
    private final BlogRepository blogRepository;
    private final PostRepository postRepository;

    public BlogService(FeedRepository feedRepository,
                       MemberRepository memberRepository,
                       BlogRepository blogRepository, PostRepository postRepository) {
        this.feedRepository = feedRepository;
        this.memberRepository = memberRepository;
        this.blogRepository = blogRepository;
        this.postRepository = postRepository;
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
            Feed feed = feedRepository.findByUrl(url)
                    .orElseThrow(FeedNotFoundException::new);
            blog = Blog.create(
                    url,
                    feed.getAuthor(), // todo 검증하기 -> LocalDate -> LocalDateTime으로 변환시 시간은 기본값으로 강제 설정 아닌가? 의미가 없지 않나?
                    feed.getLastPublishdDate()
            );
//            blog = blogDomain.saveByUrlAndAuthor(url, syndFeed.getAuthor());
        }

        Subscription.create(member, blog); //persistence cascade

        Feed feed = feedRepository.findByUrl(blog.getUrl())
                .orElseThrow(FeedNotFoundException::new);
        putAllPostsOfBlog(blog, feed);
    }

    @Transactional
    public Blog putAllPostsOfBlog(Blog blog, Feed feed) {
        postRepository.deleteAllByBlog(blog);

        List<FeedItem> feedItems = feed.getFeedItems();
//        List<SyndEntry> entries = syndFeed.getEntries();

        feedItems.stream()
                .forEach(e -> Post.of(e.getTitle(), e.getUrl(), blog)); //Blog-Post 연관관계등록


        LocalDateTime localDateTime = feed.getLastPublishdDate();
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
            Feed feed = feedRepository.findByUrl(blog.getUrl())
                    .orElseThrow(FeedNotFoundException::new);
            LocalDateTime lastPublishdLocalDateTime = feed.getLastPublishdDate();

            boolean isUpdated = blog.isUpdated(lastPublishdLocalDateTime);
            blog = isUpdated ? putAllPostsOfBlog(blog, feed) : blog;

            String author = blog.getAuthor();
            URL url = blog.getUrl();
            feed.getLastPublishedFeedItem();

            URL lastPublishedUrl = getLastPublishdUrl(syndFeed);

            blogResponses.add(
                    BlogResponse.of(author, url, isUpdated, lastPublishedUrl)
            );
        }

        return blogResponses;
    }

    private URL getLastPublishdUrl(SyndFeed syndFeed) throws MalformedURLException {
        String link = syndFeed.getEntries().get(0).getLink();//정렬이 되어 있음
        return new URL(link);
    }
}
