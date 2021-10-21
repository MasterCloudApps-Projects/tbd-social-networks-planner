package com.mastercloudapps.thesocialnetworkplanner.twitter.repository;

import com.mastercloudapps.thesocialnetworkplanner.twitter.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TweetRepository extends JpaRepository<Tweet, Long> {
}
