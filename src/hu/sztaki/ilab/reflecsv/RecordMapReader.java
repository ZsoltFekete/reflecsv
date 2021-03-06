package hu.sztaki.ilab.reflecsv;

import java.io.Reader;
import java.util.Map;
import java.util.HashMap;

import java.io.FileReader;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

public class RecordMapReader <ID, ID_RECORD extends IdRecord<ID>, T> {

  private Reader reader;
  private char separator;
  private T sampleRecord;
  private ID_RECORD idRecord;

  private boolean isExceptionForDuplicateId = true;

  public RecordMapReader(Reader reader, char separator,
      ID_RECORD idRecord, T sampleRecord) {
    this.reader = reader;
    this.separator = separator;
    this.sampleRecord = sampleRecord;
    this.idRecord = idRecord;
  }

  public RecordMapReader(String fileName, char separator,
      ID_RECORD idRecord, T sampleRecord)
    throws FileNotFoundException{
    this.separator = separator;
    this.sampleRecord = sampleRecord;
    this.idRecord = idRecord;
    this.reader = new FileReader(fileName);
  }

  public void setExceptionForDuplicateId() {
    isExceptionForDuplicateId = true;
  }

  public void setNoExceptionForDuplicateId() {
    isExceptionForDuplicateId = false;
  }

  public Map<ID, T> read() {
    CsvReader csvReader = new CsvReader(reader, separator);
    Map<ID, T> map = new HashMap<ID, T>();
    csvReader.registerObject(sampleRecord);
    csvReader.registerObject(idRecord);
    while (csvReader.hasNext()) {
      Object[] records = csvReader.getNextRecords();
      @SuppressWarnings("unchecked")
      ID id = ((ID_RECORD) records[1]).getId();
      if (isExceptionForDuplicateId && map.containsKey(id)) {
        throw new RuntimeException("The following id is a duplicate:" + id.toString());
      }
      @SuppressWarnings("unchecked")
      T t = (T)records[0];
      map.put(id, t);
    }
    return map;
  }
}
