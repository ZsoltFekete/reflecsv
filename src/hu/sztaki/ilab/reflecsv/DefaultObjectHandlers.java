package hu.sztaki.ilab.reflecsv;

public class DefaultObjectHandlers {

  public static class StringHandler implements ObjectHandler {
    public Object convert(String value) {
      return value;
    }
  }

  public static class IntHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Integer(Integer.valueOf(value));
    }
  }

  public static class DoubleHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Double(Double.valueOf(value));
    }
  }

  public static class BooleanHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Boolean(Boolean.valueOf(value));
    }
  }

  public static class ByteHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Byte(Byte.valueOf(value));
    }
  }

  public static class FloatHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Float(Float.valueOf(value));
    }
  }

  public static class LongHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Long(Long.valueOf(value));
    }
  }

  public static class ShortHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Short(Short.valueOf(value));
    }
  }

  public static class CharHandler implements ObjectHandler {
    public Object convert(String value) {
      return new Character(value.charAt(0));
    }
  }

}

