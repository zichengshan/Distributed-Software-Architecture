
import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class csvWriter {
    /**
     * writeCsv() method is used to write all the data to OutputB.csv file
     * @param dataStream
     * @throws IOException
     */
    public static void writeCsv(List<String[]> dataStream) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter("OutputB.csv"));
        csvWriter.writeAll(dataStream);
        csvWriter.flush();
    }

    /**
     * writeWildJumpCsv() method is used to write all the wild jump points of altitude
     * @param dataStream
     * @throws IOException
     */
    public static void writeWildJumpCsv(List<String[]> dataStream) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter("WildPoint.csv"));
        csvWriter.writeAll(dataStream);
        csvWriter.flush();
    }
}
