package hu.sztaki.ilab.reflecsv;

import hu.sztaki.ilab.reflecsv.util.Split;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


public class CsvReader {

  private Reader reader;
  private char separator;
  
  private String line = null;

  private List<Object> recordObjects = new ArrayList<Object>();
  private List<ObjectDescriptor> objectDesciptors =
    new ArrayList<ObjectDescriptor>();

  private ArrayList<String> header;
  private String headerString;
  private BufferedReader bufferedreader;;

  private Map<Class, ObjectHandler> objectHandlers =
    new HashMap<Class, ObjectHandler>();

  private Map<Class, FieldHandler> fieldHandlers =
    new HashMap<Class, FieldHandler>();

  private static class FieldDescriptor {
    public Field field;
    public int originalIndex;
    public int newIndex;
    public FieldHandler handler;
  }

  private static class ObjectDescriptor {
    public List<FieldDescriptor> fields = new ArrayList<FieldDescriptor>();
  }

  public CsvReader(Reader reader, char separator) {
    this.reader = reader;
    this.separator = separator;
    createObjectHandlers();
  }

  public CsvReader(String fileName, char separator)
    throws FileNotFoundException {
    this.separator = separator;
    createReader(fileName);
    createObjectHandlers();
  }

  private void createReader(String fileName) throws FileNotFoundException {
    reader = new FileReader(fileName);
  }

  private void createObjectHandlers() {
    objectHandlers.put(String.class,
        new DefaultObjectHandlers.StringHandler());
    objectHandlers.put(Integer.class,
        new DefaultObjectHandlers.IntHandler());
    objectHandlers.put(Double.class,
        new DefaultObjectHandlers.DoubleHandler());
    objectHandlers.put(Boolean.class,
        new DefaultObjectHandlers.BooleanHandler());
    objectHandlers.put(Byte.class,
        new DefaultObjectHandlers.ByteHandler());
    objectHandlers.put(Float.class,
        new DefaultObjectHandlers.FloatHandler());
    objectHandlers.put(Long.class,
        new DefaultObjectHandlers.LongHandler());
    objectHandlers.put(Short.class,
        new DefaultObjectHandlers.ShortHandler());
    objectHandlers.put(Character.class,
        new DefaultObjectHandlers.CharHandler());
  }

