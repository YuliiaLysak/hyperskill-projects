package edu.lysak.qrcodeapi.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;

@Service
public class ImageService {

    public BufferedImage createImage(int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics = image.createGraphics();

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, width, height);

        return image;
    }

    public BufferedImage createImage(String data, int width, int height) {
        QRCodeWriter writer = new QRCodeWriter();
        try {
            BitMatrix bitMatrix = writer.encode(data, BarcodeFormat.QR_CODE, width, height);
            return MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException exception) {
            throw new RuntimeException("Failed to create QR code", exception);
        }
    }
}
