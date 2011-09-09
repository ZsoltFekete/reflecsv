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
    Record1 record1 = csvReader.registerClass(Record1.class);
    Record2 record2 = csvReader.registerObject(new Record2());
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
    Record1 record1 = csvReader.registerClass(Record1.class);
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
    Record1 record1 = csvReader.registerClass(Record1.class);
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
    Record1 record1 = new Record1();
    csvReader.registerObject(record1);

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

    Record1 record1 = csvReader.registerObject(new Record1());

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
    Record1 record1 = csvReader.registerClass(Record1.class);

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
    Record1 record1 = csvReader.registerClass(Record1.class);

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
    Record1 record1 = csvReader.registerClass(Record1.class);
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
    Record3 record3 = csvReader.registerObject(new Record3());

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

  static class Record {
    int int_;
    byte byte_;
    char char_;
    double double_;
    float float_;
    boolean boolean_;
    long long_;
    short short_;
  }

  public void testOwnPrimitiveHandlers() {
    String inputString =
      "int_,byte_,char_,double_,float_,boolean_,long_,short_\n" +
      "1,2,3,4,5,6,7,8\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record record = csvReader.registerClass(Record.class);

    class MyIntHandler implements IntHandler {
      public int convert(String s) {
        return 123;
      }
    }

    class MyDoubleHandler implements DoubleHandler {
      public double convert(String s) {
        return 3.45;
      }
    }

    class MyBooleanHandler implements BooleanHandler {
      public boolean convert(String s) {
        return true;
      }
    }

    class MyCharHandler implements CharHandler {
      public char convert(String s) {
        return 'q';
      }
    }

    class MyFloatHandler implements FloatHandler {
      public float convert(String s) {
        return 3.78f;
      }
    }

    class MyLongHandler implements LongHandler {
      public long convert(String s) {
        return 567;
      }
    }

    class MyShortHandler implements ShortHandler {
      public short convert(String s) {
        return 23;
      }
    }

    class MyByteHandler implements ByteHandler {
      public byte convert(String s) {
        return 114;
      }
    }

    csvReader.setIntHandler(new MyIntHandler());
    csvReader.setDoubleHandler(new MyDoubleHandler());
    csvReader.setBooleanHandler(new MyBooleanHandler());
    csvReader.setByteHandler(new MyByteHandler());
    csvReader.setFloatHandler(new MyFloatHandler());
    csvReader.setLongHandler(new MyLongHandler());
    csvReader.setShortHandler(new MyShortHandler());
    csvReader.setCharHandler(new MyCharHandler());

    csvReader.start();
    csvReader.next();
    assertEquals(123, record.int_);
    assertEquals(114, record.byte_);
    assertEquals('q', record.char_);
    assertEquals(3.45, record.double_);
    assertEquals(3.78f, record.float_);
    assertEquals(true, record.boolean_);
    assertEquals(567, record.long_);
    assertEquals(23, record.short_);

  }

  public void testNotCompleteRecord() {
    String inputString =
    "field1,field_qwe,field2,field4\n" +
      "11,qwe,s2,3.4\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = csvReader.registerClass(Record1.class);

    int counter = 0;
    csvReader.start();
    csvReader.next();
    assertEquals(11, record1.field1);
    assertEquals("s2", record1.field2);
  }

  public static class LongRecord {
    public String field2;
    public String field5;
    public String field6;
    public String field8;
    public String field10;
    public String field11;
  }

  public void testLongNotCompleteRecord() {
    String inputString =
    "field1,field2,field3,field4,field5,field6,field7,field8," +
    "field9,field10,field11,field12,field13,field14\n" +
      "1,2,3,4,5,6,7,8,9,10,11,12,13,14\n" +
      "1,2,3,4,5,6,7,8,9,10,11,12,13,14\n" +
      "1,2,3,4,5,6,7,8,9,10,11,12,13,14\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    LongRecord record = csvReader.registerClass(LongRecord.class);

    int counter = 0;
    csvReader.start();
    csvReader.next();
    csvReader.next();
    csvReader.next();
    assertEquals("2", record.field2);
    assertEquals("5", record.field5);
    assertEquals("6", record.field6);
    assertEquals("8", record.field8);
    assertEquals("10", record.field10);
    assertEquals("11", record.field11);
  }

  static class ObjectRecord {
    Integer int_;
    Byte byte_;
    Character char_;
    Double double_;
    Float float_;
    Boolean boolean_;
    Long long_;
    Short short_;
  }

  public void testDefaultObjectHandlers() {
    String inputString =
      "int_,byte_,char_,double_,float_,boolean_,long_,short_\n" +
      "123,114,q,3.45,3.78,true,567,23\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    ObjectRecord record = csvReader.registerObject(new ObjectRecord());

    csvReader.start();
    csvReader.next();
    assertEquals(new Integer(123), record.int_);
    assertEquals(new Byte((byte) 114), record.byte_);
    assertEquals(new Character('q'), record.char_);
    assertEquals(3.45, record.double_);
    assertEquals(3.78f, record.float_);
    assertEquals(new Boolean(true), record.boolean_);
    assertEquals(new Long(567), record.long_);
    assertEquals(new Short((short) 23), record.short_);
  }

  public void testDuplicateField() {
    String inputString =
      "field1,field2,field2\n" +
      "1,qwe,3.4\n" +
      "-3,asd,-4.5\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = csvReader.registerObject(new Record1());
    try {
      csvReader.start();
      fail("Theres was no exception.");
    } catch (RuntimeException e) {}
  }

  public void testAllowedDuplicateField() {
    String inputString =
      "field1,field2,field3,field3\n" +
      "1,qwe,3.4,3.4\n" +
      "-3,asd,-4.5,-4.5\n";
    Reader stringReader = new StringReader(inputString);
    CsvReader csvReader = new CsvReader(stringReader, ',');
    Record1 record1 = csvReader.registerObject(new Record1());
    try {
      csvReader.start();
    } catch (RuntimeException e) {
      fail("Theres was exception occured. But this case is allowed.");
      e.printStackTrace();
    }
  }


  public static Test suite() {
    return new TestSuite(CsvReaderTest.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }

}
