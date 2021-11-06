package com.mastercloudapps.thesocialnetworkplanner.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class ToJsonString {

    public String toJsonString(){
        String jsonString = null;
        try {
            jsonString = new ObjectMapper().writeValueAsString(this);
        } catch(JsonProcessingException e) {
            log.error(e);
        }
        return jsonString;
    }
}
