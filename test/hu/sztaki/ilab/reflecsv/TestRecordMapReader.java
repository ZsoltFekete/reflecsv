package hu.sztaki.ilab.reflecsv;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.Map;
import java.util.HashMap;
import java.io.Reader;
import java.io.StringReader;

public class TestRecordMapReader extends TestCase {

  @Override
  protected void setUp() {
  }

  @Override
  protected void tearDown() {
  }

  private static class StringIdRecord implements IdRecord<String> {
    public String id;
    public String getId() {
      return id;
    }
  }

  private static class Record1 {
    public int field1;
    public String field2;
  }

  public void testRecordMapReader() {
    String inputString =
      "id,field1,field2,field3\n" +
      "a,1,qwe,3.4\n" +
      "b,-3,asd,-4.5\n";
    Reader reader = new StringReader(inputString);
    RecordMapReader<String, StringIdRecord, Record1> recordReader =
      new RecordMapReader<String, StringIdRecord, Record1>(reader, ',',
          new StringIdRecord(), new Record1());
    Map<String, Record1> map = recordReader.read();
    assertNotNull(map);
    assertEquals(2, map.size());
    assertEquals(1, map.get("a").field1);
    assertEquals("qwe", map.get("a").field2);
    assertEquals(-3, map.get("b").field1);
    assertEquals("asd", map.get("b").field2);
  }

  public void testRecordMapReaderDuplicateId() {
    String inputString =
      "id,field1,field2,field3\n" +
      "a,2,qwe,3.4\n" +
      "a,1,qwe,3.4\n" +
      "b,-3,asd,-4.5\n";
    Reader reader = new StringReader(inputString);
    RecordMapReader<String, StringIdRecord, Record1> recordReader =
      new RecordMapReader<String, StringIdRecord, Record1>(reader, ',',
          new StringIdRecord(), new Record1());
    recordReader.setNoExceptionForDuplicateId();
    Map<String, Record1> map = recordReader.read();
    assertNotNull(map);
    assertEquals(2, map.size());
    assertEquals(1, map.get("a").field1);
    assertEquals("qwe", map.get("a").field2);
    assertEquals(-3, map.get("b").field1);
    assertEquals("asd", map.get("b").field2);
  }

  public void testRecordMapReaderDuplicateIdWithException() {
    String inputString =
      "id,field1,field2,field3\n" +
      "a,2,qwe,3.4\n" +
      "a,1,qwe,3.4\n" +
      "b,-3,asd,-4.5\n";
    Reader reader = new StringReader(inputString);
    RecordMapReader<String, StringIdRecord, Record1> recordReader =
      new RecordMapReader<String, StringIdRecord, Record1>(reader, ',',
          new StringIdRecord(), new Record1());
    recordReader.setExceptionForDuplicateId();
    try {
      Map<String, Record1> map = recordReader.read();
      fail("There was no exception.");
    } catch (RuntimeException e) {}

  }

  public static Test suite() {
    return new TestSuite(TestRecordMapReader.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }


}
