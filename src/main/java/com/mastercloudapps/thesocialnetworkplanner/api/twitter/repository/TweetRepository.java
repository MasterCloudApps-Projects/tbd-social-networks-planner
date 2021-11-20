package com.mastercloudapps.thesocialnetworkplanner.api.twitter.repository;

import com.mastercloudapps.thesocialnetworkplanner.api.twitter.model.Tweet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TweetRepository extends JpaRepository<Tweet, Long> {

    Optional<Tweet> findByTwitterId(Long twitterId);

    List<Tweet> findByTwitterIdIsNull();
}
