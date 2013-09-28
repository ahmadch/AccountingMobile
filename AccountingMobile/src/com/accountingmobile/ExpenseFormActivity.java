/*created by ahmad chaaban*/
package com.accountingmobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import com.accountingmobile.R;
import com.accountingmobile.categoryendpoint.model.Category;
import com.accountingmobile.expenseendpoint.model.Expense;
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
public class ExpenseFormActivity extends Activity implements
OnItemSelectedListener{
	protected static Expense updatedExpense;
	Expense expense;
	private List<Category> mCategoryList;
	EditText etExpenseName;
	EditText etExpenseAmount;
	TextView tvExpenseDate;
	Spinner spinner;
	static final int DATE_DIALOG_ID = 999;
	int id_from,id_to,id_test;
	private int year;
	private int month;
	private int day;
	long add_cat_id; 
	SimpleDateFormat dateFormat;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_form);
		
		etExpenseName = (EditText)findViewById(R.id.etExpenseName);
		etExpenseAmount = (EditText)findViewById(R.id.etExpenseAmount);
		tvExpenseDate=(TextView) findViewById(R.id.tvExpenseDate);
		spinner = (Spinner) findViewById(R.id.spinner);
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
		dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		loadSpinnerData();
		spinner.setOnItemSelectedListener(this);
		tvExpenseDate.setOnClickListener(new OnClickListener() {
			 
			   
			@Override
			   public void onClick(View v) {
				   id_test=v.getId();
				   showDialog(DATE_DIALOG_ID);
			   }
		});
		
		
		if (updatedExpense!=null){
			etExpenseName.setText(updatedExpense.getName());
			etExpenseAmount.setText(updatedExpense.getPrice().toString());
			tvExpenseDate.setText(updatedExpense.getExpenseDate().toString().substring(0, 10));
		}
		else
		{
			etExpenseName.setText("");
			etExpenseAmount.setText("");
			tvExpenseDate.setText(new StringBuilder()
				// Month is 0 based, just add 1
				.append(year).append("-").append(month + 1).append("-")
				.append(day).append(" "));
			
			
		}
		
		
		
		
		 Button btnSave = (Button) findViewById(R.id.Btn_save_expense);
		 btnSave.setOnClickListener(new OnClickListener() {
		 
		   @Override
		   public void onClick(View v) {
		 
			   if(etExpenseName.getText().toString().trim().equals("")||
				  etExpenseAmount.getText().toString().trim().equals(""))
					   
			   {
				   showAlert("Fields are required!");
			   }
			   else
				  
				   if (updatedExpense!=null)
				   {
					   updatedExpense.setName(etExpenseName.getText().toString().trim());
						
					   updatedExpense.setPrice(Double.parseDouble(etExpenseAmount.getText().toString().trim()));
					   try {
						updatedExpense.setExpenseDate(new DateTime(dateFormat.parse(tvExpenseDate.getText().toString().trim())));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					   MainActivity.dbHandel.updateExpense(updatedExpense);
					   Toast.makeText(ExpenseFormActivity.this,"Expense Updated!", Toast.LENGTH_SHORT).show();

				   }
				   else
				   {
					   expense= new Expense();  	  
					   expense.setName(etExpenseName.getText().toString().trim());
					   expense.setPrice(Double.parseDouble(etExpenseAmount.getText().toString().trim()));
					   expense.setCategoryId(add_cat_id);
					   try {
						   expense.setExpenseDate(new DateTime(dateFormat.parse(tvExpenseDate.getText().toString().trim())));
						   expense.setCreatedDate(new DateTime(dateFormat.parse(dateFormat.format(Calendar.getInstance().getTime()))));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					   MainActivity.dbHandel.addExpense(expense);
					   Toast.makeText(ExpenseFormActivity.this,"Expense Added!", Toast.LENGTH_SHORT).show();

				   }
			   
			   		Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
			   		startActivity(MainIntent);
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
				if (id_test==R.id.tvExpenseDate)
				{
					tvExpenseDate.setText(new StringBuilder().append(year)
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
		    	
		    	// Creating adapter for spinner
		        ArrayAdapter<SpinnerCategory> dataAdapter = new ArrayAdapter<SpinnerCategory>(this,
		                android.R.layout.simple_spinner_item,objectArray);
		        
		        // Drop down layout style - list view with radio button
		        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 
		        // attaching data adapter to spinner
		        spinner.setAdapter(dataAdapter);
		        
		        if (updatedExpense!=null)
				{	
		        	for(int i = 0; i < objectArray.length; ++i) {
		                if(objectArray[i].cat_key == updatedExpense.getCategoryId())
		                		{
		                			spinner.setSelection(i);
		                			i=objectArray.length;
		                		}
		            }
				}
		    }
		    
		    
		    
		    
		    /* on item selected on spinner*/
		    
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				SpinnerCategory st = (SpinnerCategory)spinner.getSelectedItem();
				if (updatedExpense!=null)
				{
					updatedExpense.setCategoryId(st.cat_key);
				}
				else
				{
					add_cat_id=st.cat_key;
				}
				
			}






			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
				if (updatedExpense==null)
				{
					add_cat_id= (Long) arg0.getAdapter().getItem(2);
				}
			}
		    
		    
		    
		    
		  		    
			 public void showAlert(final String msg){
			    	ExpenseFormActivity.this.runOnUiThread(new Runnable() {
			            public void run() {
			                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseFormActivity.this);
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
