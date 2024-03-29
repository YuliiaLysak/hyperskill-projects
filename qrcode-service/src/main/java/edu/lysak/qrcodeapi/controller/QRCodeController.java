package edu.lysak.qrcodeapi.controller;

import edu.lysak.qrcodeapi.domain.ImageType;
import edu.lysak.qrcodeapi.service.ImageConverter;
import edu.lysak.qrcodeapi.service.ImageService;
import edu.lysak.qrcodeapi.service.ValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

@RestController
@RequiredArgsConstructor
public class QRCodeController {

    private final ImageService imageService;
    private final ImageConverter imageConverter;
    private final ValidationService validationService;

    @GetMapping("/api/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }

    // Spring Boot serialization (with HttpMessageConverter<BufferedImage> bean)
    @GetMapping("/api/qrcode")
    public ResponseEntity<BufferedImage> generateQRCode(
        @RequestParam(value = "size", required = false, defaultValue = "250") int size,
        @RequestParam(value = "type", required = false, defaultValue = "png") String type,
        @RequestParam("contents") String contents,
        @RequestParam(value = "correction", required = false, defaultValue = "L") String correction
    ) {
        validationService.validateContent(contents);
        validationService.validateSize(size);
        validationService.validateQRCodeCorrection(correction);
        validationService.validateImageType(type);
        return ResponseEntity.ok()
            .contentType(ImageType.mediaTypeOf(type))
            .body(imageService.createImage(contents, size, size, correction));
    }

    // Custom serialization
    @GetMapping("/api/qrcode-bytes")
    public ResponseEntity<byte[]> generateQRCodeBytes() {
        BufferedImage image = imageService.createImage(250, 250);
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(imageConverter.convertToBytes(image));
    }
}
