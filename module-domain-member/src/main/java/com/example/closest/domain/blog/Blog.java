package com.example.closest.domain.blog;

import com.example.closest.domain.Subscription.Subscription;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "blog")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Subscription> subscriptions = new ArrayList<>();

    @Builder
    public Blog(String link) {
        this.link = link;
    }

}