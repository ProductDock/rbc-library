package com.productdock.api;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import com.productdock.util.MethodUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

@RestController
@RequestMapping("/api/test")
public class RestApi {

    private final String imagePath = "./src/main/resources/qrcodes/QRCode.png";

    @GetMapping("/generateImageQRCode")
    public String getQRCode() {
        MethodUtils.generateImageQRCode("https://www.youtube.com/", 250, 250, imagePath);
        return "QR code created";
    }

    @GetMapping("/getURL")
    public void getURL() {
        try {
            BufferedImage bf = ImageIO.read(new FileInputStream("/Users/vladimir/Documents/Projects/rbc-library/src/main/resources/qrcodes/QRCode.png"));
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(new BufferedImageLuminanceSource(bf)));
            Result result = new MultiFormatReader().decode(bitmap);
            System.out.println(result.getText());
        } catch (Exception e) {

        }
    }

    @GetMapping
    public String get() {
        return "test";
    }
}
