package android.support.v4.view.accessibility;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityRecord;

class AccessibilityEventCompatIcs
{
  public static void appendRecord(AccessibilityEvent paramAccessibilityEvent, Object paramObject)
  {
    paramAccessibilityEvent.appendRecord((AccessibilityRecord)paramObject);
  }
  
  public static Object getRecord(AccessibilityEvent paramAccessibilityEvent, int paramInt)
  {
    return paramAccessibilityEvent.getRecord(paramInt);
  }
  
  public static int getRecordCount(AccessibilityEvent paramAccessibilityEvent)
  {
    return paramAccessibilityEvent.getRecordCount();
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     android.support.v4.view.accessibility.AccessibilityEventCompatIcs
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */