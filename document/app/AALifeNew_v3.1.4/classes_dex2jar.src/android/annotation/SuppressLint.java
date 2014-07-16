package android.annotation;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.CLASS)
@Target({java.lang.annotation.ElementType.TYPE, java.lang.annotation.ElementType.FIELD, java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.PARAMETER, java.lang.annotation.ElementType.CONSTRUCTOR, java.lang.annotation.ElementType.LOCAL_VARIABLE})
public @interface SuppressLint
{
  String[] value();
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     android.annotation.SuppressLint
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */