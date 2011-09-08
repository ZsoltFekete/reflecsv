package  hu.sztaki.ilab.reflecsv.example;

import hu.sztaki.ilab.reflecsv.CsvReader;

public class Example {

  private static class Record {
    public int field1;
    public String field2;
  }

  public static void main(String args[]) {
    String fileName = args[0];

    java.io.Reader fileReader = null;
    try {
      fileReader = new java.io.FileReader(fileName);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    CsvReader csvReader = new CsvReader(fileReader, ',');

    Record record = (Record) csvReader.registerClass(Record.class);

    csvReader.start();
    while (csvReader.hasNext()) {
      csvReader.next();
      System.out.println("field1 = " + record.field1);
      System.out.println("field2 = " + record.field2);
    }
  }
}
