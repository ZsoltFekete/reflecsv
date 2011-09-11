package hu.sztaki.ilab.reflecsv;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

class ObjectDescriptorCreator {

  private TypeManager typeManeger;

  private List<Object> recordObjects;
  
  private String headerString;

  private List<String> header;

  private List<ObjectDescriptor> objectDesciptors =
    new ArrayList<ObjectDescriptor>();

  public ObjectDescriptorCreator(TypeManager typeManeger,
      List<Object> recordObjects, String headerString, List<String> header) {
    this.recordObjects = recordObjects;
    this.headerString = headerString;
    this.header = header;
    this.typeManeger = typeManeger;
  }

  public List<ObjectDescriptor> createObjectDescriptors() {
    for (Object recordObject : recordObjects) {
      ObjectDescriptor objectDescriptor = createObjectDescriptor(recordObject);
      objectDesciptors.add(objectDescriptor);
    }
    return objectDesciptors;
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
    String name = getAnnotationOrFieldName(field);
    int originalIndex = findInHeader(name);
    if (-1 == originalIndex) {
      throw new RuntimeException("Field \"" + name +
          "\" was not found in header. The header was:\n" +
          headerString);
    }
    FieldDescriptor fieldDescriptor = new FieldDescriptor();
    field.setAccessible(true);
    fieldDescriptor.field = field;
    fieldDescriptor.originalIndex = originalIndex;
    FieldHandler fieldHandler = typeManeger.createFieldHandler(field.getType());
    fieldDescriptor.handler = fieldHandler;
    return fieldDescriptor;
  }

  private static String getAnnotationOrFieldName(Field field) {
    Name annotation = field.getAnnotation(Name.class);
    if (null != annotation) {
      return annotation.value();
    } else {
      return field.getName();
    }
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

}
