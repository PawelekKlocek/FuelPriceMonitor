package project;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;


public class MainController {
    public Main main;
    @FXML
    private ImageView chartImageView;
    @FXML
    private TextArea dataTextArea;

    private String pb95Data;
    private String lpgData;
    private String onData;
    private String pb98Data;
    public MainController() {

        this.chartImageView = new ImageView();
    }
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
    public void displayChart1(ActionEvent event) {
        try {
            List<BufferedImage> chartList = main.fetchChart();

            if (chartList != null && !chartList.isEmpty()) {
                BufferedImage chart1 = chartList.get(0);
                Image chartImage = SwingFXUtils.toFXImage(chart1, null);
                chartImageView.setImage(chartImage);
            } else {
                System.err.println("Error loading chart");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPb95Data(ActionEvent event) {
        if (main != null) {
            pb95Data = main.checkPB();
        }
    }
    public void displayPB95(ActionEvent event){
        chartImageView.setImage(null);
        showData(pb95Data);
    }

    public void setPb98Data(ActionEvent event) {
        if (main != null) {
            pb98Data = main.checkPB98();
        }
    }
    public void displayPB98(ActionEvent event){
        chartImageView.setImage(null);
        showData(pb98Data);
    }
    public void setLpgDataData(ActionEvent event) {
        if (main != null) {
            lpgData = main.checkLPG();
        }
    }
    public void displayLPG(ActionEvent event){
        chartImageView.setImage(null);
        showData(lpgData);
    }
    public void setOnData(ActionEvent event) {
        if (main != null) {
            onData = main.checkON();
        }
    }
    public void displayON(ActionEvent event){
        chartImageView.setImage(null);
        showData(onData);
    }

    public void refreshData(){
        setLpgDataData(null);
        setOnData(null);
        setPb95Data(null);
        setPb98Data(null);
    }


    public void Exit(ActionEvent event){
        main.exitApplication();
    }

}
