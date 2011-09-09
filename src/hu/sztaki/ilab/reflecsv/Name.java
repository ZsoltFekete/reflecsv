package hu.sztaki.ilab.reflecsv;

import  java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 
@Retention(RetentionPolicy.RUNTIME)
public @interface Name {
  String value();
}

