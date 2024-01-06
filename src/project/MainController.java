package project;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
public class MainController {
    private Button fetchDataButtonPB95;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private ImageView imageLabel;

    public void initialize() {
        // Tutaj możesz przypisać dowolne początkowe ustawienia widoku, jeśli to konieczne
    }

    @FXML
    private void checkPB(ActionEvent event) {
        // Obsługa kliknięcia przycisku PB95
        // Możesz przenieść kod z metody checkPB() z klasy Main tutaj
        System.out.println("asdasdasd");
    }


}
