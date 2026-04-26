package com.event.events.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;

@Service
public class ImageService {

    private final WebClient webClient = WebClient.create();

    @Value("${cloudinary.cloud-name}")
    private String cloudName;

    public String getBlurDataURL(String url) {
        if (url == null || url.isEmpty()) return null;

        if (cloudName == null || cloudName.isEmpty()) {
            throw new RuntimeException("Missing CLOUDINARY_CLOUD_NAME in environment variables.");
        }

        String prefix = "https://res.cloudinary.com/" + cloudName + "/image/upload/";
        String[] parts = url.split(prefix);

        if (parts.length < 2) {
            System.out.println("Invalid Cloudinary URL provided: " + url);
            return null;
        }

        String suffix = parts[1];

        String transformedUrl = prefix + "w_100,e_blur:5000,q_auto,f_auto/" + suffix;

        byte[] imageBytes = webClient.get()
                .uri(transformedUrl)
                .retrieve()
                .bodyToMono(byte[].class)
                .block();

        if (imageBytes == null) {
            throw new RuntimeException("Failed to fetch blurred image");
        }

        String base64 = Base64.getEncoder().encodeToString(imageBytes);

        return "data:image/png;base64," + base64;
    }
}
