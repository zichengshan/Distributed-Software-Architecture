import java.io.IOException;
import java.util.Observable;
import java.util.Observer;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LogHandler implements Observer {
    private Logger logger;
    public LogHandler(){
        EventBus.subscribeTo(EventBus.EV_SHOW, this);
        try {
            logger = Logger.getLogger(LogHandler.class.getName());
            logger.setUseParentHandlers(false);
            FileHandler file_handler = new FileHandler("LoggerOutputA.log");
            logger.addHandler(file_handler);
            SimpleFormatter simpleformatter = new SimpleFormatter();
            file_handler.setFormatter(simpleformatter);
        } catch (IOException e){
            System.out.println("log initiating is failed");
        }
    }
    @Override
    public void update(Observable o, Object arg) {

    }
}
