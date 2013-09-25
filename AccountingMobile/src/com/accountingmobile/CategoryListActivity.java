/*created by ahmad chaaban*/
package com.accountingmobile;

import java.util.List;
import com.accountingmobile.categoryendpoint.model.Category;



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
 * This activity is for display the list of categories
 */
public class CategoryListActivity extends Activity {
	
	private List<Category> mCategoryList;
	private EditText et;
	CategoryAdapter adapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_list);

	    et = (EditText)findViewById(R.id.etCategorySearch);
		 et.addTextChangedListener(new TextWatcher() {

	            @Override
	            public void onTextChanged(CharSequence s, int start, int before, int count) {
	      
	            	if (!mCategoryList.isEmpty())
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
		 
		 
		 
		 mCategoryList=MainActivity.dbHandel.getAllCategory();
		 if (mCategoryList.isEmpty())
	    	{
	    		showAlert("No Category found ,Add one!");
	    	}
	    	else
	    	{    		
	    		
	    	// Create the list
			ListView listViewCategory = (ListView)findViewById(R.id.list_category);
			 adapter=new CategoryAdapter(mCategoryList, getLayoutInflater());
			 
			 listViewCategory.setAdapter(adapter);
			 
			 
			 listViewCategory.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
						Category selectedCategory=(Category) adapter.getItem(position);
						CategoryActivity.currentCategory=selectedCategory;
						Intent CategoryIntent = new Intent(getBaseContext(),CategoryActivity.class);				
						startActivity(CategoryIntent);
						
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
			return true;
		}

		@Override
		public boolean onOptionsItemSelected(MenuItem item) {
		
			if (item.getItemId() == R.id.Add)
			{
				CategoryFormActivity.updatedCategory=null;
				startActivity(new Intent(getBaseContext(), CategoryFormActivity.class));
			}
			
			return true;
		}
		
		
	    public void showAlert(final String msg){
	    	CategoryListActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryListActivity.this);
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