/*created by ahmad chaaban*/
package com.accountingmobile;

import com.accountingmobile.categoryendpoint.model.Category;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

/*
 * This activity display a category data when clicking on a category item from the category list.
 */
public class CategoryActivity extends Activity {
	protected static Category currentCategory;
	TextView tvCategoryName;
	TextView tvCategoryDesc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category);
		
		tvCategoryDesc = (TextView) findViewById(R.id.tvCategoryDesc);
		tvCategoryName = (TextView) findViewById(R.id.tvCategoryName);
		tvCategoryName.setText(currentCategory.getName());
		tvCategoryDesc.setText(currentCategory.getDescription());
		
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
				
				CategoryFormActivity.updatedCategory=currentCategory;
				Intent CategoryFormIntent = new Intent(getBaseContext(),CategoryFormActivity.class);
				startActivity(CategoryFormIntent);
			}
			else if (item.getItemId() == R.id.Delete)
			{
				/*
				 * If the category not have the key engine then delete it directly.
				 * If the category have the key engine and not have a child(expense and income) then save the key engine(for the sync process) and delete it.
				 *If the category have the key engine and  have a child then get a message to delete first the child .
				 */
				if(currentCategory.getCategorykey().equals("null"))
				{
					MainActivity.dbHandel.deleteCategory(currentCategory);
					Toast.makeText(CategoryActivity.this,"Category Deleted!", Toast.LENGTH_SHORT).show();
					Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
					startActivity(MainIntent);
				}
				else
				{
					if(MainActivity.dbHandel.getAllExpenseByCategory(currentCategory.getCategorykey()).isEmpty()
							&&
						MainActivity.dbHandel.getAllIncomeByCategory(currentCategory.getCategorykey()).isEmpty())
					{
						Globals g = Globals.getInstance();
						g.setDelCatKey(currentCategory.getCategorykey());
						MainActivity.dbHandel.deleteCategory(currentCategory);
						Toast.makeText(CategoryActivity.this,"Category Deleted!", Toast.LENGTH_SHORT).show();
						Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
						startActivity(MainIntent);
					}
					else
					{
						Toast.makeText(CategoryActivity.this,"You should delete the related income or expense before delete this category!", Toast.LENGTH_SHORT).show();
					}
					
				}
				
				

			}
			
			return true;
		}
		
		
}