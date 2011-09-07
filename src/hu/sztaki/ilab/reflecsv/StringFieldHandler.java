package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

class StringFieldHandler implements FieldHandler {
  public void fillField(Field field, Object obj, String value) {
    try {
      field.set(obj, value);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}

