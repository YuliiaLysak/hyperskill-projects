package edu.lysak.qrcodeapi.service;

import edu.lysak.qrcodeapi.domain.ImageType;
import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    public void validateSize(int size) {
        if (size < 150 || size > 350) {
            throw new IllegalArgumentException("Image size must be between 150 and 350 pixels");
        }
    }

    public void validateImageType(String type) {
        ImageType imageType = ImageType.fromValue(type);
        if (imageType == ImageType.UNKNOWN) {
            throw new IllegalArgumentException("Only png, jpeg and gif image types are supported");
        }
    }
}
