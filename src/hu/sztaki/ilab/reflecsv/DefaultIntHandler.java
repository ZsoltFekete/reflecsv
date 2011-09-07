package hu.sztaki.ilab.reflecsv;

public class DefaultIntHandler implements IntHandler {
  public int convert(String value) {
    return Integer.valueOf(value);
  }
}

