package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

class DblFieldHandler implements FieldHandler {
  public void fillField(Field field, Object obj, String value) {
    double doubleValue = Double.valueOf(value);
    try {
      field.setDouble(obj, doubleValue);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}

