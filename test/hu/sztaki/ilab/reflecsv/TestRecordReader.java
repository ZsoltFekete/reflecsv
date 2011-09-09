package hu.sztaki.ilab.reflecsv;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;
import java.io.Reader;
import java.io.StringReader;

public class TestRecordReader extends TestCase {

  @Override
  protected void setUp() {
  }

  @Override
  protected void tearDown() {
  }

  private static class Record1 {
    public int field1;
    public String field2;
  }

  public void testRecordReader() {
    String inputString =
      "field1,field2,field3\n" +
      "1,qwe,3.4\n" +
      "-3,asd,-4.5\n";
    Reader reader = new StringReader(inputString);
    RecordReader<Record1> recordReader =
      new RecordReader<Record1>(reader, ',', new Record1());
    List<Record1> list = recordReader.read();
    assertNotNull(list);
    assertEquals(2, list.size());
    assertEquals(1, list.get(0).field1);
    assertEquals("qwe", list.get(0).field2);
    assertEquals(-3, list.get(1).field1);
    assertEquals("asd", list.get(1).field2);
  }

  public static Test suite() {
    return new TestSuite(TestRecordReader.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }


}
