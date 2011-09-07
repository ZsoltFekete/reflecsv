package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

class PrimitiveHandlers {
 
  public static class IntFieldHandler implements FieldHandler {
    private IntHandler intHandler;
    public IntFieldHandler(IntHandler intHandler) {
      this.intHandler = intHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      int intValue = intHandler.convert(value);
      try {
        field.setInt(obj, intValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static class DoubleFieldHandler implements FieldHandler {
    private DoubleHandler doubleHandler;
    public DoubleFieldHandler(DoubleHandler doubleHandler) {
      this.doubleHandler = doubleHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      double doubleValue = doubleHandler.convert(value);
      try {
        field.setDouble(obj, doubleValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static class BooleanFieldHandler implements FieldHandler {
    private BooleanHandler booleanHandler;
    public BooleanFieldHandler(BooleanHandler booleanHandler) {
      this.booleanHandler = booleanHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      boolean booleanValue = booleanHandler.convert(value);
      try {
        field.setBoolean(obj, booleanValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static class ByteFieldHandler implements FieldHandler {
    private ByteHandler byteHandler;
    public ByteFieldHandler(ByteHandler byteHandler) {
      this.byteHandler = byteHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      byte byteValue = byteHandler.convert(value);
      try {
        field.setByte(obj, byteValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static class FloatFieldHandler implements FieldHandler {
    private FloatHandler floatHandler;
    public FloatFieldHandler(FloatHandler floatHandler) {
      this.floatHandler = floatHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      float floatValue = floatHandler.convert(value);
      try {
        field.setFloat(obj, floatValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static class LongFieldHandler implements FieldHandler {
    private LongHandler longHandler;
    public LongFieldHandler(LongHandler longHandler) {
      this.longHandler = longHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      long longValue = longHandler.convert(value);
      try {
        field.setLong(obj, longValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static class ShortFieldHandler implements FieldHandler {
    private ShortHandler shortHandler;
    public ShortFieldHandler(ShortHandler shortHandler) {
      this.shortHandler = shortHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      short shortValue = shortHandler.convert(value);
      try {
        field.setShort(obj, shortValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

  public static class CharFieldHandler implements FieldHandler {
    private CharHandler charHandler;
    public CharFieldHandler(CharHandler charHandler) {
      this.charHandler = charHandler;
    }
    public void fillField(Field field, Object obj, String value) {
      char charValue = charHandler.convert(value);
      try {
        field.setChar(obj, charValue);
      } catch (IllegalAccessException e) {
        e.printStackTrace();
      }
    }
  }

}
