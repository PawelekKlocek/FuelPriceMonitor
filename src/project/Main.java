package project;

import javafx.application.Application;
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
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;

public class Main extends Application {
     public void start(Stage primaryStage) throws Exception {

         Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
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

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Ceny Paliw Live");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(Color.BLACK);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(Color.BLACK);

        JTextArea outputTextArea = new JTextArea();
        JTextArea outputText = new JTextArea();
        JLabel imageLabel = new JLabel();



        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        JButton fetchDataButtonPB95 = new JButton(new ImageIcon("resources\\pb95.png"));
        JButton fetchDataButtonPB98 = new JButton(new ImageIcon("resources\\pb98r.png"));
        JButton fetchDataButtonON = new JButton(new ImageIcon("resources\\ON.png"));
        JButton fetchDataButtonLPG = new JButton(new ImageIcon("resources\\LPG.png"));
        JButton wykresyButton = new JButton(new ImageIcon("resources\\wykres.jpg"));

        // Ustawienie preferowanego rozmiaru dla przycisków
        Dimension buttonSize = new Dimension(100, 100);
        fetchDataButtonPB95.setPreferredSize(buttonSize);
        fetchDataButtonPB98.setPreferredSize(buttonSize);
        fetchDataButtonON.setPreferredSize(buttonSize);
        fetchDataButtonLPG.setPreferredSize(buttonSize);
        wykresyButton.setPreferredSize(buttonSize);

        fetchDataButtonPB95.addActionListener(e -> checkPB());
        fetchDataButtonPB98.addActionListener(e -> checkPB98());
        fetchDataButtonON.addActionListener(e -> checkON());
        fetchDataButtonLPG.addActionListener(e -> checkLPG());
        wykresyButton.addActionListener(e -> {
            try {
                fetchChart();
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        });

        // Ustawienie layoutu dla panelu przycisków
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 5, 5));
        buttonPanel.add(fetchDataButtonPB95);
        buttonPanel.add(fetchDataButtonPB98);
        buttonPanel.add(fetchDataButtonON);
        buttonPanel.add(fetchDataButtonLPG);
        buttonPanel.add(wykresyButton);

        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(imageLabel, BorderLayout.WEST);

        frame.getContentPane().add(mainPanel);
        frame.setSize(1000, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }


    private static void fetchChart() throws IOException {
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
            BufferedImage chart2 = ImageIO.read(new File("charts\\chart7_0.png"));
            BufferedImage chart3 = ImageIO.read(new File("charts\\chart8_0.png"));

            // Swing elements: !
            JLabel pbLabel = new JLabel(new ImageIcon(chart1));
            JLabel onLabel = new JLabel(new ImageIcon(chart2));
            JLabel LPGlabel = new JLabel(new ImageIcon(chart3));
            JFrame newDataFrame = new JFrame("Wykres paliwa PB");
            newDataFrame.setSize(1000, 1000);
            newDataFrame.add(pbLabel);
            newDataFrame.add(onLabel);
            newDataFrame.add(LPGlabel);
            newDataFrame.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));
            newDataFrame.setLocationRelativeTo(null);
            newDataFrame.setVisible(true);
        }
    }

    private static void downloadImage(String imgUrl, String savePath) throws IOException {
        URL url = new URL(imgUrl);
        try (OutputStream out = new FileOutputStream(savePath)) {
            out.write(Jsoup.connect(imgUrl).ignoreContentType(true).execute().bodyAsBytes());
            logger.info("Obraz został zapisany w: " + savePath);
        }
    }

    public static void checkPB() {
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

                JFrame newDataFrame = new JFrame("Dane dla PB 95");
                JTextArea newDataTextArea = new JTextArea(result.toString());
                JScrollPane newDataScrollPane = new JScrollPane(newDataTextArea);
                newDataFrame.getContentPane().add(newDataScrollPane);
                newDataFrame.setSize(600, 500);
                newDataFrame.setLocationRelativeTo(null);
                newDataFrame.setVisible(true);

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
    }
    public static void checkPB98() {
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

                JFrame newDataFrame = new JFrame("Dane dla PB 98");
                JTextArea newDataTextArea = new JTextArea(result.toString());
                JScrollPane newDataScrollPane = new JScrollPane(newDataTextArea);
                newDataFrame.getContentPane().add(newDataScrollPane);
                newDataFrame.setSize(600, 500);
                newDataFrame.setLocationRelativeTo(null);
                newDataFrame.setVisible(true);

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
    }
    public static void checkON() {
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

                JFrame newDataFrame = new JFrame("Dane dla ON");
                JTextArea newDataTextArea = new JTextArea(result.toString());
                JScrollPane newDataScrollPane = new JScrollPane(newDataTextArea);
                newDataFrame.getContentPane().add(newDataScrollPane);
                newDataFrame.setSize(600, 500);
                newDataFrame.setLocationRelativeTo(null);
                newDataFrame.setVisible(true);

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
    }
    public static void checkLPG() {
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

                JFrame newDataFrame = new JFrame("Dane dla LPG");
                JTextArea newDataTextArea = new JTextArea(result.toString());
                JScrollPane newDataScrollPane = new JScrollPane(newDataTextArea);
                newDataFrame.getContentPane().add(newDataScrollPane);
                newDataFrame.setSize(600, 500);
                newDataFrame.setLocationRelativeTo(null);
                newDataFrame.setVisible(true);

            }
            else{
                logger.error("Nie udało się pobrac danych");
            }
        } catch (IOException e) {
            logger.error("Błąd podczas pobierania strony");
        }
    }


}