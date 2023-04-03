package com.productdock.kafka;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Callable;

public class KafkaFileUtil {

    public static Callable<Boolean> ifFileExists(String testFile) {
        return () -> {
            File f = new File(testFile);
            return f.isFile();
        };
    }

    public static Object getMessageFrom(String testFile) throws IOException, ClassNotFoundException {
        FileInputStream fileInputStream = new FileInputStream(testFile);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        var bookRatingMessage = objectInputStream.readObject();
        objectInputStream.close();
        return bookRatingMessage;
    }
}
