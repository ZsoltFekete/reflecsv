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

}
