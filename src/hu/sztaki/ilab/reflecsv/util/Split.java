package hu.sztaki.ilab.reflecsv.util;

import java.util.ArrayList;

public class Split {
  public static ArrayList<String> split(String s, char separator) {
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
}
