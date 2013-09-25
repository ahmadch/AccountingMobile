/*created by ahmad chaaban*/
package com.accountingmobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import com.accountingmobile.categoryendpoint.model.Category;
import com.accountingmobile.incomeendpoint.model.Income;
import com.google.api.client.util.DateTime;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View.OnClickListener;
import android.widget.Spinner;

/*
 * This activity created for update or insert an expense.
 */

@SuppressLint("SimpleDateFormat")
public class IncomeFormActivity extends Activity implements
OnItemSelectedListener{
	protected static Income updatedIncome;
	Income income;
	private List<Category> mCategoryList;
	EditText etIncomeName;
	EditText etIncomePayer;
	EditText etIncomeAmount;
	TextView tvIncomeDate;
	Spinner spinnerIncomeCategory;
	Spinner spinner_payment;
	static final int DATE_DIALOG_ID = 999;
	int id_from,id_to,id_test;
	private int year;
	private int month;
	private int day;
	long add_cat_id; 
	String paymtType;
	SimpleDateFormat dateFormat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.income_form);
		
		etIncomeName = (EditText)findViewById(R.id.etIncomeName);
		etIncomeAmount = (EditText)findViewById(R.id.etIncomeAmount);
		etIncomePayer=(EditText) findViewById(R.id.etIncomePayer);
		tvIncomeDate=(TextView) findViewById(R.id.tvIncomeDate);
		spinner_payment = (Spinner) findViewById(R.id.spinner_payment);
		spinnerIncomeCategory = (Spinner) findViewById(R.id.spinnerIncomeCategory);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		loadSpinnerData();
		spinnerIncomeCategory.setOnItemSelectedListener(this);
		spinner_payment.setOnItemSelectedListener(this);
		tvIncomeDate.setOnClickListener(new OnClickListener() {
			 
			   
			@Override
			   public void onClick(View v) {
				   id_test=v.getId();
				   showDialog(DATE_DIALOG_ID);
			   }
		});
		
		
		if (updatedIncome!=null){
			etIncomeName.setText(updatedIncome.getName());
			etIncomeAmount.setText(updatedIncome.getPrice().toString());
			etIncomePayer.setText(updatedIncome.getPayer().toString());
			tvIncomeDate.setText(updatedIncome.getIncomeDate().toString().substring(0, 10));

			
		}
		else
		{
			etIncomeName.setText("");
			etIncomeAmount.setText("");
			etIncomePayer.setText("");
			tvIncomeDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(year).append("-").append(month + 1).append("-")
				.append(day).append(" "));
			
			
		}
		
		
		
		
		 Button btnSave = (Button) findViewById(R.id.Btn_save_income);
		 btnSave.setOnClickListener(new OnClickListener() {
		 
		   @Override
		   public void onClick(View v) {
		 
			   
			   if(etIncomeName.getText().toString().trim().equals("")||
					   etIncomeAmount.getText().toString().trim().equals("")||
					   etIncomePayer.getText().toString().trim().equals(""))
							   
			   {
						   showAlert("Fields are required!");
			   }
			   else
			   {		   
				   
				   if (updatedIncome!=null)
				   {
							 
					   updatedIncome.setName(etIncomeName.getText().toString().trim());
					   updatedIncome.setPayer(etIncomePayer.getText().toString().trim());
					   updatedIncome.setPrice(Double.parseDouble(etIncomeAmount.getText().toString().trim()));
					   try {
						   		updatedIncome.setIncomeDate(new DateTime(dateFormat.parse(tvIncomeDate.getText().toString().trim())));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							   MainActivity.dbHandel.updateIncome(updatedIncome);
							   Toast.makeText(IncomeFormActivity.this,"Income Updated!", Toast.LENGTH_SHORT).show();
							   
				   }
				   else
				   {			
					   income= new Income();  	  
					   income.setName(etIncomeName.getText().toString().trim());
					   income.setPayer(etIncomePayer.getText().toString().trim());
					   income.setPrice(Double.parseDouble(etIncomeAmount.getText().toString().trim()));
					   income.setCategoryId(add_cat_id);
					   income.setPaymentType(paymtType);
					   try {
						   		income.setIncomeDate(new DateTime(dateFormat.parse(tvIncomeDate.getText().toString().trim())));
						   		income.setCreatedDate(new DateTime(dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()))));
							} catch (ParseException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
							}
					   MainActivity.dbHandel.addIncome(income);
					   Toast.makeText(IncomeFormActivity.this,"Income Added!", Toast.LENGTH_SHORT).show();

					}
				   Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
					startActivity(MainIntent);

		   }
			   
		 }
		  });
		  
	}
	
		 
	 /*building the calendar*/
	 @Override
		protected Dialog onCreateDialog(int id) {
			switch (id) {
			case DATE_DIALOG_ID:
			   // set date picker as current date
			   return new DatePickerDialog(this, datePickerListener, 
	                         year, month,day);
			}
			return null;
		}
	 
		private DatePickerDialog.OnDateSetListener datePickerListener 
	                = new DatePickerDialog.OnDateSetListener() {
	 
			// when dialog box is closed, below method will be called.
			public void onDateSet(DatePicker view, int selectedYear,
					int selectedMonth, int selectedDay) {
				year = selectedYear;
				month = selectedMonth;
				day = selectedDay;
	 
				// set selected date into text view
				if (id_test==R.id.tvIncomeDate)
				{
					tvIncomeDate.setText(new StringBuilder().append(year)
				   .append("-").append(month + 1).append("-").append(day)
				   .append(" "));
				}
			
				
	 
			}
		};
		
	
			  
		   /**
		     * Function to load the spinner data from the category entity
		     * */
		    private void loadSpinnerData() {
		    	
		    	mCategoryList= MainActivity.dbHandel.getAllCategory();
		    	SpinnerCategory[] objectArray = new SpinnerCategory[mCategoryList.size()];
		    	
		    	for (int x = 0; x <mCategoryList.size(); x++) {
		    	 
		    		objectArray[x] = new SpinnerCategory(mCategoryList.get(x).getCategoryId(),
		    				 mCategoryList.get(x).getName(),mCategoryList.get(x).getCategorykey());
		    	   
		    	  
		    	}
		    	
		    	// Creating adapter for spinnerIncomeCategory
		        ArrayAdapter<SpinnerCategory> dataAdapter = new ArrayAdapter<SpinnerCategory>(this,
		                android.R.layout.simple_spinner_item,objectArray);
		        
		        // Drop down layout style - list view with radio button
		        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 
		        // attaching data adapter to spinner
		        spinnerIncomeCategory.setAdapter(dataAdapter);
		        
		        
		        
		        String[] PaymentMethod=new String[2];
		        PaymentMethod[0]="Check";
		        PaymentMethod[1]="Cash";
		        
		        // Creating adapter for spinner_payment
		        ArrayAdapter<String> PaydataAdapter = new ArrayAdapter<String>(this,
		                android.R.layout.simple_spinner_item,PaymentMethod);
		        
		        // Drop down layout style - list view with radio button
		        PaydataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 
		        // attaching data adapter to spinner
		        spinner_payment.setAdapter(PaydataAdapter);
		        
		        
		        
		        
		        if (updatedIncome!=null)
				{	
		        	for(int i = 0; i < objectArray.length; ++i) {
		                if(objectArray[i].cat_key == updatedIncome.getCategoryId())
		                		{
		                			spinnerIncomeCategory.setSelection(i);
		                			i=objectArray.length;
		                		}
		            }
		        	
		        	for(int i = 0; i < PaymentMethod.length; ++i) {
		        		if(PaymentMethod[i].equals(updatedIncome.getPaymentType()))
		        		{
		        			spinner_payment.setSelection(i);
		        			i=PaymentMethod.length;
		        		}
		        	}
				}
		        
		        

		    }
		    
		    
		    
		    
		    /* on item selected on spinner*/
		    
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				Spinner spin_cat = (Spinner)arg0;
		        Spinner spin_pay = (Spinner)arg0;
				if(spin_cat.getId()==R.id.spinnerIncomeCategory)
				{
					SpinnerCategory st = (SpinnerCategory)spinnerIncomeCategory.getSelectedItem();
					if (updatedIncome!=null)
					{
						updatedIncome.setCategoryId(st.cat_key);
					}
					else
					{
						add_cat_id=st.cat_key;
					}
				}
				
				if(spin_pay.getId()==R.id.spinner_payment)
				{
					String payType=(String)spinner_payment.getSelectedItem();
					if (updatedIncome!=null)
					{
						updatedIncome.setPaymentType(payType);
					}
					else
					{
						paymtType=payType;
					}
				}
				
				
			}






			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				Spinner spin_cat = (Spinner)arg0;
		        Spinner spin_pay = (Spinner)arg0;
				if(spin_cat.getId()==R.id.spinnerIncomeCategory)
				{
					if (updatedIncome==null)
					{
						add_cat_id= arg0.getAdapter().getItemId(2);
					}
				}
				if(spin_pay.getId()==R.id.spinner_payment)
				{
					if (updatedIncome==null)
					{
						paymtType=arg0.getAdapter().getItem(0).toString();
					}
				}
				
			}
		    

		    
			 public void showAlert(final String msg){
			    	IncomeFormActivity.this.runOnUiThread(new Runnable() {
			            public void run() {
			                AlertDialog.Builder builder = new AlertDialog.Builder(IncomeFormActivity.this);
			                builder.setTitle("Error");
			                builder.setMessage(msg)
			                       .setCancelable(false)
			                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
			                           public void onClick(DialogInterface dialog, int id) {
			                           }
			                       });                     
			                AlertDialog alert = builder.create();
			                alert.show();               
			            }
			        });
			    }
		 
		 

		}
