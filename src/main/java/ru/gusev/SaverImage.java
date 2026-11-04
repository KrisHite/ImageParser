package ru.gusev;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class SaverImage {

    private final String dirPath;

    SaverImage(String dirPath) {
        this.dirPath = dirPath;
    }

    public void SaveImage(String url, int countOfImage) {
        System.out.println("№" + countOfImage + " URL: " + url);
        try {//Скачивание и сохранение
            URI uri = URI.create(url);
            URL imageUrl = uri.toURL();
            BufferedImage image = ImageIO.read(imageUrl);
            File out = new File(dirPath + "\\" + countOfImage + ".jpg");
            ImageIO.write(image, "jpg", out);
            System.out.println("Сохранен.");
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            System.out.println("Недопустимый формат!");
        } catch (IOException e) {
            System.out.println("Не удалось сохранить изображение!");
        }
    }
}
