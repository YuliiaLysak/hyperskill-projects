package edu.lysak.qrcodeapi.controller;

import edu.lysak.qrcodeapi.service.ImageConverter;
import edu.lysak.qrcodeapi.service.ImageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.image.BufferedImage;

@RestController
public class QRCodeController {

    private final ImageService imageService;
    private final ImageConverter imageConverter;

    public QRCodeController(ImageService imageService, ImageConverter imageConverter) {
        this.imageService = imageService;
        this.imageConverter = imageConverter;
    }

    @GetMapping("/api/health")
    public ResponseEntity<Void> health() {
        return ResponseEntity.ok().build();
    }

    // Spring Boot serialization (with HttpMessageConverter<BufferedImage> bean)
    @GetMapping("/api/qrcode")
    public ResponseEntity<BufferedImage> generateQRCode() {
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(imageService.createImage(250, 250));
    }

    // Custom serialization
    @GetMapping("/api/qrcode-bytes")
    public ResponseEntity<byte[]> generateQRCode2() {
        BufferedImage image = imageService.createImage(250, 250);
        return ResponseEntity
            .ok()
            .contentType(MediaType.IMAGE_PNG)
            .body(imageConverter.convertToBytes(image));
    }
}
