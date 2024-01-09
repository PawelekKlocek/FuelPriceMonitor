package project;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;


public class MainController {
    public Main main;
    private ImageView chartImageView;
    @FXML
    private TextArea dataTextArea;
    public void setMain(Main main){
        this.main = main;
    }
    public void showData(String data) {
        if (dataTextArea != null) {
            dataTextArea.setText(data);
        } else {
            System.err.println("dataTextArea is null");
        }
    }
    public void displayChart1(ActionEvent event){
        try {
            BufferedImage chart1 = main.fetchChart("chart1");

            if (chart1 != null) {
                displayChart(chart1);
            } else {
                System.err.println("Failed to load chart1");
            }
        } catch (IOException e) {
            System.err.println("Error while fetching chart1: " + e.getMessage());
        }
    }
    public void displayChart(BufferedImage chartImage) {
        if (chartImageView != null) {
            Image fxImage = SwingFXUtils.toFXImage(chartImage, null);
            chartImageView.setImage(fxImage);
        } else {
            System.err.println("chartImageView is null");
        }
    }


    public void pB95(ActionEvent event) {
        if (main != null) {
            showData(main.checkPB());
        }
    }
    public void pB98(ActionEvent event) {
        if (main != null) {
            showData(main.checkPB98());
        }
    }
    public void oN(ActionEvent event) {
        if (main != null) {
            showData(main.checkON());
        }
    }
    public void lPG(ActionEvent event) {
        if (main != null) {
            showData(main.checkLPG());
        }
    }
    public void Exit(ActionEvent event){
        main.exitApplication();
    }

}
