package com.productdock.api;

import com.productdock.util.MethodUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test")
public class RestApi {

    private final String imagePath = "./src/main/resources/qrcodes/QRCode.png";

    @GetMapping("/generateImageQRCode")
    public String getQRCode() {
        MethodUtils.generateImageQRCode("https://productdock.com/", 250, 250, imagePath);
        return "QR code created";
    }

    @GetMapping
    public String get() {
        return "test";
    }
}
