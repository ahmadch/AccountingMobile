/*created by ahmad chaaban*/
package com.accountingmobile;

import java.util.List;

import com.accountingmobile.R;
import com.accountingmobile.expenseendpoint.model.Expense;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

/*
 * This activity is for display the list of expenses
 */
public class ExpenseListActivity extends Activity{
	private List<Expense> mExpenseList;
	private EditText et;
	ExpenseAdapter adapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense_list);

	    et = (EditText)findViewById(R.id.etExpenseSearch);
		 et.addTextChangedListener(new TextWatcher() {

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	      
	            	if (!mExpenseList.isEmpty())
	            	{
	            	adapter.getFilter().filter(s);
	            	}
	            	
	            }

	            @Override
	            public void beforeTextChanged(CharSequence s, int start, int count,
	                    int after) {
	                // TODO Auto-generated method stub

	            }

	            @Override
	            public void afterTextChanged(Editable s) {
	                // TODO Auto-generated method stub

	            }
	        });
		 
		 mExpenseList=MainActivity.dbHandel.getAllExpense();
		 if (mExpenseList.isEmpty())
	    	{
	    		showAlert("No Expense found ,Add one!");
	    	}
	    	else
	    	{    	
	    		// Create the list
				ListView listViewExpense = (ListView)findViewById(R.id.list_expense);
				 adapter=new ExpenseAdapter(mExpenseList, getLayoutInflater());
				 
				 listViewExpense.setAdapter(adapter);
				 
				 
				 listViewExpense.setOnItemClickListener(new OnItemClickListener() {

						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
							Expense selectedExpense=(Expense) adapter.getItem(position);
							ExpenseActivity.currentExpense=selectedExpense;
							Intent ExpenseIntent = new Intent(getBaseContext(),ExpenseActivity.class);				
							startActivity(ExpenseIntent);
							
						}
					});

	    	}
		
	}	
	
	 
	  
	  public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater Inflater = getMenuInflater();
			Inflater.inflate(R.menu.menu, menu);
			MenuItem edit = menu.findItem(R.id.Edit);
			edit.setVisible(false);
			
			MenuItem delete = menu.findItem(R.id.Delete);
			delete.setVisible(false);
			
			MenuItem sync = menu.findItem(R.id.Sync);
			sync.setVisible(false);
			
			MenuItem exit = menu.findItem(R.id.Exit);
			exit.setVisible(false);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		
			if (item.getItemId() == R.id.Add)
			{
				ExpenseFormActivity.updatedExpense=null;
				startActivity(new Intent(getBaseContext(), ExpenseFormActivity.class));
			}
			
			return true;
		}
		
		 public void showAlert(final String msg){
		    	ExpenseListActivity.this.runOnUiThread(new Runnable() {
		            public void run() {
		                AlertDialog.Builder builder = new AlertDialog.Builder(ExpenseListActivity.this);
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
