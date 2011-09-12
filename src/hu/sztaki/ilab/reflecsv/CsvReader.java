package hu.sztaki.ilab.reflecsv;

import hu.sztaki.ilab.reflecsv.util.Split;

import java.lang.reflect.Field;
import java.lang.annotation.Annotation;
import java.io.BufferedReader;
import java.io.Reader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CsvReader {

  private Reader reader;

  private char separator;
  
  private String line = null;

  private boolean isFirstHasNextOrNext = true;

  private List<Object> recordObjects = new ArrayList<Object>();

  private List<ObjectDescriptor> objectDesciptors;

  private ArrayList<String> header;

  private String headerString;

  private BufferedReader bufferedreader;;

  private List<Integer> sortedIndexList = new ArrayList<Integer>();

  private int[] sortedIndexArray;

  private TypeManager typeManeger = new TypeManager();

  public CsvReader(Reader reader, char separator) {
    this.reader = reader;
    this.separator = separator;
  }

  public CsvReader(String fileName, char separator)
    throws FileNotFoundException {
    this.separator = separator;
    createReader(fileName);
  }

  private void createReader(String fileName) throws FileNotFoundException {
    reader = new FileReader(fileName);
  }

  public <T> T registerClass(Class<T> cls) {
    T t = Constructor.construct(cls);
    return registerObject(t);
  }

  public <T> T registerObject(T obj) {
    recordObjects.add(obj);
    return obj;
  }

  public void setIntHandler(IntHandler intHandler) {
    typeManeger.setIntHandler(intHandler);
  }

  public void setDoubleHandler(DoubleHandler doubleHandler) {
    typeManeger.setDoubleHandler(doubleHandler);
  }

  public void setBooleanHandler(BooleanHandler booleanHandler) {
    typeManeger.setBooleanHandler(booleanHandler);
  }

  public void setByteHandler(ByteHandler byteHandler) {
    typeManeger.setByteHandler(byteHandler);
  }

  public void setFloatHandler(FloatHandler floatHandler) {
    typeManeger.setFloatHandler(floatHandler);
  }

  public void setLongHandler(LongHandler longHandler) {
    typeManeger.setLongHandler(longHandler);
  }

  public void setShortHandler(ShortHandler shortHandler) {
    typeManeger.setShortHandler(shortHandler);
  }

  public void setCharHandler(CharHandler charHandler) {
    typeManeger.setCharHandler(charHandler);
  }

  public void setObjectHandler(Class cls, ObjectHandler objectHandler) {
    typeManeger.setObjectHandler(cls, objectHandler);
  }
    
  private void start() {
    bufferedreader = new BufferedReader(reader);

    readHeader();

    typeManeger.createFieldHandlers();

    ObjectDescriptorCreator objectDescriptorCreator =
      new ObjectDescriptorCreator(typeManeger, recordObjects, headerString,
        header);
    objectDesciptors = objectDescriptorCreator.createObjectDescriptors();

    createObjectCloners();

    createSortedIndicesAndReIndexFieldDescriptors();

    readNextLine();
  }

  private void readHeader() {
    readNextLine();
    headerString = line;
    if (null == headerString) {
      throw new RuntimeException("File is empty. " +
          "It should contain at leas one line: a header");
    }
    header = Split.split(headerString, separator);
  }

  private void createObjectCloners() {
    objectCloners = new ObjectCloner[recordObjects.size()];
    for (int i = 0; i < recordObjects.size(); ++i) {
      objectCloners[i] = new ObjectCloner(recordObjects.get(i));
    }
  }

  private void createSortedIndicesAndReIndexFieldDescriptors() {
    ReIndexer reIndexer = new ReIndexer(objectDesciptors);
    sortedIndexArray = reIndexer.createSortedIndexArray();
    reIndexer.reIndexFieldDescriptors();
  }

  public boolean hasNext() {
    if (isFirstHasNextOrNext) {
      start();
      isFirstHasNextOrNext = false;
    }
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

  private ObjectCloner[] objectCloners;

  public <T> T getNextRecord() {
    next();
    if (1 != recordObjects.size()) {
      throw new RuntimeException("To use getNextRecord you must have exactly" +
          " 1 record registered.\n" +
          "The number of registered records is: " + recordObjects.size());
    }
    @SuppressWarnings("unchecked")
    T result = (T) objectCloners[0].getClone();
    return result;
  }

  public Object[] getNextRecords() {
    next();
    Object[] result = new Object[recordObjects.size()];
    for (int i = 0; i < recordObjects.size(); ++i) {
      result[i] = objectCloners[i].getClone();
    }
    return result;
  }

  public void next() {
    if (isFirstHasNextOrNext) {
      start();
      isFirstHasNextOrNext = false;
    }
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

