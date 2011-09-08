package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Method;

public class ObjectCloner {
  public static <T> T clone(T t) {
    try {
      Method method = t.getClass().getMethod("clone");
      method.setAccessible(true);
      Object obj = method.invoke(t);
      @SuppressWarnings("unchecked")
      T result = (T) obj;
      return result;
    } catch (java.lang.IllegalAccessException e) {
      e.printStackTrace();
    } catch (java.lang.NoSuchMethodException e) {
      e.printStackTrace();
    } catch (java.lang.reflect.InvocationTargetException e) {
      e.printStackTrace();
    }
    return null;
  }
}
