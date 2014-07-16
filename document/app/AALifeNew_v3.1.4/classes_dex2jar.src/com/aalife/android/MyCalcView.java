package com.aalife.android;

import android.content.Context;
import android.os.Vibrator;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MyCalcView
  extends LinearLayout
{
  private EditText calcText = null;
  public String resultText = "";
  Vibrator vibrator = null;
  
  public MyCalcView(Context paramContext)
  {
    super(paramContext);
    this.vibrator = ((Vibrator)paramContext.getSystemService("vibrator"));
    View localView = LayoutInflater.from(paramContext).inflate(2130903060, null);
    LinearLayout.LayoutParams localLayoutParams = new LinearLayout.LayoutParams(-1, -1);
    localView.setLayoutParams(localLayoutParams);
    this.calcText = ((EditText)localView.findViewById(2131296381));
    Button localButton1 = (Button)localView.findViewById(2131296385);
    View.OnClickListener local1 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "1");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton1.setOnClickListener(local1);
    Button localButton2 = (Button)localView.findViewById(2131296386);
    View.OnClickListener local2 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "2");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton2.setOnClickListener(local2);
    Button localButton3 = (Button)localView.findViewById(2131296387);
    View.OnClickListener local3 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "3");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton3.setOnClickListener(local3);
    Button localButton4 = (Button)localView.findViewById(2131296389);
    View.OnClickListener local4 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "4");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton4.setOnClickListener(local4);
    Button localButton5 = (Button)localView.findViewById(2131296390);
    View.OnClickListener local5 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "5");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton5.setOnClickListener(local5);
    Button localButton6 = (Button)localView.findViewById(2131296391);
    View.OnClickListener local6 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "6");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton6.setOnClickListener(local6);
    Button localButton7 = (Button)localView.findViewById(2131296393);
    View.OnClickListener local7 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "7");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton7.setOnClickListener(local7);
    Button localButton8 = (Button)localView.findViewById(2131296394);
    View.OnClickListener local8 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "8");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton8.setOnClickListener(local8);
    Button localButton9 = (Button)localView.findViewById(2131296395);
    View.OnClickListener local9 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "9");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton9.setOnClickListener(local9);
    Button localButton10 = (Button)localView.findViewById(2131296398);
    View.OnClickListener local10 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          MyCalcView.this.calcText.setText(str + "0");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton10.setOnClickListener(local10);
    Button localButton11 = (Button)localView.findViewById(2131296397);
    View.OnClickListener local11 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          if (MyCalcView.this.hasDian(str).booleanValue())
          {
            MyCalcView.this.calcText.setText(str + ".");
            MyCalcView.this.setGuangBiao();
          }
        }
      }
    };
    localButton11.setOnClickListener(local11);
    Button localButton12 = (Button)localView.findViewById(2131296382);
    View.OnClickListener local12 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        MyCalcView.this.calcText.setText("");
      }
    };
    localButton12.setOnClickListener(local12);
    Button localButton13 = (Button)localView.findViewById(2131296383);
    View.OnClickListener local13 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        String str1 = MyCalcView.this.calcText.getText().toString();
        if (str1.length() > 0)
        {
          String str2 = str1.substring(0, -1 + str1.length());
          MyCalcView.this.calcText.setText(str2);
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton13.setOnClickListener(local13);
    Button localButton14 = (Button)localView.findViewById(2131296388);
    View.OnClickListener local14 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        String str = MyCalcView.this.calcText.getText().toString();
        if (MyCalcView.this.hasResult().booleanValue())
        {
          str = UtilityHelper.formatDouble(Math.abs(Double.parseDouble(MyCalcView.this.resultText)), "#.######");
          MyCalcView.this.calcText.setText(str);
        }
        if (MyCalcView.this.isNumber(str).booleanValue())
        {
          MyCalcView.this.calcText.setText(str + "+");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton14.setOnClickListener(local14);
    Button localButton15 = (Button)localView.findViewById(2131296392);
    View.OnClickListener local15 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        String str = MyCalcView.this.calcText.getText().toString();
        if (MyCalcView.this.hasResult().booleanValue())
        {
          str = UtilityHelper.formatDouble(Math.abs(Double.parseDouble(MyCalcView.this.resultText)), "#.######");
          MyCalcView.this.calcText.setText(str);
        }
        if (MyCalcView.this.isNumber(str).booleanValue())
        {
          MyCalcView.this.calcText.setText(str + "-");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton15.setOnClickListener(local15);
    Button localButton16 = (Button)localView.findViewById(2131296396);
    View.OnClickListener local16 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        String str = MyCalcView.this.calcText.getText().toString();
        if (MyCalcView.this.hasResult().booleanValue())
        {
          str = UtilityHelper.formatDouble(Math.abs(Double.parseDouble(MyCalcView.this.resultText)), "#.######");
          MyCalcView.this.calcText.setText(str);
        }
        if (MyCalcView.this.isNumber(str).booleanValue())
        {
          MyCalcView.this.calcText.setText(str + "×");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton16.setOnClickListener(local16);
    Button localButton17 = (Button)localView.findViewById(2131296400);
    View.OnClickListener local17 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        String str = MyCalcView.this.calcText.getText().toString();
        if (MyCalcView.this.hasResult().booleanValue())
        {
          str = UtilityHelper.formatDouble(Math.abs(Double.parseDouble(MyCalcView.this.resultText)), "#.######");
          MyCalcView.this.calcText.setText(str);
        }
        if (MyCalcView.this.isNumber(str).booleanValue())
        {
          MyCalcView.this.calcText.setText(str + "÷");
          MyCalcView.this.setGuangBiao();
        }
      }
    };
    localButton17.setOnClickListener(local17);
    Button localButton18 = (Button)localView.findViewById(2131296399);
    View.OnClickListener local18 = new View.OnClickListener()
    {
      public void onClick(View paramAnonymousView)
      {
        MyCalcView.this.doVibrator();
        if (!MyCalcView.this.hasResult().booleanValue())
        {
          String str = MyCalcView.this.calcText.getText().toString();
          if (MyCalcView.this.isNumber(str).booleanValue())
          {
            MyCalcView.this.getResult(str);
            MyCalcView.this.setGuangBiao();
          }
        }
      }
    };
    localButton18.setOnClickListener(local18);
    addView(localView);
  }
  
  private void doVibrator()
  {
    this.vibrator.vibrate(1000L);
    this.vibrator.cancel();
  }
  
  private void getResult(String paramString)
  {
    Matcher localMatcher1;
    ArrayList localArrayList1;
    Matcher localMatcher2;
    ArrayList localArrayList2;
    label52:
    int i;
    if (paramString.length() > 0)
    {
      localMatcher1 = Pattern.compile("[0-9\\.]+").matcher(paramString);
      localArrayList1 = new ArrayList();
      if (localMatcher1.find()) {
        break label153;
      }
      localMatcher2 = Pattern.compile("[\\+\\-\\×\\÷]+").matcher(paramString);
      localArrayList2 = new ArrayList();
      if (localMatcher2.find()) {
        break label173;
      }
      i = 0;
      label63:
      if (i < localArrayList2.size()) {
        break label189;
      }
    }
    for (int j = 0;; j++)
    {
      if (j >= localArrayList2.size())
      {
        this.resultText = UtilityHelper.formatDouble(((Double)localArrayList1.get(-1 + localArrayList1.size())).doubleValue(), "#.######");
        this.calcText.setText(paramString + "=" + this.resultText);
        return;
        label153:
        localArrayList1.add(Double.valueOf(Double.parseDouble(localMatcher1.group())));
        break;
        label173:
        localArrayList2.add(localMatcher2.group());
        break label52;
        label189:
        String str = "+";
        if (i > 0) {
          str = (String)localArrayList2.get(i - 1);
        }
        if (((String)localArrayList2.get(i)).equals("×"))
        {
          double d3 = ((Double)localArrayList1.get(i)).doubleValue();
          double d4 = ((Double)localArrayList1.get(i + 1)).doubleValue();
          localArrayList1.remove(i);
          localArrayList1.add(i, Double.valueOf(0.0D));
          localArrayList1.remove(i + 1);
          localArrayList1.add(i + 1, Double.valueOf(d3 * d4));
          localArrayList2.remove(i);
          localArrayList2.add(i, str);
        }
        if (((String)localArrayList2.get(i)).equals("÷"))
        {
          double d1 = ((Double)localArrayList1.get(i)).doubleValue();
          double d2 = ((Double)localArrayList1.get(i + 1)).doubleValue();
          localArrayList1.remove(i);
          localArrayList1.add(i, Double.valueOf(0.0D));
          localArrayList1.remove(i + 1);
          localArrayList1.add(i + 1, Double.valueOf(d1 / d2));
          localArrayList2.remove(i);
          localArrayList2.add(i, str);
        }
        i++;
        break label63;
      }
      if (((String)localArrayList2.get(j)).equals("+"))
      {
        double d7 = ((Double)localArrayList1.get(j)).doubleValue();
        double d8 = ((Double)localArrayList1.get(j + 1)).doubleValue();
        localArrayList1.remove(j);
        localArrayList1.add(j, Double.valueOf(0.0D));
        localArrayList1.remove(j + 1);
        localArrayList1.add(j + 1, Double.valueOf(d7 + d8));
        localArrayList2.remove(j);
        localArrayList2.add(j, "");
      }
      if (((String)localArrayList2.get(j)).equals("-"))
      {
        double d5 = ((Double)localArrayList1.get(j)).doubleValue();
        double d6 = ((Double)localArrayList1.get(j + 1)).doubleValue();
        localArrayList1.remove(j);
        localArrayList1.add(j, Double.valueOf(0.0D));
        localArrayList1.remove(j + 1);
        localArrayList1.add(j + 1, Double.valueOf(d5 - d6));
        localArrayList2.remove(j);
        localArrayList2.add(j, "");
      }
    }
  }
  
  private Boolean hasDian(String paramString)
  {
    if (paramString.length() > 0)
    {
      Matcher localMatcher = Pattern.compile("[0-9]+[\\.]*[0-9]*$").matcher(paramString);
      String str = "";
      if (localMatcher.find()) {
        str = localMatcher.group(0);
      }
      if ((!str.equals("")) && (str.lastIndexOf('.') < 0)) {
        return Boolean.valueOf(true);
      }
    }
    return Boolean.valueOf(false);
  }
  
  private Boolean hasResult()
  {
    String str = this.calcText.getText().toString();
    if ((str.length() > 0) && (str.lastIndexOf('=') >= 0)) {
      return Boolean.valueOf(true);
    }
    return Boolean.valueOf(false);
  }
  
  private Boolean isNumber(String paramString)
  {
    if (paramString.length() > 0)
    {
      String str = paramString.substring(-1 + paramString.length());
      return Boolean.valueOf(Pattern.compile("[0-9]?").matcher(str).matches());
    }
    return Boolean.valueOf(false);
  }
  
  private void setGuangBiao()
  {
    this.calcText.setSelection(this.calcText.length());
  }
}


/* Location:           D:\fxlandroid\document\app\AALifeNew_v3.1.4\classes_dex2jar.jar
 * Qualified Name:     com.aalife.android.MyCalcView
 * JD-Core Version:    0.7.0-SNAPSHOT-20130630
 */