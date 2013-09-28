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

import com.accountingmobile.R;
import com.accountingmobile.categoryendpoint.model.Category;
import com.accountingmobile.incomeendpoint.model.Income;

/*
 * This activity display an income data when clicking on a income item from the income list.
 */

public class IncomeActivity extends Activity {
	protected static Income currentIncome;
	TextView tvIncomeName;
	TextView tvIncomeAmount;
	TextView tvIncomeDate;
	TextView tvIncomeCategory;
	TextView tvCreatedDate;
	TextView tvPaymentMethod;
	TextView tvPayer;
	Category category;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.income);
		
		tvIncomeName = (TextView) findViewById(R.id.tvIncomeName);
		tvIncomeAmount = (TextView) findViewById(R.id.tvIncomeAmount);
		tvIncomeDate = (TextView) findViewById(R.id.tvIncomeDate);
		tvCreatedDate = (TextView) findViewById(R.id.tvCreatedDate);
		tvPaymentMethod = (TextView) findViewById(R.id.tvPaymentMethod);
		tvPayer = (TextView) findViewById(R.id.tvPayer);
		
		tvIncomeName.setText(currentIncome.getName());
		tvIncomeAmount.setText(currentIncome.getPrice().toString());
		tvIncomeDate.setText(currentIncome.getIncomeDate().toString().substring(0, 10));	
		tvCreatedDate.setText(currentIncome.getCreatedDate().toString().substring(0, 10));
		tvPaymentMethod.setText(currentIncome.getPaymentType());
		tvPayer.setText(currentIncome.getPayer());

		tvIncomeCategory = (TextView) findViewById(R.id.tvIncomeCategory);
		tvIncomeCategory.setText(MainActivity.dbHandel.getCategoryByKey(currentIncome.getCategoryId()).getName());

		
	}
	
	 public boolean onCreateOptionsMenu(Menu menu) {
			MenuInflater Inflater = getMenuInflater();
			Inflater.inflate(R.menu.menu, menu);
			MenuItem item = menu.findItem(R.id.Add);
			item.setVisible(false);
			
			MenuItem sync = menu.findItem(R.id.Sync);
			sync.setVisible(false);
			
			MenuItem exit = menu.findItem(R.id.Exit);
			exit.setVisible(false);
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		
		    if (item.getItemId() == R.id.Edit) {
				
				IncomeFormActivity.updatedIncome=currentIncome;
				Intent IncomeFormIntent = new Intent(getBaseContext(),IncomeFormActivity.class);
				startActivity(IncomeFormIntent);
			}
			else if (item.getItemId() == R.id.Delete)
			{
				/*
				 * If the income have the key engine then save the key engine(for the sync process) and delete it.
				 */
				if(!currentIncome.getIncomekey().equals("null"))
				{
					Globals g = Globals.getInstance();
					g.setDelExpKey(currentIncome.getIncomekey());
				}
				MainActivity.dbHandel.deleteIncome(currentIncome);
			    Toast.makeText(IncomeActivity.this,"Income Deleted!", Toast.LENGTH_SHORT).show();
				Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
				startActivity(MainIntent);
			}
			
			return true;
		}
		
}