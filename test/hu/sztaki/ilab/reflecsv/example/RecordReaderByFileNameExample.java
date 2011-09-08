package  hu.sztaki.ilab.reflecsv.example;

import hu.sztaki.ilab.reflecsv.RecordReader;
import java.util.List;

public class RecordReaderByFileNameExample {

  private static class Record {
    public int field1;
    public String field2;
    public Record clone() {
      Record copy = new Record();
      copy.field1 = field1;
      copy.field2 = field2;
      return copy;
    }
  }

  public static void main(String args[]) {
    String fileName = args[0];

    RecordReader<Record> recordReader = null;
    try {
      recordReader = new RecordReader<Record>(fileName, ',', new Record());
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }
  
    List<Record> records = recordReader.read();

    System.out.println("field1 = " + records.get(0).field1);
    System.out.println("field2 = " + records.get(0).field2);
    System.out.println("field1 = " + records.get(1).field1);
    System.out.println("field2 = " + records.get(1).field2);
  }
}
