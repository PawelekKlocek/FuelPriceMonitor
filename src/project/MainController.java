package project;
import javafx.event.ActionEvent;

public class MainController {
    public Main main;
    public void setMain(Main main){
        this.main = main;
    }


    public void pB95(ActionEvent event) {
        if (main != null) {
            main.checkPB();
        }
    }
    public void pB98(ActionEvent event) {
        if (main != null) {
            main.checkPB98();
        }
    }
    public void oN(ActionEvent event) {
        if (main != null) {
            main.checkON();
        }
    }
    public void lPG(ActionEvent event) {
        if (main != null) {
            main.checkLPG();
        }
    }

}
