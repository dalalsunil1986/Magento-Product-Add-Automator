package mariell.csv;

import com.googlecode.jcsv.CSVStrategy;
import com.googlecode.jcsv.annotations.internal.ValueProcessorProvider;
import com.googlecode.jcsv.reader.CSVEntryParser;
import com.googlecode.jcsv.reader.CSVReader;
import com.googlecode.jcsv.reader.internal.AnnotationEntryParser;
import com.googlecode.jcsv.reader.internal.CSVReaderBuilder;

import java.io.*;
import java.util.List;

public class CsvParser {

    public static List<Entry> parse(File csvFile) throws IOException {
        Reader reader = new FileReader(csvFile);
        ValueProcessorProvider provider = new ValueProcessorProvider();
        CSVEntryParser<Entry> entryParser = new AnnotationEntryParser<Entry>(Entry.class, provider);
        CSVReader<Entry> csvEntryReader = new CSVReaderBuilder<Entry>(reader)
                .strategy(CSVStrategy.UK_DEFAULT)
                .entryParser(entryParser).build();

        try {
            return csvEntryReader.readAll();
        } finally {
            reader.close();
        }
    }
}
