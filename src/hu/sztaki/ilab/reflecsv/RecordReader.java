package hu.sztaki.ilab.reflecsv;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

public class RecordReader <T> {

  private Reader reader;
  private char separator;
  private T sampleRecord;

  public RecordReader(Reader reader, char separator, T sampleRecord) {
    this.reader = reader;
    this.separator = separator;
    this.sampleRecord = sampleRecord;
  }

  public RecordReader(String fileName, char separator, T sampleRecord)
    throws FileNotFoundException{
    this.separator = separator;
    this.sampleRecord = sampleRecord;
    this.reader = new FileReader(fileName);
  }

  public List<T> read() {
    CsvReader csvReader = new CsvReader(reader, separator);
    List<T> list = new ArrayList<T>();
    csvReader.registerObject(sampleRecord);
    while (csvReader.hasNext()) {
      @SuppressWarnings("unchecked")
      T nextRecord = (T) csvReader.getNextRecord();
      list.add(nextRecord);
    }
    return list;
  }
}
