package project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class Main extends Application {
     public void start(Stage primaryStage) throws Exception {

         FXMLLoader loader =new  FXMLLoader(getClass().getResource("Main.fxml"));
         Parent root = loader.load();
         MainController controller = loader.getController();
         controller.setMain(this);
         Scene scene = new Scene(root, 1000, 725);
         primaryStage.setScene(scene);
         primaryStage.setTitle("Ceny Paliw Live");
         primaryStage.show();
    }
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static String url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/";
    public static void main(String[] args) {
//        SwingUtilities.invokeLater(() -> createAndShowGUI());
        launch(args);
    }
    protected void exitApplication(){
        Platform.exit();
    }


    public static BufferedImage fetchChart(String whichchart) throws IOException {
        url = "https://www.e-petrol.pl/notowania/rynek-krajowy/ceny-stacje-paliw";
        String savePath = "charts\\";

        Connection.Response response = Jsoup.connect(url).execute();
        logger.info("Nawiązywanie połączenia...");

        if (response.statusCode() == 200) {
            logger.info("Nawiązano połączenie");
            Document document = response.parse();
            Elements cardBodyElements = document.select(".card-body");

            for (int i = 0; i < cardBodyElements.size(); i++) {
                Elements imgElements = cardBodyElements.get(i).select("img");

                for (int j = 0; j < imgElements.size(); j++) {
                    String imgUrl = imgElements.get(j).attr("src");
                    downloadImage(imgUrl, savePath + "chart" + i + "_" + j + ".png");
                }
            }
            BufferedImage chart1 = ImageIO.read(new File("charts\\chart6_0.png"));
            logger.info("utworzono plik chart6_0.png");
            BufferedImage chart2 = ImageIO.read(new File("charts\\chart7_0.png"));
            logger.info("utworzono plik chart7_0.png");
            BufferedImage chart3 = ImageIO.read(new File("charts\\chart8_0.png"));
            logger.info("utworzono plik chart8_0.png");


            if (whichchart.equals("chart1")){return chart1;}
            else if (whichchart.equals("chart2")){return chart2;}
            else if (whichchart.equals("chart3")){return chart3;}
            else {logger.warn("nie wybrano poprawnego wykresu! Wybierz chart 1-3");}
        }
        logger.error("Nie załadowano zdjęć");
        return null;

    }

    private static void downloadImage(String imgUrl, String savePath) throws IOException {
        URL url = new URL(imgUrl);
        try (OutputStream out = new FileOutputStream(savePath)) {
            out.write(Jsoup.connect(imgUrl).ignoreContentType(true).execute().bodyAsBytes());
            logger.info("Obraz został zapisany w: " + savePath);
        }
    }

    public static String checkPB() {
        url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/pb/";
        try {
            Connection.Response response = Jsoup.connect(url).execute();
            logger.info("Nawiązywanie połączenia...");
            if (response.statusCode() == 200) {
                logger.info("Nawiązano połączenie");
                Document document = response.parse();

                Elements names = document.select(".address");
                Elements addresses = document.select(".name.shorter");
                Elements prices = document.select(".petrol.pb");


                StringBuilder result = new StringBuilder();
                for(int i = 0; i < names.size(); i++){
                    result.append("Stajca: ").append(names.get(i).text()).append("\n");
                    result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                    result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.pb").get(i).text().trim()).append("\n");
                    result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                    result.append("\n");
                }
//                JFrame newDataFrame = new JFrame("Dane dla PB 95");
//                JTextArea newDataTextArea = new JTextArea(result.toString());
//                JScrollPane newDataScrollPane = new JScrollPane(newDataTextArea);
//                newDataFrame.getContentPane().add(newDataScrollPane);
//                newDataFrame.setSize(600, 500);
//                newDataFrame.setLocationRelativeTo(null);
//                newDataFrame.setVisible(true);
                    return result.toString();

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
        return null;
    }
    public static String checkPB98() {
        url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/pb-premium/";
        try {
            Connection.Response response = Jsoup.connect(url).execute();
            logger.info("Nawiązywanie połączenia...");
            if (response.statusCode() == 200) {
                logger.info("Nawiązano połączenie");
                Document document = response.parse();

                Elements names = document.select(".address");
                Elements addresses = document.select(".name.shorter");
                Elements prices = document.select(".petrol.pbp");


                StringBuilder result = new StringBuilder();
                for(int i = 0; i < names.size(); i++){
                    result.append("Stajca: ").append(names.get(i).text()).append("\n");
                    result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                    result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.pbp").get(i).text().trim()).append("\n");
                    result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                    result.append("\n");
                }
                return result.toString();

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
        return null;
    }
    public static String checkON() {
        url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/on/";
        try {
            Connection.Response response = Jsoup.connect(url).execute();
            logger.info("Nawiązywanie połączenia...");
            if (response.statusCode() == 200) {
                logger.info("Nawiązano połączenie");
                Document document = response.parse();

                Elements names = document.select(".address");
                Elements addresses = document.select(".name.shorter");
                Elements prices = document.select(".petrol.on");


                StringBuilder result = new StringBuilder();
                for(int i = 0; i < names.size(); i++){
                    result.append("Stajca: ").append(names.get(i).text()).append("\n");
                    result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                    result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.on").get(i).text().trim()).append("\n");
                    result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                    result.append("\n");
                }
                return result.toString();

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
        return null;
    }
    public static String checkLPG() {
        url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/lpg/";
        try {
            Connection.Response response = Jsoup.connect(url).execute();
            logger.info("Nawiązywanie połączenia...");
            if (response.statusCode() == 200) {
                logger.info("Nawiązano połączenie");
                Document document = response.parse();

                Elements names = document.select(".address");
                Elements addresses = document.select(".name.shorter");
                Elements prices = document.select(".petrol.lpg");


                StringBuilder result = new StringBuilder();
                for(int i = 0; i < names.size(); i++){
                    result.append("Stajca: ").append(names.get(i).text()).append("\n");
                    result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                    result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.lpg").get(i).text().trim()).append("\n");
                    result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                    result.append("\n");
                }
                return result.toString();

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
        return null;
    }


}