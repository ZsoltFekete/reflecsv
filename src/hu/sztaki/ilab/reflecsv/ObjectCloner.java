package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Method;
import java.lang.reflect.Field;

public class ObjectCloner {
  public static <T> T clone(T t) {
    @SuppressWarnings("unchecked")
    T copy = Constructor.construct((Class<T>)t.getClass());
    Class<?> cls = t.getClass();
    Field[] fields = cls.getDeclaredFields();
    for (Field field : fields) {
      field.setAccessible(true);
      try {
        field.set(copy, field.get(t));
      } catch (java.lang.IllegalAccessException e) {
        e.printStackTrace();
      }
    }
    return copy;
  }
}
