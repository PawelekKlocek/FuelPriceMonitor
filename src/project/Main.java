package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.List;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static String url = "https://www.autocentrum.pl/paliwa/ceny-paliw/malopolskie/";


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Ceny Paliw Live");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JTextArea outputTextArea = new JTextArea();
        JTextArea outputText = new JTextArea();
        JLabel imageLabel = new JLabel();

        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        JButton fetchDataButton = new JButton("Pobierz dane");
        JButton wykresyButton = new JButton("wykresy cen");
        fetchDataButton.addActionListener(e -> checkPB());
        wykresyButton.addActionListener(e -> wykresy(outputText));

        mainPanel.add(fetchDataButton, BorderLayout.NORTH);
        mainPanel.add(wykresyButton, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(imageLabel, BorderLayout.WEST);

        frame.getContentPane().add(mainPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
    private static void checkPB() {
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


    private static void wykresy(JTextArea textArea){

    }
}
