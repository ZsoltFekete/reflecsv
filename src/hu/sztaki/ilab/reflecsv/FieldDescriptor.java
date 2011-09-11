package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;

class FieldDescriptor {
  public Field field;
  public int originalIndex;
  public int newIndex;
  public FieldHandler handler;
}

