package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Method;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

  public class ObjectCloner {

  private Object object;

  private Field[] fields;

  private Method method;

  private boolean hasCloneMethod;

  public ObjectCloner(Object object) {
    this.object = object;
    createFields();
    checkCloneMethod();
  }

  private void createFields() {
    Class<?> cls = object.getClass();
    fields = cls.getDeclaredFields();
    List<Field> fieldList = new ArrayList<Field>();
    for (Field field : fields) {
      if (!field.isSynthetic()) {
        field.setAccessible(true);
        fieldList.add(field);
      }
    }
    fields = fieldList.toArray(new Field[fieldList.size()]); 
  }

  private void checkCloneMethod() {
    hasCloneMethod = false;
    try {
      method = object.getClass().getMethod("clone");
      method.setAccessible(true);
      hasCloneMethod = true;
    } catch (java.lang.NoSuchMethodException e) {
      hasCloneMethod = false;
    }
  }

  public Object getClone() {
    if (hasCloneMethod) {
      try {
        return method.invoke(object);
      } catch (java.lang.IllegalAccessException e) {
        e.printStackTrace();
      } catch (java.lang.reflect.InvocationTargetException e) {
        e.printStackTrace();
      }
      return null;
    } else {
      Object copy = Constructor.construct(object.getClass());
      for (Field field : fields) {
        try {
          field.set(copy, field.get(object));
        } catch (java.lang.IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      return copy;
    }
  }

}
