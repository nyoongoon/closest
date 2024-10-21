package com.closest.www.api.service.blog;

import com.closest.www.api.controller.blog.response.BlogResponse;
import com.closest.www.api.service.blog.request.AddBlogServiceRequest;
import com.closest.www.domain.blog.Blog;
import com.closest.www.domain.blog.BlogRepository;
import com.closest.www.domain.feed.Feed;
import com.closest.www.domain.feed.FeedItem;
import com.closest.www.domain.feed.FeedRepository;
import com.closest.www.domain.feed.exception.FeedNotFoundException;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberJpaRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BlogServiceTest {
    private static final Logger log = LoggerFactory.getLogger(BlogServiceTest.class);
    @Autowired
    private BlogService blogService;
    @Autowired
    private MemberJpaRepository memberJpaRepository;
    @Autowired
    private FeedRepository feedRepository;
    @Autowired
    private BlogRepository blogRepository;

    @Test
    @DisplayName("블로그 엔티티 등록하고 멤버, 구독 엔티티와 연관관계 맺는다")
    @Transactional
    void memberSubscriptsBlog() throws MalformedURLException {
        // given
        String userEmail = "abc@naver.com";
        Member member = Member.builder()
                .userEmail(userEmail)
                .password("1234")
                .build();
        memberJpaRepository.save(member);

        URL url = new URL("https://goalinnext.tistory.com/rss");

        AddBlogServiceRequest request = new AddBlogServiceRequest(
                userEmail,
                url
        );

        // when
        blogService.memberSubscriptsBlog(request);
        // then
        Blog saved = member.getSubscriptions().get(0).getBlog();
        Feed feed = feedRepository.findByUrl(url).orElseThrow(FeedNotFoundException::new);
        assertThat(saved.getAuthor()).isEqualTo(feed.getAuthor());
        assertThat(saved.getUrl()).isEqualTo(url);
    }

    @Test
    @DisplayName("블로그에 포스트 업데이트 시 최대 50개 포스트가 업데이트된다.")
    @Transactional
    void test2() throws MalformedURLException {
        // given
        URL url = new URL("https://goalinnext.tistory.com/rss");
        Feed feed = feedRepository.findByUrl(url)
                .orElseThrow(FeedNotFoundException::new);
        Blog blog = Blog.create(url, feed.getAuthor(), feed.getLastPublishdDate());
        blogRepository.save(blog);

        // when
        blogService.putAllPostsOfBlog(blog, feed);
        // then
        List<FeedItem> feedItems = feed.getFeedItems();
        assertThat(blog.getPosts().size()).isEqualTo(feedItems.size());
    }

    @Test
    @DisplayName("블로그에 포스트 업데이트 시 최근 발행시간이 업데이트 된다.")
    @Transactional
    void test3() throws MalformedURLException {
        // given
        URL url = new URL("https://goalinnext.tistory.com/rss");
        Feed feed = feedRepository.findByUrl(url)
                .orElseThrow(FeedNotFoundException::new);
        Blog blog = Blog.create(
                url,
                feed.getAuthor(),
                feed.getLastPublishdDate()
        );
        blogRepository.save(blog);
        // when
        blogService.putAllPostsOfBlog(blog, feed);
        // then
        FeedItem lastPublishedFeedItem = feed.getLastPublishedFeedItem();
        assertThat(blog.getLastPublishedDate())
                .isEqualTo(lastPublishedFeedItem.getPublishedDate());
    }

    @Test
    @DisplayName("멤버로 블로그 뷰 목록 조회.")
    @Transactional
    void test4() throws MalformedURLException {
        // given
        String userEmail = "abc@naver.com";
        Member member = Member.builder()
                .userEmail(userEmail)
                .password("1234")
                .build();
        memberJpaRepository.save(member);
        URL url1 = new URL("https://goalinnext.tistory.com/rss");
        AddBlogServiceRequest request1 = new AddBlogServiceRequest(userEmail, url1);
        blogService.memberSubscriptsBlog(request1);
        URL url2 = new URL("https://jojoldu.tistory.com/rss");
        AddBlogServiceRequest request2 = new AddBlogServiceRequest(userEmail, url2);
        blogService.memberSubscriptsBlog(request2);
        //when
        List<BlogResponse> blogResponses = blogService.getBlogViewsByMember(member);

        //then
        log.info("result: {}", blogResponses.get(0));
        assertThat(blogResponses.get(0).blogUrl()).isEqualTo(new URL("https://goalinnext.tistory.com/rss"));
        assertThat(blogResponses.get(1).blogUrl()).isEqualTo(new URL("https://jojoldu.tistory.com/rss"));
    }
}