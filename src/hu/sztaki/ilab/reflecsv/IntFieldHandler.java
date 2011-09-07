package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

class IntFieldHandler implements FieldHandler {
  public void fillField(Field field, Object obj, String value) {
    int intValue = Integer.valueOf(value);
    try {
      field.setInt(obj, intValue);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}

