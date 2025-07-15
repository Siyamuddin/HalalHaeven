package com.pm.unitalk.Model;

public class CloudinaryImage {
    private String url;
    private String publicId;

    public CloudinaryImage(String url, String publicId) {
        this.url = url;
        this.publicId = publicId;
    }

    public String getUrl() { return url; }
    public String getPublicId() { return publicId; }
}
