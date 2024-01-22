package edu.lysak.qrcodeapi.service;

import edu.lysak.qrcodeapi.domain.ImageType;
import edu.lysak.qrcodeapi.domain.QRCodeCorrection;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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

    public void validateContent(String contents) {
        if (!StringUtils.hasText(contents)) {
            throw new IllegalArgumentException("Contents cannot be null or blank");
        }
    }

    public void validateQRCodeCorrection(String correction) {
        QRCodeCorrection qrCodeCorrection = QRCodeCorrection.fromValue(correction);
        if (qrCodeCorrection == QRCodeCorrection.UNKNOWN) {
            throw new IllegalArgumentException("Permitted error correction levels are L, M, Q, H");
        }
    }
}
