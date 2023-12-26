package project;
import org.apache.logging.log4j.*;

public class Main {
    private static Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {

        logger.info("info");
        logger.error("error");
    }
}
