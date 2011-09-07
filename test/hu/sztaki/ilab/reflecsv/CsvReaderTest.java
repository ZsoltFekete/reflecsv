package hu.sztaki.ilab.reflecsv;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.io.Reader;
import java.io.StringReader;

public class CsvReaderTest extends TestCase {

  @Override
  protected void setUp() {
  }

  @Override
  protected void tearDown() {
  }

  public void testEmpty() {
    Integer i = 5;
    assertNotNull(i);
    int j = 5;
    assertTrue(i == j);
    try {
      i = null;
      i.toString();
      fail("Allows nullpointer");
    } catch (NullPointerException e) {}
  }

  public void testConstructor() {
    Reader stringReader = new StringReader("");
    CsvReader csvReader = new CsvReader(stringReader, ',');
  }

  private static class Record1 {
    public int field1;
    public String field2;
  }

  private static class Record2 {
    public String field2;
    public double field3;
  }

  public void testRegisterClass() {
    Reader stringReader = new StringReader("");
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);
    Record2 record2 = (Record2) csvReader.registerClass(Record2.class);
//    Record1 record1 = csvReader.registerClass();
    assertNotNull(record1);
    assertTrue(record1 instanceof Record1);
    assertNotNull(record2);
    assertTrue(record2 instanceof Record2);
  }

  public void testRun() {
    String inputString =
      "field1,field2,field3\n" +
      "1,qwe,3.4\n" +
      "-3,asd,-4.5\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);
    Record2 record2 = new Record2();
    csvReader.registerObject(record2);
    csvReader.start();
    assertTrue(csvReader.hasNext());
    csvReader.next();
    assertEquals(1, record1.field1);
    assertEquals("qwe", record1.field2);
    assertEquals("qwe", record2.field2);
    assertEquals(3.4, record2.field3);
    assertTrue(csvReader.hasNext());
    csvReader.next();
    assertEquals(-3, record1.field1);
    assertEquals("asd", record1.field2);
    assertEquals("asd", record2.field2);
    assertEquals(-4.5, record2.field3);
    assertFalse(csvReader.hasNext());
  }

  public void testNotFound() {
    String inputString =
      "field1_,field2,field3\n" +
      "1,qwe,3.4\n" +
      "-3,asd,-4.5\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);
    try {
      csvReader.start();
    fail("Excepion was not arised.");
    } catch (RuntimeException e) {}
  }

  public void testWhileLoop() {
    String inputString =
      "field1,field2,field3\n" +
      "1,qwe,3.4\n" +
      "-3,asd,-4.5\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);

    int counter = 0;
    csvReader.start();
    while (csvReader.hasNext()) {
      csvReader.next();
      if (0 == counter) {
        assertEquals(1, record1.field1);
      }
      if (1 == counter) {
        assertEquals(-3, record1.field1);
      }
      ++counter;
    }
    assertEquals(2, counter);
  }

  public void testNoEndlineAtTheEnd() {
    String inputString =
      "field1,field2,field3\n" +
      "1,qwe,3.4\n" +
      "-3,asd,-4.5";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);

    int counter = 0;
    csvReader.start();
    while (csvReader.hasNext()) {
      csvReader.next();
      ++counter;
    }
    assertEquals(2, counter);
  }

  public void testEmptyFile() {
    String inputString = "";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);

    int counter = 0;
    try {
      csvReader.start();
      fail("Exception should have happened.");
    } catch (RuntimeException e) {}
  }

  public void testOwnStringHandler() {
    String inputString =
      "field1,field2,field3\n" +
      "1,qwe,3.4\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);

    class MyStringHandler implements ObjectHandler {
      public Object convert(String s) {
        return "test_prefix_" + s;
      }
    }
    csvReader.setObjectHandler(String.class, new MyStringHandler());

    int counter = 0;
    csvReader.start();
    csvReader.hasNext();
    csvReader.next();
    assertEquals("test_prefix_qwe", record1.field2);
  }

  static class Sub {
    public String s;
  }

  static class Record3 {
    int field1;
    Sub field2 = new Sub();
  }

  public void testNextWithoutHasNext() {
    String inputString =
      "field1,field2,field3\n" +
      "1,qwe,3.4\n" +
      "-3,asd,-4.5\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = (Record1) csvReader.registerClass(Record1.class);
    csvReader.start();
    csvReader.next();
    assertEquals("qwe", record1.field2);
    csvReader.next();
    assertEquals("asd", record1.field2);
  }

  public void testOwnClassHandler() {
    String inputString =
      "field1,field2,field3\n" +
      "1,qwe,3.4\n" +
      "1,asd,3.4\n" +
      "1,other,3.4\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    // Record3 record3 = (Record3) csvReader.registerClass(Record3.class);
    Record3 record3 = new Record3();
    csvReader.registerObject(record3);

    class MySubHandler implements ObjectHandler {
      public Sub convert(String s) {
        Sub sub = new Sub();
        sub.s = s + s;
        if (s.equals("qwe")) {
          sub.s = "hello";
        }
        if (s.equals("asd")) {
          sub.s = "world";
        }
        return sub;
      }
    }
    csvReader.setObjectHandler(Sub.class, new MySubHandler());
    csvReader.start();
    csvReader.next();
    assertEquals("hello", record3.field2.s);
    csvReader.next();
    assertEquals("world", record3.field2.s);
    csvReader.next();
    assertEquals("otherother", record3.field2.s);
  }

  public static Test suite() {
    return new TestSuite(CsvReaderTest.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }

}
