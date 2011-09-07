package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

class IntFieldHandler implements FieldHandler {
  private IntHandler intHandler;

  public IntFieldHandler(IntHandler intHandler) {
    this.intHandler = intHandler;
  }

  public void fillField(Field field, Object obj, String value) {
//    int intValue = Integer.valueOf(value);
    int intValue = intHandler.convert(value);
    try {
      field.setInt(obj, intValue);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}

