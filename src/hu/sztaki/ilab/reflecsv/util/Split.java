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

 public static ArrayList<String> splitReqiredFields(String s, char separator,
     List<Integer> requiredFields) {
    if (null == requiredFields) {
      throw new RuntimeException("splitReqiredFields: requiredFields" +
          " list is null");
    }

    if (null == s) {
      throw new RuntimeException("Split: String is null.");
    }

    ArrayList<String> res = new ArrayList<String>();
    if (0 == requiredFields.size()) {
      return res;
    }

    int actualFieldNum = 0;
    int actualRequiredListIndex = 0;
    int actualRequiredFieldIndex = requiredFields.get(0);
    StringBuffer sb = null;
    if (actualFieldNum == actualRequiredFieldIndex) {
      sb = new StringBuffer();
    }
    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (c == separator) {
         if (actualRequiredFieldIndex == actualFieldNum) {
          res.add(sb.toString());
          ++actualRequiredListIndex;
          if (actualRequiredListIndex >= requiredFields.size()) {
            return res;
          }
          actualRequiredFieldIndex =
            requiredFields.get(actualRequiredListIndex);
        }
        ++actualFieldNum;
        if (actualFieldNum == actualRequiredFieldIndex) {
          sb = new StringBuffer();
        } else {
          sb = null;
        }
      } else {
        if (actualFieldNum == actualRequiredFieldIndex) {
          sb.append(c);
        }
      }
    }
    res.add(sb.toString());
    return res;

 } 
}
