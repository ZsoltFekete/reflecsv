package  hu.sztaki.ilab.reflecsv.example;

import hu.sztaki.ilab.reflecsv.CsvReader;
import hu.sztaki.ilab.reflecsv.Name;
import java.io.FileNotFoundException;

public class Example {

  private static class Record implements Cloneable {
    @Name("field1")
    public int field1_;
    public String field2;
  }

  public static void main(String args[]) {
    String fileName = args[0];

    java.io.Reader fileReader = null;
    try {
      fileReader = new java.io.FileReader(fileName);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
      System.exit(1);
    }

    CsvReader csvReader = new CsvReader(fileReader, ',');

    Record record = csvReader.registerClass(Record.class);

    while (csvReader.hasNext()) {
      csvReader.next();
      System.out.println("field1 = " + record.field1_);
      System.out.println("field2 = " + record.field2);
    }
  }
}
