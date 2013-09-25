/*created by ahmad chaaban*/
package com.accountingmobile;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.accountingmobile.categoryendpoint.model.Category;
import com.accountingmobile.expenseendpoint.model.Expense;

/*
 * This activity display an expense data when clicking on a expense item from the expense list.
 */
public class ExpenseActivity extends Activity {
	protected static Expense currentExpense;
	TextView tvExpenseName;
	TextView tvExpenseAmount;
	TextView tvExpenseDate;
	TextView tvExpenseCategory;
	TextView tvCreatedDate;
	Category category;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.expense);
		
		tvExpenseName = (TextView) findViewById(R.id.tvExpenseName);
		tvExpenseAmount = (TextView) findViewById(R.id.tvExpenseAmount);
		tvExpenseDate = (TextView) findViewById(R.id.tvExpenseDate);
		tvCreatedDate = (TextView) findViewById(R.id.tvCreatedDate);
		
		tvExpenseName.setText(currentExpense.getName());
		tvExpenseAmount.setText(currentExpense.getPrice().toString());
		tvExpenseDate.setText(currentExpense.getExpenseDate().toString().substring(0, 10));
		
		tvCreatedDate.setText(currentExpense.getCreatedDate().toString().substring(0, 10));
		
		tvExpenseCategory = (TextView) findViewById(R.id.tvExpenseCategory);
		tvExpenseCategory.setText(MainActivity.dbHandel.getCategoryByKey(currentExpense.getCategoryId()).getName());
		
		
	}
	
	 public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater Inflater = getMenuInflater();
			Inflater.inflate(R.menu.menu, menu);
			MenuItem item = menu.findItem(R.id.Add);
			item.setVisible(false);
			
			MenuItem sync = menu.findItem(R.id.Sync);
			sync.setVisible(false);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		
			 if (item.getItemId() == R.id.Edit) {
				
				ExpenseFormActivity.updatedExpense=currentExpense;
				Intent CategoryFormIntent = new Intent(getBaseContext(),ExpenseFormActivity.class);
				startActivity(CategoryFormIntent);
			}
			else if (item.getItemId() == R.id.Delete)
			{
				/*
				 * If the expense have the key engine then save the key engine(for the sync process) and delete it.
				 */
				if(!currentExpense.getExpensekey().equals("null"))
				{
					Globals g = Globals.getInstance();
					g.setDelExpKey(currentExpense.getExpensekey());
				}
				MainActivity.dbHandel.deleteExpense(currentExpense);
				  Toast.makeText(ExpenseActivity.this,"Expense Deleted!", Toast.LENGTH_SHORT).show();
				Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
				startActivity(MainIntent);
			}
			
			return true;
		}
		

}