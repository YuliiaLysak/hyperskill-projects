package edu.lysak.qrcodeapi.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;

@Getter
@RequiredArgsConstructor
public enum ImageType {
    PNG("png"),
    JPEG("jpeg"),
    GIF("gif"),
    UNKNOWN("unknown");

    private final String value;

    public static MediaType mediaTypeOf(String type) {
        ImageType imageType = fromValue(type);
        return switch (imageType) {
            case PNG -> MediaType.IMAGE_PNG;
            case JPEG -> MediaType.IMAGE_JPEG;
            case GIF -> MediaType.IMAGE_GIF;
            case UNKNOWN -> throw new IllegalStateException();
        };
    }

    public static ImageType fromValue(String type) {
        for (ImageType imageType : values()) {
            if (imageType.getValue().equals(type)) {
                return imageType;
            }
        }
        return UNKNOWN;
    }
}
