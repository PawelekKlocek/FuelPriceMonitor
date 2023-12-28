package project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    private static final String URL = "https://kantor.live/kantory/krakow";


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> createAndShowGUI());
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Kantory Live App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JTextArea outputTextArea = new JTextArea();
        JTextArea outputText = new JTextArea();

        outputTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(outputTextArea);

        JButton fetchDataButton = new JButton("Pobierz dane");
        fetchDataButton.addActionListener(e -> fetchData(outputTextArea));
        JButton wykresyButton = new JButton("wykresy walut");
        fetchDataButton.addActionListener(e -> fetchData(outputText));
        wykresyButton.addActionListener(e -> wykresy(outputText));

        mainPanel.add(fetchDataButton, BorderLayout.NORTH);
        mainPanel.add(wykresyButton, BorderLayout.SOUTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        frame.getContentPane().add(mainPanel);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static void fetchData(JTextArea outputTextArea) {
        try {
            Connection.Response response = Jsoup.connect(URL).execute();
            logger.info("Nawiązywanie połączenia...");
            if (response.statusCode() == 200) {
                Document document = response.parse();
                Elements aElements = document.select("td.border-0.align-middle.td-title a.kantor-name");
                logger.info("Odczytanie nazw kantorów ze strony");
                Elements currencyRateElementsNames = document.select("td.border-0.align-middle.currency-grid-rates div.grid-item.grid-item");
                Elements currencyRateElements = document.select("td.border-0 align-middle currency-grid-rates grid-item grid-item");
                Elements currencyRateElementsRight= document.select("td.border-0 align-middle currency-grid-rates grid-item grid-item-right");

                StringBuilder result = new StringBuilder();
                for (Element aElement : aElements) {
                    result.append("Strona internetowa: ").append(aElement.attr("href")).append("\n");
                    result.append("Nazwa: ").append(aElement.text()).append("\n");
                    result.append("USD: ").append(currencyRateElementsNames.text()).append("\n");
                    result.append("USD: ").append(currencyRateElements.text()).append("\n");
                    result.append("USD: ").append(currencyRateElementsRight.text()).append("\n\n");
                outputTextArea.setText(result.toString());
            }} else {
                outputTextArea.setText("Błąd podczas pobierania strony. Kod odpowiedzi: " + response.statusCode());
                logger.error("Błąd podczas pobierania strony");
            }
        } catch (IOException e) {
            outputTextArea.setText("Błąd podczas pobierania strony:\n" + e.getMessage());
            logger.error("Błąd podczas pobierania strony");
        }
    }private static void wykresy(JTextArea textArea){

    }
}
