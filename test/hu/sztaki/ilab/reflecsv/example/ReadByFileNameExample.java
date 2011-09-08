package  hu.sztaki.ilab.reflecsv.example;

import hu.sztaki.ilab.reflecsv.CsvReader;

public class ReadByFileNameExample {

  private static class Record {
    public int field1;
    public String field2;
  }

  public static void main(String args[]) {
    String fileName = args[0];

    CsvReader csvReader = null;
    try {
      csvReader = new CsvReader(fileName, ',');
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    }

    Record record = (Record) csvReader.registerClass(Record.class);

    csvReader.start();
    while (csvReader.hasNext()) {
      csvReader.next();
      System.out.println("field1 = " + record.field1);
      System.out.println("field2 = " + record.field2);
    }
  }
}
