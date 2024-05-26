package com.example.closest.domain.blog;

import com.example.closest.domain.Subscription.Subscription;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "blog")
public class Blog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String link;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "blog")
    private List<Subscription> subscriptions = new ArrayList<>();

    protected Blog(){}

    public Blog(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public List<Subscription> getSubscriptions() {
        return subscriptions;
    }
}