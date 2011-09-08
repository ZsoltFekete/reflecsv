package hu.sztaki.ilab.reflecsv;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class RecordReader <T> {

  private Reader reader;
  private char separator;
  private T sampleRecord;

  public RecordReader(Reader reader, char separator, T sampleRecord) {
    this.reader = reader;
    this.separator = separator;
    this.sampleRecord = sampleRecord;
  }

  List<T> read() {
    CsvReader csvReader = new CsvReader(reader, ',');
    List<T> list = new ArrayList<T>();
    csvReader.registerObject(sampleRecord);
    csvReader.start();
    while (csvReader.hasNext()) {
      csvReader.next();
      list.add(cloneObject(sampleRecord));
    }
    return list;
  }
  
  T cloneObject(T t) {
    try {
      @SuppressWarnings("unchecked")
      T result = (T) t.getClass().getMethod("clone").invoke(t);
      return result;
    } catch (java.lang.IllegalAccessException e) {
      e.printStackTrace();
    } catch (java.lang.NoSuchMethodException e) {
      e.printStackTrace();
    } catch (java.lang.reflect.InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
}
