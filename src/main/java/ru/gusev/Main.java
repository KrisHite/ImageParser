package ru.gusev;

import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\alexg\\IdeaProjects\\ImageParser\\src\\main\\resources\\chromedriver-win64\\chromedriver.exe");
        //Объекты классов
        ChromeOptions options = new ChromeOptions();
        Scanner scanner = new Scanner(System.in);
        //Настройка Selenium
        options.addArguments("--headless=new");//Безголовый режим
        options.addArguments("--disable-gpu");
        options.addArguments("--window-size=1920,1080");
        //Запуск Selenium
        WebDriver driver = new ChromeDriver(options);
        //Переменные
        String wetTarget;
        String targetURL = "https://yandex.ru/images/search?text=";
        String dirPath = "D:\\Image";
        String finalDirPath;
        int countOfImage, width, heigh;

        File dir = new File(dirPath);
        if(!dir.exists()){
            if(!dir.mkdir()){
                System.out.println("Не удается создать каталог!");
                return;
            }
        }

        System.out.println("Это парсер картинок с яндекс.");
        System.out.println("Введите запрос запрос: ");
        wetTarget = scanner.nextLine();

        finalDirPath = dirPath + "\\" + wetTarget;

//        File finalDir = new File(finalDirPath);
//        if(!finalDir.exists()){
//            //boolean flag = finalDir.mkdir();
//            if(!finalDir.mkdir()){
//                System.out.println("Не удается создать каталог!");
//                return;
//            }
//            System.out.println("Каталог: " + finalDirPath + " создан.");
//        }

        //Преобразуем target для URL
        String target = wetTarget.replaceAll(" ", "+");
        //
        System.out.println("Введите колличество изображений для парсинга: ");
        countOfImage = scanner.nextInt();
        System.out.println("Введите желаемый размер изображения через пробел или введите нули, чтобы скачать любой размер: ");
        width = scanner.nextInt();
        heigh = scanner.nextInt();
        //
        System.out.println("Начинаю поиск по запросу: " + target);
        //
        File finalDir = new File(finalDirPath);
        if(!finalDir.exists()){
            if(!finalDir.mkdir()){
                System.out.println("Не удается создать каталог!");
                return;
            }
            System.out.println("Каталог: " + finalDirPath + " создан.");
        }
        //
        SaverImage save = new SaverImage(finalDirPath);
        //Поиск изображений
        try {
            if(width != 0 && heigh != 0){
                driver.get(targetURL + target + "&isize=eq&iw=" + width + "&ih=" + heigh);
            }else{
                driver.get(targetURL + target);
            }

            Thread.sleep(3000);
            try {
                WebElement firstElem = driver.findElement(By.cssSelector("a.ImagesContentImage-Cover"));
                firstElem.click();
            } catch (NoSuchElementException e) {
                System.out.println("Не найдено ни одного изображения по запросу!");
                e.getMessage();
            }
            //
            WebElement fullImageEllement;
            WebElement nextButton;
            String imageUrl;
            //
            for (int i = 0; i < countOfImage; i++) {
                try {
                    fullImageEllement = driver.findElement(By.cssSelector("img.MMImage-Origin"));
                    imageUrl = fullImageEllement.getAttribute("src");
                    save.SaveImage(imageUrl, i);
                    nextButton = driver.findElement(By.cssSelector("button[aria-label=\"Следующая картинка\"]"));
                    nextButton.click();
                    Thread.sleep(500);
                } catch (StaleElementReferenceException e) {
                    i--;
                } catch (NullPointerException e) {
                    e.getMessage();
                    break;
                } catch (NoSuchElementException e) {
                    e.getMessage();
                    System.out.println("Достигнут конец галереи!");
                    break;
                }
            }
            System.out.println("Работа окончена. Результат сохранен в: " + finalDirPath);
            driver.quit();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

