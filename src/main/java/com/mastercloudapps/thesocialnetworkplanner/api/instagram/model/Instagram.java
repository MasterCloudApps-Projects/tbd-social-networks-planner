package com.mastercloudapps.thesocialnetworkplanner.api.instagram.model;

import com.mastercloudapps.thesocialnetworkplanner.api.resource.model.Resource;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.Date;
import java.util.List;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
@Entity
public class Instagram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long instagramId;
    private String text;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "instagram")
    private List<Resource> resource;
    private String username;
    private Date scheduledDate;
    private Date creationDate;
    private Date updateDate;
}