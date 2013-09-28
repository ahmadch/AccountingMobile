/*Created by ahmad chaaban*/
package com.accountingmobile;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.accountingmobile.R;
import com.accountingmobile.categoryendpoint.model.Category;


import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;
import org.apache.http.impl.client.DefaultHttpClient;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.os.Build;
import android.widget.Toast;
/*
 * The Home page that contain a  menu list
 */
@TargetApi(Build.VERSION_CODES.ECLAIR)
@SuppressLint("NewApi")
public class MainActivity extends Activity {
	DefaultHttpClient http_client = new DefaultHttpClient();
	protected static DatabaseHandler dbHandel ;
	ListView mainListView ;
	private ArrayAdapter<String> listAdapter ;
	private List<Category> mCategoryList;
	boolean	check;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		dbHandel = new DatabaseHandler(this);
		
		 // Find the ListView resource.   
	    mainListView = (ListView) findViewById( R.id.list_home );  
	  
	    // Create and populate a List of activity names.  
	    String[] array_home = new String[] { "Category", "Expense", "Income","Balance"};    
	    ArrayList<String> homeList = new ArrayList<String>();  
	    homeList.addAll( Arrays.asList(array_home) );  
	      
	    // Create ArrayAdapter using the home list.  
	    listAdapter = new ArrayAdapter<String>(this, R.layout.list_home_item,R.id.tvRowHome,homeList);  
	      
	    // Set the ArrayAdapter as the ListView's adapter.  
	    mainListView.setAdapter( listAdapter ); 
	    
	    mainListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

				switch (position)
				{
					case 0 : 						
						Intent CategoryIntent = new Intent(getBaseContext(), CategoryListActivity.class);
						startActivity(CategoryIntent);
						break;
					case 1 : 
						/*
						 * Get all categories from db:
						 * if no category found then get a message to fill a category before continue.
						 * if category found,check if at least one category not have a key engine then get msg to do a sync before continue
						 * else go to the expense activity
						 */
						mCategoryList=MainActivity.dbHandel.getAllCategory();
						if(mCategoryList.isEmpty())
						{
							Toast.makeText(MainActivity.this,"No category found,you must fill at least one!", Toast.LENGTH_SHORT).show();
						}
						else
						{
							if(CheckCategory())
							{
								Intent ExpenseIntent = new Intent(getBaseContext(), ExpenseListActivity.class);
								startActivity(ExpenseIntent);
							}
							else
							{
								Toast.makeText(MainActivity.this,"Categories need to sync before complete!", Toast.LENGTH_SHORT).show();
							}
						}
						
						break;
		    		case 2 :	
		    			/*
						 * Get all categories from db:
						 * if no category found then get a message to fill a category before continue.
						 * if category found,check if at least one category not have a key engine then get msg to do a sync before continue
						 * else go to the income activity
						 */
		    			mCategoryList=MainActivity.dbHandel.getAllCategory();
						if(mCategoryList.isEmpty())
						{
							Toast.makeText(MainActivity.this,"No category found,you must fill at least one!", Toast.LENGTH_SHORT).show();
						}
						else
						{
							if(CheckCategory())
							{
				    			Intent IncomeIntent = new Intent(getBaseContext(), IncomeListActivity.class);
								startActivity(IncomeIntent);
							}
							else
							{
								Toast.makeText(MainActivity.this,"Categories need to sync before complete!", Toast.LENGTH_SHORT).show();
							}
						}

		    			break;
		    		case 3 :	
		    			/*
						 * Get all categories from db:
						 * if no category found then get a message to fill a category before continue.
						 * if category found,check if at least one category not have a key engine then get msg to do a sync before continue
						 * else go to the bilan activity
						 */
		    			mCategoryList=MainActivity.dbHandel.getAllCategory();
						if(mCategoryList.isEmpty())
						{
							Toast.makeText(MainActivity.this,"No category found,you must fill at least one!", Toast.LENGTH_SHORT).show();
						}
						else
						{
							if(CheckCategory())
							{
				    			Intent BilanIntent = new Intent(getBaseContext(),BilanFilterActivty.class);
								startActivity(BilanIntent);
							}
							else
							{
								Toast.makeText(MainActivity.this,"Categories need to sync before complete!", Toast.LENGTH_SHORT).show();
							}
						}
		    			break;
				}
				
			}
		});
	    
	}
	
	/*
	 * check if at least one category not have a key engine
	 */
	protected boolean CheckCategory()
	{
		
		check=true;
		for(int i=0;i<mCategoryList.size();i++)
		{
			//Log.d("Name: ",String.valueOf(mCategoryList.get(i).getCategorykey()));
			if(String.valueOf(mCategoryList.get(i).getCategorykey()).equals("null"))
			{
				check=false;
				i=mCategoryList.size();
			}
			
		}
		return check;
		
	}
	
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater Inflater = getMenuInflater();
		Inflater.inflate(R.menu.menu, menu);
		MenuItem edit = menu.findItem(R.id.Edit);
		edit.setVisible(false);
		
		MenuItem delete = menu.findItem(R.id.Delete);
		delete.setVisible(false);
		MenuItem add = menu.findItem(R.id.Add);
		add.setVisible(false);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	
		if (item.getItemId() == R.id.Sync)
		{
			
			startActivity(new Intent(getBaseContext(), SyncActivity.class));
		}
		else if(item.getItemId() == R.id.Exit)
		{
			this.finish();
		    Intent intent = new Intent(Intent.ACTION_MAIN);
		    intent.addCategory(Intent.CATEGORY_HOME);
		    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		    startActivity(intent);
		}
		return true;
	}

	
	
	

}
