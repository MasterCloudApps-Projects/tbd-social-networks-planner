package com.mastercloudapps.thesocialnetworkplanner.api.schedule;

public interface Schedulable {

    boolean shouldPost();
    void accept(Visitor visitor);
}
