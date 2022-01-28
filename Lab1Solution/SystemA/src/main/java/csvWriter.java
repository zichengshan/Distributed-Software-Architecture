
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class csvWriter {
    /**
     * writeCsv() method is used to write data stream info into the csv file
     * @param dataStream
     * @throws IOException
     */
    public static void writeCsv(List<String[]> dataStream) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter("OutputA.csv"));
        csvWriter.writeAll(dataStream);
        csvWriter.flush();
    }
}
