package com.pm.unitalk.Configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SiteInfoConfig {
    @Value("${site.name}")
    private String name;

    @Value("${site.email}")
    private String email;

    @Value("${site.phone}")
    private String phone;

    @Value("${site.address}")
    private String address;

    @Value("${site.facebook}")
    private String facebook;

    @Value("${site.twitter}")
    private String twitter;

    @Value("${site.instagram}")
    private String instagram;

    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public String getAddress() { return address; }
    public String getFacebook() { return facebook; }
    public String getTwitter() { return twitter; }
    public String getInstagram() { return instagram; }
} 