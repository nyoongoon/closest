package com.closest.www.domain.blog;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.net.URL;
import java.util.Optional;

@Repository
interface BlogRepository extends JpaRepository<Blog, Long> {
    Optional<Blog> findByLink(String link);
//    @Query("SELECT distinct t FROM Team t join fetch t.members")
//    public List<Team> findAllWithMemberUsingFetchJoin();

    @Query("SELECT b FROM Blog b join fetch b.posts WHERE b.id = :id")
    Optional<Blog> findBlogByIdWithPostUsingFetchJoin(Long id);

    boolean existsByLink(URL link);
}
