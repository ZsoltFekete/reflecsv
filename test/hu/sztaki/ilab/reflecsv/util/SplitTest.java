package hu.sztaki.ilab.reflecsv.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;

public class SplitTest extends TestCase {

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

  public void testEmpytString() {
    ArrayList<String> result = Split.split("", '|');
    assertNotNull(result); 
    assertEquals(1, result.size());
    assertEquals("", result.get(0));
  }

  public void testNotContains() {
    ArrayList<String> result = Split.split("test", '|');
    assertNotNull(result); 
    assertEquals(1, result.size());
    assertEquals("test", result.get(0));
  }

  public void testContainsOnce() {
    ArrayList<String> result = Split.split("test|example", '|');
    assertNotNull(result); 
    assertEquals(2, result.size());
    assertEquals("test", result.get(0));
    assertEquals("example", result.get(1));
  }

  public void testContainsTwice() {
    ArrayList<String> result = Split.split("test|example|qwe", '|');
    assertNotNull(result); 
    assertEquals(3, result.size());
    assertEquals("test", result.get(0));
    assertEquals("example", result.get(1));
    assertEquals("qwe", result.get(2));
  }

  public void testContainsTwiceWithEmptyString() {
    ArrayList<String> result = Split.split("test||qwe", '|');
    assertNotNull(result); 
    assertEquals(3, result.size());
    assertEquals("test", result.get(0));
    assertEquals("", result.get(1));
    assertEquals("qwe", result.get(2));
  }

  public void testLongerString() {
    String input = ",asd,qwe,f,,g,";
    ArrayList<String> result = Split.split(input, ',');
    assertNotNull(result); 
    assertEquals(7, result.size());
    assertEquals("", result.get(0));
    assertEquals("asd", result.get(1));
    assertEquals("qwe", result.get(2));
    assertEquals("f", result.get(3));
    assertEquals("", result.get(4));
    assertEquals("g", result.get(5));
    assertEquals("", result.get(6));
  }

  public static Test suite() {
    return new TestSuite(SplitTest.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }

}
