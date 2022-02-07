import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * It is used to save all the output information into a log file
 * Add <code> LogHandler logHandler = new LogHandler(); </code> at line 88 in SystemMain.java
 */
public class LogHandler implements Observer {
    private static List<String> cache = new ArrayList<>();

    public LogHandler() {
        EventBus.subscribeTo(EventBus.EV_SHOW, this);
    }

    @Override
    public void update(Observable event, Object param) {
        writeFile((String) param);
    }

    public static void writeFile(String str) {
        FileWriter writer;
        try {
            writer = new FileWriter("log.txt", true);
            writer.write(str + "\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
