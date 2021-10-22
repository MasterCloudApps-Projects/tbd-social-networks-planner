package com.mastercloudapps.thesocialnetworkplanner.twitter.repository;

import com.mastercloudapps.thesocialnetworkplanner.twitter.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    List<Tweet> findByTwitterIdIsNull();
}
