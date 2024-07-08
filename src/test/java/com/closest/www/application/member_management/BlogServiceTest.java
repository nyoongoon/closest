package com.closest.www.application.member_management;

import com.closest.www.api.controller.blog.response.BlogResponse;
import com.closest.www.api.service.blog.BlogService;
import com.closest.www.client.rss.exception.FailToReadFeedException;
import com.closest.www.client.rss.RssFeedReader;
import com.closest.www.domain.blog.Blog;
import com.closest.www.domain.blog.BlogDomain;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberDomain;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class BlogServiceTest {
    private static final Logger log = LoggerFactory.getLogger(BlogServiceTest.class);
    @Autowired
    BlogDomain blogDomain;
    @Autowired
    private MemberDomain memberDomain;
    @Autowired
    private BlogService blogService;
    @Autowired
    private RssFeedReader rssFeedReader;

    @Test
    @DisplayName("블로그 엔티티 등록하고 멤버, 구독 엔티티와 연관관계 맺는다")
    @Transactional
    void test1() throws MalformedURLException, FailToReadFeedException {
        // given
        String userEmail = "abc@naver.com";
        Member member = new Member.Builder()
                .userEmail(userEmail)
                .password("1234")
                .build();
        memberDomain.regist(member);
        URL link = new URL("https://goalinnext.tistory.com/rss");
        // when
        blogService.memberSubscriptsBlog(userEmail, link);
        // then
        Blog saved = member.getSubscriptions().get(0).getBlog();
        assertThat(saved.getAuthor()).isEqualTo(rssFeedReader.readFeed(link).getAuthor());
        assertThat(saved.getUrl()).isEqualTo(link);
    }

    @Test
    @DisplayName("블로그에 포스트 업데이트 시 최대 50개 포스트가 업데이트된다.")
    @Transactional
    void test2() throws MalformedURLException, FailToReadFeedException {
        // given
        URL link = new URL("https://goalinnext.tistory.com/rss");
        SyndFeed syndFeed = rssFeedReader.readFeed(link);
        Blog blog = blogDomain.saveByUrlAndAuthor(link, syndFeed.getAuthor());
        // when
        blogService.putAllPostsOfBlog(blog, syndFeed);
        // then
        List<SyndEntry> entries = syndFeed.getEntries();
        assertThat(blog.getPosts().size()).isEqualTo(entries.size());
    }

    @Test
    @DisplayName("블로그에 포스트 업데이트 시 최근 발생시간이 업데이트 된다.")
    @Transactional
    void test3() throws MalformedURLException {
        // given
        URL link = new URL("https://goalinnext.tistory.com/rss");
        SyndFeed syndFeed = rssFeedReader.readFeed(link);
        Blog blog = blogDomain.saveByUrlAndAuthor(link, syndFeed.getAuthor());
        // when
        blogService.putAllPostsOfBlog(blog, syndFeed);
        // then
        List<SyndEntry> entries = syndFeed.getEntries();
        Date publishedDate = entries.get(0).getPublishedDate();
        LocalDateTime localDateTime = publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        assertThat(blog.getLastPublishedLocalDateTime()).isEqualTo(localDateTime);
    }

    @Test
    @DisplayName("멤버로 블로그 뷰 목록 조회.")
    @Transactional
    void test4() throws MalformedURLException {
        // given
        String userEmail = "abc@naver.com";
        Member member = new Member.Builder()
                .userEmail(userEmail)
                .password("1234")
                .build();
        memberDomain.regist(member);
        URL link1 = new URL("https://goalinnext.tistory.com/rss");
        blogService.memberSubscriptsBlog(userEmail, link1);
        URL link2 = new URL("https://jojoldu.tistory.com/rss");
        blogService.memberSubscriptsBlog(userEmail, link2);
        //when
        List<BlogResponse> blogResponses = blogService.getBlogViewsByMember(member);

        //then
        log.info("result: {}", blogResponses.get(0));
        assertThat(blogResponses.get(0).blogUrl()).isEqualTo(new URL("https://goalinnext.tistory.com/rss"));
        assertThat(blogResponses.get(1).blogUrl()).isEqualTo(new URL("https://jojoldu.tistory.com/rss"));
    }
}