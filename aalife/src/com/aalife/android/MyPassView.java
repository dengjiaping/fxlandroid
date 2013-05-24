package com.aalife.android;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class MyPassView extends LinearLayout {
	public EditText passText;
	public Button btnOk;

	public MyPassView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		
		LayoutInflater inflater = LayoutInflater.from(context);
		View popView = inflater.inflate(R.layout.passlayout, null);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		popView.setLayoutParams(params);
		
		passText = (EditText) popView.findViewById(R.id.passtext);		
		btnOk = (Button) popView.findViewById(R.id.buttonok);
		
		Button btn1 = (Button) popView.findViewById(R.id.button1);
		btn1.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "1");
			}
		});
		Button btn2 = (Button) popView.findViewById(R.id.button2);
		btn2.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "2");
			}
		});
		Button btn3 = (Button) popView.findViewById(R.id.button3);
		btn3.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "3");
			}
		});
		Button btn4 = (Button) popView.findViewById(R.id.button4);
		btn4.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "4");
			}
		});
		Button btn5 = (Button) popView.findViewById(R.id.button5);
		btn5.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "5");
			}
		});
		Button btn6 = (Button) popView.findViewById(R.id.button6);
		btn6.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "6");
			}
		});
		Button btn7 = (Button) popView.findViewById(R.id.button7);
		btn7.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "7");
			}
		});
		Button btn8 = (Button) popView.findViewById(R.id.button8);
		btn8.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "8");
			}
		});
		Button btn9 = (Button) popView.findViewById(R.id.button9);
		btn9.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "9");
			}
		});
		Button btn0 = (Button) popView.findViewById(R.id.button0);
		btn0.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				String s = passText.getText().toString();
				passText.setText(s + "0");
			}
		});
		Button btnDel = (Button) popView.findViewById(R.id.buttondel);
		btnDel.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View view) {
				passText.setText("");
			}
		});
		
		this.addView(popView);
	}

}
