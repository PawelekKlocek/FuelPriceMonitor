package project;
import org.apache.logging.log4j.*;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        System.out.println("this is sout");
        logger.info("info");
        logger.error("error");
    }
}
