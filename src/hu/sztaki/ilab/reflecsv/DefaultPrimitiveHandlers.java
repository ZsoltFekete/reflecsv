package hu.sztaki.ilab.reflecsv;

public class DefaultPrimitiveHandlers {

public static class DefaultIntHandler implements IntHandler {
  public int convert(String value) {
    return Integer.valueOf(value);
  }
}

public static class DefaultDoubleHandler implements DoubleHandler {
  public double convert(String value) {
    return Double.valueOf(value);
  }
}

}

