package edu.lysak.qrcodeapi.domain;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum QRCodeCorrection {
    LOW("L"),
    MEDIUM("M"),
    QUARTILE("Q"),
    HIGH("H"),
    UNKNOWN("unknown");

    private final String value;

    public static ErrorCorrectionLevel errorCorrectionLevelOf(String correction) {
        QRCodeCorrection qrCodeCorrection = fromValue(correction);
        return switch (qrCodeCorrection) {
            case LOW -> ErrorCorrectionLevel.L;
            case MEDIUM -> ErrorCorrectionLevel.M;
            case QUARTILE -> ErrorCorrectionLevel.Q;
            case HIGH -> ErrorCorrectionLevel.H;
            case UNKNOWN -> throw new IllegalStateException();
        };
    }

    public static QRCodeCorrection fromValue(String type) {
        for (QRCodeCorrection imageType : values()) {
            if (imageType.getValue().equals(type)) {
                return imageType;
            }
        }
        return UNKNOWN;
    }
}
