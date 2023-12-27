package project;
import org.apache.logging.log4j.*;

public class Main {
    private static final Logger logger = LogManager.getLogger(Main.class);
    public static void main(String[] args) {
        System.out.println("programik");
        logger.info("info");
        logger.error("error");
        logger.fatal("fatal");
        logger.warn("warn");
        logger.debug("debug");


    }
}
