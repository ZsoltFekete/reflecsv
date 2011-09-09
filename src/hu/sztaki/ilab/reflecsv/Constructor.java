package hu.sztaki.ilab.reflecsv;

class Constructor {
  public static <T> T construct(Class<T> cls) {
    Object[] emtpyList = new Object[0];
    try {
      java.lang.reflect.Constructor constructor = cls.getDeclaredConstructor();
      constructor.setAccessible(true);
      Object obj = constructor.newInstance();
      T result = cls.cast(obj);
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
