package com.closest.www.application.member_management;

import com.closest.www.application.rss.FailToReadFeedException;
import com.closest.www.application.rss.RssFeedReader;
import com.closest.www.domain.blog.Blog;
import com.closest.www.domain.blog.BlogDomain;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberDomain;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
class MemberManagementServiceTest {
    @Autowired
    BlogDomain blogDomain;
    @Autowired
    private MemberDomain memberDomain;
    @Autowired
    private MemberManagementService memberManagementService;
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
        memberManagementService.memberSubscriptsBlog(userEmail, link);
        // then
        assertThat(member.getSubscriptions().get(0).getBlog().getUrl()).isEqualTo(link);
    }

    @Test
    @DisplayName("블로그에 포스트 업데이트 시 최대 50개 포스트가 업데이트된다.")
    @Transactional
    void test2() throws MalformedURLException, FailToReadFeedException {
        // given
        URL link = new URL("https://goalinnext.tistory.com/rss");
        Blog blog = blogDomain.saveByUrl(link);

        // when
        memberManagementService.updateBlogPosts(blog);

        // then
        SyndFeed syndFeed = rssFeedReader.readFeed(blog.getUrl());
        List<SyndEntry> entries = syndFeed.getEntries();

        assertThat(blog.getPosts().size()).isEqualTo(entries.size());
    }

    @Test
    @DisplayName("블로그에 포스트 업데이트 시 최근 발생시간이 업데이트 된다.")
    @Transactional
    void test3() throws MalformedURLException, FailToReadFeedException {
        // given
        URL link = new URL("https://goalinnext.tistory.com/rss");
        Blog blog = blogDomain.saveByUrl(link);

        // when
        memberManagementService.updateBlogPosts(blog);

        // then
        SyndFeed syndFeed = rssFeedReader.readFeed(blog.getUrl());
        List<SyndEntry> entries = syndFeed.getEntries();
        Date publishedDate = entries.get(0).getPublishedDate();
        LocalDateTime localDateTime = publishedDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        assertThat(blog.getLastPublishedDate()).isEqualTo(localDateTime);
    }
}