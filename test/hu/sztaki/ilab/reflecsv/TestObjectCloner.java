package hu.sztaki.ilab.reflecsv;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestObjectCloner extends TestCase {

  @Override
  protected void setUp() {
  }

  @Override
  protected void tearDown() {
  }

  private static class MyObject {
    int i;
    public MyObject clone() {
      MyObject copy = new MyObject();
      copy.i = i;
      return copy;
    }
  }

  public void testClone() {
    MyObject original = new MyObject();
    original.i =17;

    MyObject copy = ObjectCloner.clone(original);

    assertNotNull(copy);
    assertNotSame(original, copy);
    assertEquals(17, copy.i);
  }

  public static Test suite() {
    return new TestSuite(TestObjectCloner.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }


}
