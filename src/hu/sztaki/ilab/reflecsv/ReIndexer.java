package hu.sztaki.ilab.reflecsv;

import java.util.List;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

class ReIndexer {

  private List<ObjectDescriptor> objectDesciptors;

  private SortedSet<Integer> sortedIndices;

  private Map<Integer, Integer> originalToNewIndex;

  public ReIndexer(List<ObjectDescriptor> objectDesciptors) {
    this.objectDesciptors = objectDesciptors;
  }

  int[] createSortedIndexArray() {
    collectAllIndices();
    createOriginalToNewIndexMap();
    return SortedSetToArray(sortedIndices);
  }

  public void reIndexFieldDescriptors() {
    fillNewIndexMembers(originalToNewIndex);
  }

  private void collectAllIndices() {
    sortedIndices = new TreeSet<Integer>();
    for (ObjectDescriptor objectDescriptor : objectDesciptors) {
      for (FieldDescriptor fieldDescriptor : objectDescriptor.fields) {
        sortedIndices.add(fieldDescriptor.originalIndex);
      }
    }
  }

  private void createOriginalToNewIndexMap() {
    originalToNewIndex = new HashMap<Integer, Integer>();
    int counter = 0;
    for (Integer index : sortedIndices) {
      originalToNewIndex.put(index, counter);
      ++counter;
    }
  }

  private static int[] SortedSetToArray(SortedSet<Integer> sortedIndices) {
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

}
