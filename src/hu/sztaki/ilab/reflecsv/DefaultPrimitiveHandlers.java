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

  public static class DefaultBooleanHandler implements BooleanHandler {
    public boolean convert(String value) {
      return Boolean.valueOf(value);
    }
  }

  public static class DefaultByteHandler implements ByteHandler {
    public byte convert(String value) {
      return Byte.valueOf(value);
    }
  }

  public static class DefaultFloatHandler implements FloatHandler {
    public float convert(String value) {
      return Float.valueOf(value);
    }
  }

  public static class DefaultLongHandler implements LongHandler {
    public long convert(String value) {
      return Long.valueOf(value);
    }
  }

  public static class DefaultShortHandler implements ShortHandler {
    public short convert(String value) {
      return Short.valueOf(value);
    }
  }

  public static class DefaultCharHandler implements CharHandler {
    public char convert(String value) {
      return value.charAt(0);
    }
  }

}

