package com.closest.www.application.member_management;

import com.closest.www.application.rss.FailToReadFeedException;
import com.closest.www.application.rss.RssFeedReader;
import com.closest.www.domain.Post.Post;
import com.closest.www.domain.Subscription.Subscription;
import com.closest.www.domain.blog.Blog;
import com.closest.www.domain.blog.BlogDomain;
import com.closest.www.domain.member.Member;
import com.closest.www.domain.member.MemberDomain;
import com.rometools.rome.feed.synd.SyndFeed;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.MalformedURLException;
import java.net.URL;
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
    public void userSubscriptsBlog(String userEmail, String link) {
        Member member = memberDomain.findMemberByUserEmail(userEmail);



        Blog blog = blogDomain.existsByLink(link) ?
                blogDomain.findBlogByLink(link) : blogDomain.saveByLink(link);

        Subscription.of(member, blog); //persistence cascade
    }


    @Transactional
    public void updateBlogPosts(Long blogId) throws MalformedURLException, FailToReadFeedException {
        Blog blog = blogDomain.findBlogByIdWithPostUsingFetchJoin(blogId);


        SyndFeed syndFeed = rssFeedReader.readFeed(new URL(blog.getLink()));
        syndFeed.getEntries().size();


        List<Post> posts = blog.getPosts();

    }


}
