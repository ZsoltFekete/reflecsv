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
      Method method = t.getClass().getMethod("clone");
      method.setAccessible(true);
      @SuppressWarnings("unchecked")
      T result = (T) method.invoke(t);
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
