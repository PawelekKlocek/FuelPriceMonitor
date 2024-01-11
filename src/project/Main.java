package project;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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

         ExecutorService executorService = Executors.newFixedThreadPool(4);
         List<Runnable> tasks = new ArrayList<>();
         tasks.add(() -> fetchDataPB(controller));
         tasks.add(() -> fetchDataPB98(controller));
         tasks.add(() -> fetchDataON(controller));
         tasks.add(() -> fetchDataLPG(controller));
         tasks.add(() -> {
             try {
                 fetchChart();
                 logger.info("Udało się pobrać wykresy");
             } catch (IOException e) {
                 throw new RuntimeException(e);
             }
         });

         for (Runnable task : tasks) {
             executorService.submit(task);
         }
         executorService.shutdown();



     }


    private static void fetchDataPB(MainController controller) {
        Platform.runLater(() -> controller.setPb95Data(null));
    }

    private static void fetchDataPB98(MainController controller) {
        Platform.runLater(() -> controller.setPb98Data(null));
    }

    private static void fetchDataON(MainController controller) {
        Platform.runLater(() -> controller.setOnData(null));
    }

    private static void fetchDataLPG(MainController controller) {
        Platform.runLater(() -> controller.setLpgDataData(null));
    }

    private static final Logger logger = LogManager.getLogger(Main.class);
    private static String url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/";
    public static void main(String[] args) {
        launch(args);
    }
    protected void exitApplication(){
        Platform.exit();
    }


    public static void fetchChart() throws IOException {
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

        }

    }

    private static void downloadImage(String imgUrl, String savePath) throws IOException {
        try (OutputStream out = new FileOutputStream(savePath)) {
            out.write(Jsoup.connect(imgUrl).ignoreContentType(true).execute().bodyAsBytes());
            logger.info("Obraz został zapisany w: " + savePath);
        }
    }

    public static String checkPB() {

        StringBuilder result = new StringBuilder();
        try {

            for (int page = 1; page <= 4; page++) {
                String url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/pb/strona-" + page + "/";
                Connection.Response response = Jsoup.connect(url).execute();
                if (response.statusCode() == 200) {
                    logger.info("Nawiązano połączenie ze strona " + url);
                    Document document = response.parse();

                    Elements names = document.select(".address");
                    Elements addresses = document.select(".name.shorter");
                    Elements prices = document.select(".petrol.pb");


                    for (int i = 0; i < names.size(); i++) {
                        result.append("Stajca: ").append(names.get(i).text()).append("\n");
                        result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                        result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.pb").get(i).text().trim()).append("\n");
                        result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                        result.append("\n");
                    }
                } else {
                    logger.error("Nie udało się pobrac danych");
                    return null;
                }
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
            return null;
        }
        return result.toString();

    }
    public static String checkPB98() {
        StringBuilder result = new StringBuilder();

        try {
                String url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/pb-premium/strona-1";
                Connection.Response response = Jsoup.connect(url).execute();
                if (response.statusCode() == 200) {
                    logger.info("Nawiązano połączenie ze strona " + url);
                    Document document = response.parse();

                    Elements names = document.select(".address");
                    Elements addresses = document.select(".name.shorter");
                    Elements prices = document.select(".petrol.pbp");
                    for (int i = 0; i < names.size(); i++) {
                        result.append("Stajca: ").append(names.get(i).text()).append("\n");
                        result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                        result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.pbp").get(i).text().trim()).append("\n");
                        result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                        result.append("\n");
                    }

                } else {
                    logger.error("Nie udało się pobrac danych");
                    return null;
                }

        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
            return null;
        }
        return result.toString();

    }
    public static String checkON() {
        StringBuilder result = new StringBuilder();
        try {
            for (int page = 1; page <= 4; page++) {
                String url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/on/strona-" + page + "/";
                Connection.Response response = Jsoup.connect(url).execute();
                if (response.statusCode() == 200) {
                    logger.info("Nawiązano połączenie ze strona " + url);
                    Document document = response.parse();

                    Elements names = document.select(".address");
                    Elements addresses = document.select(".name.shorter");
                    Elements prices = document.select(".petrol.on");

                    for (int i = 0; i < names.size(); i++) {
                        result.append("Stacja: ").append(names.get(i).text()).append("\n");
                        result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                        result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.on").get(i).text().trim()).append("\n");
                        result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                        result.append("\n");
                    }
                } else {
                    logger.error("Nie udało się pobrać danych dla strony " + page);
                    return null;
                }
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
            return null;

        }
        return result.toString();
    }
    public static String checkLPG() {
        StringBuilder result = new StringBuilder();
        try {
            for (int page = 1; page <= 4; page++) {
                String url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/lpg/strona-" + page + "/";
                Connection.Response response = Jsoup.connect(url).execute();
                if (response.statusCode() == 200) {
                    logger.info("Nawiązano połączenie ze strona " + url);
                    Document document = response.parse();

                    Elements names = document.select(".address");
                    Elements addresses = document.select(".name.shorter");
                    Elements prices = document.select(".petrol.lpg");


                    for(int i = 0; i < names.size(); i++){
                        result.append("Stajca: ").append(names.get(i).text()).append("\n");
                        result.append("Adres: ").append(addresses.get(i).text()).append("\n");
                        result.append("Rodzaj paliwa: ").append(prices.select(".fuel-logo.lpg").get(i).text().trim()).append("\n");
                        result.append("Cena paliwa: ").append(prices.get(i).text().trim()).append("\n");
                        result.append("\n");
                    }


                }
                else{
                    logger.error("Nie udało się pobrac danych");
                    return null;
                    }

                }
        } catch (IOException e) {
                logger.error("Błąd podczas pobierania strony");
                return null;
        }
        return result.toString();
    }


}