  public <T> T registerClass(Class<?> cls) {
    Object[] emtpyList = new Object[0];
    try {
      Constructor constructor = cls.getDeclaredConstructor();
      constructor.setAccessible(true);
      Object obj = constructor.newInstance();
      registerObject(obj);
      @SuppressWarnings("unchecked")
      T result = (T) obj;
      return result;
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public <T> T registerObject(T obj) {
    recordObjects.add(obj);
    return obj;
  }

  public <T> T registerClass(T t) {
    Class cls = t.getClass();
    return registerClass(cls);
  }

  private IntHandler intHandler =
    new DefaultPrimitiveHandlers.DefaultIntHandler();

  private DoubleHandler doubleHandler =
    new DefaultPrimitiveHandlers.DefaultDoubleHandler();

  private BooleanHandler booleanHandler =
    new DefaultPrimitiveHandlers.DefaultBooleanHandler();

  private ByteHandler byteHandler =
    new DefaultPrimitiveHandlers.DefaultByteHandler();

  private FloatHandler floatHandler =
    new DefaultPrimitiveHandlers.DefaultFloatHandler();

  private LongHandler longHandler =
    new DefaultPrimitiveHandlers.DefaultLongHandler();

  private ShortHandler shortHandler =
    new DefaultPrimitiveHandlers.DefaultShortHandler();

  private CharHandler charHandler =
    new DefaultPrimitiveHandlers.DefaultCharHandler();

  public void setIntHandler(IntHandler intHandler) {
    this.intHandler = intHandler;
  }

  public void setDoubleHandler(DoubleHandler doubleHandler) {
    this.doubleHandler = doubleHandler;
  }

  public void setBooleanHandler(BooleanHandler booleanHandler) {
    this.booleanHandler = booleanHandler;
  }

  public void setByteHandler(ByteHandler byteHandler) {
    this.byteHandler = byteHandler;
  }

  public void setFloatHandler(FloatHandler floatHandler) {
    this.floatHandler = floatHandler;
  }

  public void setLongHandler(LongHandler longHandler) {
    this.longHandler = longHandler;
  }

  public void setShortHandler(ShortHandler shortHandler) {
    this.shortHandler = shortHandler;
  }

  public void setCharHandler(CharHandler charHandler) {
    this.charHandler = charHandler;
  }

  public void setObjectHandler(Class cls, ObjectHandler objectHandler) {
    objectHandlers.put(cls, objectHandler);
  }
    
  public void start() {
    createFieldHandlers();
    bufferedreader = new BufferedReader(reader);
    matchRecordsWithHeader();
    readNextLine();
  }

  private List<Integer> sortedIndexList = new ArrayList<Integer>();
  private int[] sortedIndexArray;

  private void matchRecordsWithHeader() {
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
    SortedSet<Integer> sortedIndices = new TreeSet<Integer>();
    collectAllIndices(sortedIndices);
    int counter = 0;
    Map<Integer, Integer> originalToNewIndex = new HashMap<Integer, Integer>();
    for (Integer index : sortedIndices) {
      originalToNewIndex.put(index, counter);
      sortedIndexList.add(index);
      ++counter;
    }
    sortedIndexArray = new int[sortedIndexList.size()];
    for (int i = 0 ; i < sortedIndexList.size(); ++i) {
      sortedIndexArray[i] = sortedIndexList.get(i);
    }
    for (ObjectDescriptor objectDescriptor : objectDesciptors) {
      for (FieldDescriptor fieldDescriptor : objectDescriptor.fields) {
        fieldDescriptor.newIndex =
          originalToNewIndex.get(fieldDescriptor.originalIndex);
      }
    }
  }

  private void collectAllIndices(SortedSet<Integer> sortedIndices) {
    for (ObjectDescriptor objectDescriptor : objectDesciptors) {
      for (FieldDescriptor fieldDescriptor : objectDescriptor.fields) {
        sortedIndices.add(fieldDescriptor.originalIndex);
      }
    }
  }

  private void createFieldHandlers() {
    createPrimitiveFieldHandlers();
    createObjectFieldHandlers();
  }

  private void createPrimitiveFieldHandlers() {
    fieldHandlers.put(Integer.TYPE,
        new PrimitiveHandlers.IntFieldHandler(intHandler));
    fieldHandlers.put(Double.TYPE,
        new PrimitiveHandlers.DoubleFieldHandler(doubleHandler));
    fieldHandlers.put(Boolean.TYPE,
        new PrimitiveHandlers.BooleanFieldHandler(booleanHandler));
    fieldHandlers.put(Byte.TYPE,
        new PrimitiveHandlers.ByteFieldHandler(byteHandler));
    fieldHandlers.put(Float.TYPE,
        new PrimitiveHandlers.FloatFieldHandler(floatHandler));
    fieldHandlers.put(Long.TYPE,
        new PrimitiveHandlers.LongFieldHandler(longHandler));
    fieldHandlers.put(Short.TYPE,
        new PrimitiveHandlers.ShortFieldHandler(shortHandler));
    fieldHandlers.put(Character.TYPE,
        new PrimitiveHandlers.CharFieldHandler(charHandler));
  }

  private void createObjectFieldHandlers() {
    for (Map.Entry<Class, ObjectHandler> entry : objectHandlers.entrySet()) {
      Class cls = entry.getKey();
      ObjectHandler objectHandler = entry.getValue();
      fieldHandlers.put(cls, new ObjectFieldHandler(objectHandler));
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
    int originalIndex = findInHeader(field.getName());
    if (-1 == originalIndex) {
      throw new RuntimeException("Field \"" + field.getName() +
          "\" was not found in header. The header was:\n" +
          headerString);
    }
    FieldDescriptor fieldDescriptor = new FieldDescriptor();
    field.setAccessible(true);
    fieldDescriptor.field = field;
    fieldDescriptor.originalIndex = originalIndex;
    FieldHandler fieldHandler = createFieldHandler(field.getType());
    fieldDescriptor.handler = fieldHandler;
    return fieldDescriptor;
  }

  private int findInHeader(String name) {
    boolean isFound = false;
    int foundIndex = -1;
    for (int i = 0; i < header.size(); ++i) {
      if (header.get(i).equals(name)) {
        if (isFound) {
          throw new RuntimeException("Field \"" + name +
              "\" is duplicated in header.");
        } else {
          isFound = true;
          foundIndex = i;
        }
      }
    }
    if (isFound) {
      return foundIndex;
    } else {
      return -1;
    }
  }
 
  private FieldHandler createFieldHandler(Class cls) {
    if (fieldHandlers.containsKey(cls)) {
      return fieldHandlers.get(cls);
    } else {
      throw new RuntimeException("Not found handler");
    }
  }

  public boolean hasNext() {
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
    String[] splittedLine = Split.splitReqiredFields(line, separator,
        sortedIndexArray);
    for (int i = 0; i < recordObjects.size(); ++i) {
      Object recordObject = recordObjects.get(i);
      ObjectDescriptor objectDescriptor = objectDesciptors.get(i);
      fillRecord(recordObject, objectDescriptor, splittedLine);
    } 
    readNextLine();
  }

  private void fillRecord(Object recordObject, ObjectDescriptor objectDescriptor,
      String[] splittedLine) {
    for (FieldDescriptor fieldDescriptor : objectDescriptor.fields) {
      fillField(recordObject, fieldDescriptor, splittedLine);
    }
  }

  private void fillField(Object obj, FieldDescriptor fieldDescriptor,
      String[] splittedLine) {
    int newIndex = fieldDescriptor.newIndex;
    Field field = fieldDescriptor.field;
    String value = splittedLine[newIndex];
    FieldHandler fieldHandler = fieldDescriptor.handler;
    fieldHandler.fillField(field, obj, value);
  }
}

