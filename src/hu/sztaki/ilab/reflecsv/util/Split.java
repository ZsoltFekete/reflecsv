package hu.sztaki.ilab.reflecsv.util;

import java.util.ArrayList;
import java.util.List;

public class Split {
  public static ArrayList<String> split(String s, char separator) {
    if (null == s) {
      throw new RuntimeException("Split: String is null.");
    }

    ArrayList<String> res = new ArrayList<String>();
    if (s.equals("")) {
      res.add(s);
      return res;
    }

    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == separator) {
        res.add(sb.toString());
        sb = new StringBuffer();
      } else {
        sb.append(c);
      }
    }
    res.add(sb.toString());
    return res;
  }

 public static String[] splitReqiredFields(String s, char separator,
     int[] requiredFields) {
    if (null == requiredFields) {
      throw new RuntimeException("splitReqiredFields: requiredFields" +
          " list is null");
    }

    if (null == s) {
      throw new RuntimeException("Split: String is null.");
    }

    String[] res = new String[requiredFields.length];
    if (0 == requiredFields.length) {
      return res;
    }

    int actualFieldNum = 0;
    int actualRequiredListIndex = 0;
    int actualRequiredFieldIndex = requiredFields[0];
    int beginIndex = 0;
    int stringLength = s.length();
    int numberOfRequiredField = requiredFields.length;
    for (int i = 0; i < stringLength; i++) {
      if (s.charAt(i) == separator) {
         if (actualFieldNum == actualRequiredFieldIndex) {
          res[actualRequiredListIndex] = s.substring(beginIndex, i);
          ++actualRequiredListIndex;
          if (actualRequiredListIndex >= numberOfRequiredField) {
            return res;
          }
          int nextIndex = requiredFields[actualRequiredListIndex];
          if (nextIndex <= actualRequiredFieldIndex) {
            throw new RuntimeException("List is not increasing:" +
                intArrayToString(requiredFields));
          }
          actualRequiredFieldIndex = nextIndex;
        }
        ++actualFieldNum;
        if (actualFieldNum == actualRequiredFieldIndex) {
          beginIndex = i + 1;
        }
      }
    }
    if (actualFieldNum == actualRequiredFieldIndex) {
      res[actualRequiredListIndex] = s.substring(beginIndex, stringLength);
      return res;
    } else {
      if (actualFieldNum < actualRequiredFieldIndex) {
        throw new RuntimeException("There is only " + (actualFieldNum + 1) +
            " field in string \"" + s + "\"" +
            " and there is a required field inded: " +
            actualRequiredFieldIndex);
      }
      throw new RuntimeException("Internal inconsistency.");
    }
  } 

 private static String intArrayToString(int[] array) {
   StringBuffer sb = new StringBuffer();
   for (int i = 0; i < array.length; ++i) {
     sb.append(" " + array[i]);
   }
   return sb.toString();
 }
}
