package hu.sztaki.ilab.reflecsv;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class TestConstructor extends TestCase {

  @Override
  protected void setUp() {
  }

  @Override
  protected void tearDown() {
  }

  private static class MyObject {
    int i = 5;
  }

  public void testClone() {
    MyObject obj = Constructor.construct(MyObject.class);
    assertNotNull(obj);
    assertEquals(5, obj.i);
  }

  public static Test suite() {
    return new TestSuite(TestConstructor.class);
  }

  public static void main (String[] args) {
    junit.textui.TestRunner.run (suite());
  }


}
