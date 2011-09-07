package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

interface FieldHandler {
  public void fillField(Field field, Object obj, String value);
}

