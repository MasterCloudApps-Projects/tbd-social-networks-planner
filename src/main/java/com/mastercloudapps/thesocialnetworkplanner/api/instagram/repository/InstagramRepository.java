package com.mastercloudapps.thesocialnetworkplanner.api.instagram.repository;

import com.mastercloudapps.thesocialnetworkplanner.api.instagram.model.Instagram;
import com.mastercloudapps.thesocialnetworkplanner.api.schedule.Schedulable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InstagramRepository extends JpaRepository<Instagram, Long> {

    List<Schedulable> findByInstagramIdIsNull();
}
