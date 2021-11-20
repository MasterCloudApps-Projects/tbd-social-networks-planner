package com.mastercloudapps.thesocialnetworkplanner.api.resource.repository;

import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.Resource;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ResourceRepository extends JpaRepository<Resource, Long> {
}
