package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

class ObjectFieldHandler implements FieldHandler {

  private ObjectHandler objectHandler;

  public ObjectFieldHandler(ObjectHandler objectHandler) {
    this.objectHandler = objectHandler;
  }

  public void fillField(Field field, Object obj, String value) {
    try {
      Object valueObject = objectHandler.convert(value);
      field.set(obj, valueObject);
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    }
  }
}

