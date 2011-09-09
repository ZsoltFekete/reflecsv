package  hu.sztaki.ilab.reflecsv.example;

import hu.sztaki.ilab.reflecsv.RecordMapReader;
import hu.sztaki.ilab.reflecsv.IdRecord;
import java.io.FileNotFoundException;
import java.util.Map;

public class RecordMapReaderByFileNameExample {

  private static class Record {
    public double field3;
    public String field2;
    public Record clone() {
      Record copy = new Record();
      copy.field2 = field2;
      copy.field3 = field3;
      return copy;
    }
  }

  private static class Id implements IdRecord<Integer> {
    public Integer field1;
    public Integer getId() {
      return field1;
    }
  }

  public static void main(String args[]) {
    String fileName = args[0];

    RecordMapReader<Integer, Id, Record> recordReader = null;
    try {
      recordReader = new RecordMapReader<Integer, Id, Record>(fileName,
          ',', new Id(), new Record());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }
  
    Map<Integer, Record> records = recordReader.read();
    System.out.println("size=" + records.size());
    System.out.println("1->" + records.get(1).field2 + "," +
        records.get(1).field3);
    System.out.println("-3->" + records.get(-3).field2 + "," +
        records.get(-3).field3);
  }
}
