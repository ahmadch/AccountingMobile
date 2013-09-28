/*created by ahmad chaaban*/
package com.accountingmobile;

import java.util.List;

import com.accountingmobile.R;
import com.accountingmobile.categoryendpoint.model.Category;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.Spinner;

/*
 * This activity make a filter by category to fill the tabs.
 */

@SuppressLint("SimpleDateFormat")
public class BilanFilterActivty extends Activity implements
OnItemSelectedListener{
	private List<Category> mCategoryList;
	Spinner spinner;
	protected static long cat_id; 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bilan_filter);
				
		spinner = (Spinner) findViewById(R.id.spinnerFilterCategory);
		loadSpinnerData();
		spinner.setOnItemSelectedListener(this);

		 Button btnFilter = (Button) findViewById(R.id.Btn_filter);
		 btnFilter.setOnClickListener(new OnClickListener() {
		 
		   @Override
		   public void onClick(View v) {
		 
			   Intent BilanIntent = new Intent(getBaseContext(),BilanActivity.class);
			   startActivity(BilanIntent);
		   }
		  });
		  
	}
	
			
				  
		   /**
		     * Function to load the spinner data from the category entity
		     * */
		    private void loadSpinnerData() {
		      
		    	mCategoryList= MainActivity.dbHandel.getAllCategory();
		    	SpinnerCategory[] objectArray = new SpinnerCategory[mCategoryList.size()+1];
		    	objectArray[0]=new SpinnerCategory(Integer.parseInt("1"),"All",Long.parseLong("1"));
		    	for (int x = 0; x <mCategoryList.size(); x++) {
		    	 
		    		objectArray[x+1] = new SpinnerCategory(mCategoryList.get(x).getCategoryId(),
		    				 mCategoryList.get(x).getName(),mCategoryList.get(x).getCategorykey());
		    	   
		    	  
		    	}
		    	
		    	// Creating adapter for spinner
		        ArrayAdapter<SpinnerCategory> dataAdapter = new ArrayAdapter<SpinnerCategory>(this,
		                android.R.layout.simple_spinner_item,objectArray);
		        
		        // Drop down layout style - list view with radio button
		        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		 
		        // attaching data adapter to spinner
		        spinner.setAdapter(dataAdapter);
		        
		        
		    }
		    
		    
		    
		    
		    /* on item selected on spinner*/
		    
			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				SpinnerCategory st = (SpinnerCategory)spinner.getSelectedItem();
				cat_id=st.cat_key;

			}


			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
				SpinnerCategory st = (SpinnerCategory)spinner.getSelectedItem();
					cat_id=st.cat_key;
				
			}
		    
		 

		}

