package hu.sztaki.ilab.reflecsv.util;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;

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

  public void testNull() {
    try {
      ArrayList<String> result = Split.split(null, '|');
      fail("Exception should has happened.");
    } catch (RuntimeException e) {}
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

  public void testRequiredSplitNull() {
    try {
      ArrayList<String> result = Split.splitReqiredFields("", ',', null);
      fail("Theres was no exception thrown.");
    } catch (RuntimeException e) {}
  }

  public void testRequiredSplitEmptyList() {
    List<String> result = Split.splitReqiredFields("", ',',
        new ArrayList<Integer>());
    assertEquals(0, result.size());
  }

  public void testRequiredSplitNonEmptyList() {
    List<Integer> required = new ArrayList<Integer>();
    required.add(1);
    required.add(4);
    required.add(5);
    required.add(8);
    String input = "00,11,22,33,44,55,66,77,88,99,1010";
    List<String> result = Split.splitReqiredFields(input, ',', required);
    assertEquals(4, result.size());
    assertEquals("11", result.get(0));
    assertEquals("44", result.get(1));
    assertEquals("55", result.get(2));
    assertEquals("88", result.get(3));
  }

  public void testRequiredSplitLastElement() {
    List<Integer> required = new ArrayList<Integer>();
    required.add(1);
    required.add(4);
    required.add(5);
    required.add(8);
    String input = "00,11,22,33,44,55,66,77,88";
    List<String> result = Split.splitReqiredFields(input, ',', required);
    assertEquals(4, result.size());
    assertEquals("11", result.get(0));
    assertEquals("44", result.get(1));
    assertEquals("55", result.get(2));
    assertEquals("88", result.get(3));
  }

  public void testRequiredSplitNonIncreasing() {
    List<Integer> required = new ArrayList<Integer>();
    required.add(1);
    required.add(4);
    required.add(3);
    String input = "00,11,22,33,44,55,66,77,88";
    try {
      List<String> result = Split.splitReqiredFields(input, ',', required);
      fail("There was no exception.");
    } catch (RuntimeException e) {}
  }

  public static Test suite() {
    return new TestSuite(SplitTest.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }

}
