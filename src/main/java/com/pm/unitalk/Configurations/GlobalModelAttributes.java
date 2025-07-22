package com.pm.unitalk.Configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalModelAttributes {

    @Autowired
    private SiteInfoConfig siteInfoConfig;

    @ModelAttribute
    public void addSiteInfo(Model model) {
        model.addAttribute("siteName", siteInfoConfig.getName());
        model.addAttribute("siteEmail", siteInfoConfig.getEmail());
        model.addAttribute("sitePhone", siteInfoConfig.getPhone());
        model.addAttribute("siteAddress", siteInfoConfig.getAddress());
        model.addAttribute("siteFacebook", siteInfoConfig.getFacebook());
        model.addAttribute("siteTwitter", siteInfoConfig.getTwitter());
        model.addAttribute("siteInstagram", siteInfoConfig.getInstagram());
    }
} 