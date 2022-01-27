
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class csvWriter {
    public static void writeCsv(List<String[]> dataStream) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter("dataAA.csv"));
        csvWriter.writeAll(dataStream);
        csvWriter.flush();
    }

}
