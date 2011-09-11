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
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;


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
    matchRecordsWithHeader();
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

  private void matchRecordsWithHeader() {
    SortedSet<Integer> sortedIndices = collectAllIndices();

    Map<Integer, Integer> originalToNewIndex =
      createOriginalToNewIndexMap(sortedIndices);

    sortedIndexArray = createSortedIndexArray(sortedIndices);
  
    fillNewIndexMembers(originalToNewIndex);
  }

  private SortedSet<Integer> collectAllIndices() {
    SortedSet<Integer> sortedIndices = new TreeSet<Integer>();
    for (ObjectDescriptor objectDescriptor : objectDesciptors) {
      for (FieldDescriptor fieldDescriptor : objectDescriptor.fields) {
        sortedIndices.add(fieldDescriptor.originalIndex);
      }
    }
    return sortedIndices;
  }

  private Map<Integer, Integer> createOriginalToNewIndexMap(
      SortedSet<Integer> sortedIndices) {
    Map<Integer, Integer> originalToNewIndex = new HashMap<Integer, Integer>();
    int counter = 0;
    for (Integer index : sortedIndices) {
      originalToNewIndex.put(index, counter);
      ++counter;
    }
    return originalToNewIndex;
  }

  private int[] createSortedIndexArray(SortedSet<Integer> sortedIndices) {
    int[] result = new int[sortedIndices.size()];
    int counter = 0;
    for (Integer index : sortedIndices) {
      result[counter] = index;
      ++counter;
    }
    return result;
  }

  private void fillNewIndexMembers(Map<Integer, Integer> originalToNewIndex) {
    for (ObjectDescriptor objectDescriptor : objectDesciptors) {
      for (FieldDescriptor fieldDescriptor : objectDescriptor.fields) {
        fieldDescriptor.newIndex =
          originalToNewIndex.get(fieldDescriptor.originalIndex);
      }
    }
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

  public <T> T getNextRecord() {
    next();
    if (0 == recordObjects.size()) {
      return null;
    }
    @SuppressWarnings("unchecked")
    T result = ObjectCloner.clone((T)recordObjects.get(0));
    return result;
  }

  public Object[] getNextRecords() {
    next();
    Object[] result = new Object[recordObjects.size()];
    for (int i = 0; i < recordObjects.size(); ++i) {
  //    @SuppressWarnings("unchecked")
      result[i] = ObjectCloner.clone(recordObjects.get(i));
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

