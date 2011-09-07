package hu.sztaki.ilab.reflecsv;

import hu.sztaki.ilab.reflecsv.util.Split;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.io.BufferedReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class CsvReader {

  private Reader reader;
  private char separator;
  
  private String line = null;

  private List<Object> recordObjects = new ArrayList<Object>();
  private List<ObjectDescriptor> objectDesciptors =
    new ArrayList<ObjectDescriptor>();

  private static class FieldDescriptor {
    public Field field;
    public int index;
    public FieldHandler handler;
  }

  private static class ObjectDescriptor {
    public List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();
  }

  public CsvReader(Reader reader, char separator) {
    this.reader = reader;
    this.separator = separator;
  }

  public Object registerClass(Class<?> cls) {
    Object[] emtpyList = new Object[0];
    try {
      Constructor constructor = cls.getDeclaredConstructor();
      constructor.setAccessible(true);
      Object obj = constructor.newInstance();
      registerObject(obj);
      return obj;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void registerObject(Object obj) {
    recordObjects.add(obj);
  }

  public <T> T registerClass() {
/*
    Class cls = T.class;
    Class[] emtpyList = new Class[0];
    Constructor constructor = cls.getConstructor(emtpyList);
    */
    return null;
  }
    
  private ArrayList<String> header;
  private String headerString;
  private BufferedReader bufferedreader;;

  public void start() {
    createHandlers();
    bufferedreader = new BufferedReader(reader);
    readNextLine();
    headerString = line;
    if (null == headerString) {
      throw new RuntimeException("File is empty. " +
          "It should contain at leas one line: a header");
    }
    header = Split.split(headerString, separator);
    for (Object recordObject : recordObjects) {
      ObjectDescriptor objectDescriptor = createObjectDescriptor(recordObject);
      objectDesciptors.add(objectDescriptor);
    }
  }

  private ObjectDescriptor createObjectDescriptor(Object recordObject) {
    ObjectDescriptor objectDescriptor = new ObjectDescriptor();
    Class cls = recordObject.getClass();
    Field fieldlist[] = cls.getDeclaredFields();
    for (int i = 0; i < fieldlist.length; i++) {
      Field field = fieldlist[i];
      FieldDescriptor fieldDescriptor = createFieldDescriptor(field);
      objectDescriptor.fields.add(fieldDescriptor);
    }
    return objectDescriptor;
  }

  private FieldDescriptor createFieldDescriptor(Field field) {
    int index = findInHeader(field.getName());
    if (-1 == index) {
      throw new RuntimeException("Field \"" + field.getName() +
          "\" was not found in header. The header was:\n" +
          headerString);
    }
    FieldDescriptor fieldDescriptor = new FieldDescriptor();
    field.setAccessible(true);
    fieldDescriptor.field = field;
    fieldDescriptor.index = index;
    FieldHandler fieldHandler = createFieldHandler(field.getType());
    fieldDescriptor.handler = fieldHandler;
    return fieldDescriptor;
  }

  private int findInHeader(String name) {
    for (int i = 0; i < header.size(); ++i) {
      if (header.get(i).equals(name)) {
        return i;
      }
    }
    return -1;
  }
 
  private Map<Class, FieldHandler> fieldHandlers =
    new HashMap<Class, FieldHandler>();
  private void createHandlers() {
    fieldHandlers.put(Integer.TYPE, new IntFieldHandler());
    fieldHandlers.put(Double.TYPE, new DblFieldHandler());
    fieldHandlers.put(String.class, new StringFieldHandler());
  } 

  private FieldHandler createFieldHandler(Class cls) {
    if (fieldHandlers.containsKey(cls)) {
      return fieldHandlers.get(cls);
    } else {
      throw new RuntimeException("Not found handler");
    }
  }

  public boolean hasNext() {
    readNextLine();
    return (null != line);
  }

  private void readNextLine() {
    line = null;
    try {
      line = bufferedreader.readLine();
    } catch (java.io.IOException e) {
      e.printStackTrace();
    }
  }

  public void next() {
    ArrayList<String> splittedLine = Split.split(line, separator);
    for (int i = 0; i < recordObjects.size(); ++i) {
      Object recordObject = recordObjects.get(i);
      ObjectDescriptor objectDescriptor = objectDesciptors.get(i);
      fillRecord(recordObject, objectDescriptor, splittedLine);
    } 
  }

  private void fillRecord(Object recordObject, ObjectDescriptor objectDescriptor,
      List<String> splittedLine) {
    for (FieldDescriptor fieldDescriptor : objectDescriptor.fields) {
      fillField(recordObject, fieldDescriptor, splittedLine);
    }
  }

  private void fillField(Object obj, FieldDescriptor fieldDescriptor,
      List<String> splittedLine) {
    int index = fieldDescriptor.index;
    Field field = fieldDescriptor.field;
    String value = splittedLine.get(index);
    FieldHandler fieldHandler = fieldDescriptor.handler;
    fieldHandler.fillField(field, obj, value);
  }
}
