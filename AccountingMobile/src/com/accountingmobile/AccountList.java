/*Created by ahmad chaaban*/
package com.accountingmobile;

import java.io.IOException;
import java.util.List;

import com.accountingmobile.categoryendpoint.Categoryendpoint;
import com.accountingmobile.categoryendpoint.model.Category;
import com.accountingmobile.categoryendpoint.model.CollectionResponseCategory;
import com.accountingmobile.expenseendpoint.Expenseendpoint;
import com.accountingmobile.expenseendpoint.model.CollectionResponseExpense;
import com.accountingmobile.expenseendpoint.model.Expense;
import com.accountingmobile.incomeendpoint.Incomeendpoint;
import com.accountingmobile.incomeendpoint.model.CollectionResponseIncome;
import com.accountingmobile.incomeendpoint.model.Income;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.json.jackson2.JacksonFactory;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.ListActivity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class AccountList extends ListActivity {
	protected AccountManager accountManager;
	protected Intent intent;
	private List<Category> mCategoryListEngine;	
	private List<Expense> mExpenseListEngine;
	private List<Income> mIncomeListEngine;
	
    /*
     * Called when the activity is first created. 
     *This activity lets the user select one of their accounts to authenticate with.
     *The Activity is a ListActivity,meaning it displays as a list view. 
     *To populate it, we instantiate a new AccountManager and call getAccountsByType on it, 
     *specifying that we want only accounts of type "com.google". 
     *Also,This activity get all data from datastore app engine and insert it on sqlite db.
     *When the user clicks on an account, we invoke the "MainActivity" activity, which will be responsible 
     *for getting the token and cookie, and displaying some info from our app

     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountManager = AccountManager.get(getApplicationContext());
        Account[] accounts = accountManager.getAccountsByType("com.google");
        this.setListAdapter(new ArrayAdapter<Account>(this, R.layout.list_item, accounts));        
        //new callTasksToFillSqlite().execute();
    }

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Account account = (Account)getListView().getItemAtPosition(position);
		Intent intent = new Intent(this, MainActivity.class);
		intent.putExtra("account", account);
		startActivity(intent);
	}
	
	private class callTasksToFillSqlite extends AsyncTask<Void, Void, Void> {
		
		   @Override
		   protected Void doInBackground(Void... params) {

			  /*code that calls the backend category*/
				   Categoryendpoint.Builder builder = new Categoryendpoint.Builder(
					         AndroidHttp.newCompatibleTransport(), new JacksonFactory(),
					         null);
					         
				   builder = CloudEndpointUtils.updateBuilder(builder);
				   CollectionResponseCategory result;
				   Categoryendpoint endpoint = builder.build();
		
				   /*code that calls the backend expense*/
				   Expenseendpoint.Builder expenseBuilder = new Expenseendpoint.Builder(
				          AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
				     
				   expenseBuilder = CloudEndpointUtils.updateBuilder(expenseBuilder);
				   CollectionResponseExpense resultExpense;
				   Expenseendpoint endpointExpense = expenseBuilder.build(); 
				   
				   /*code that calls the backend expense*/
				   Incomeendpoint.Builder incomeBuilder = new Incomeendpoint.Builder(
					          AndroidHttp.newCompatibleTransport(), new JacksonFactory(), null);
					     
				   incomeBuilder = CloudEndpointUtils.updateBuilder(incomeBuilder);
				   CollectionResponseIncome resultIncome;
				   Incomeendpoint endpointIncome = incomeBuilder.build();
				   
					     try {
				    	 /////////////////////////////////////////////////////////////////////////////
				    	 ///////Income sync code
				    	 ////////////////////////////////////////////////////////////////////////////
				    	 
					    	//get all expenses from engine datastore
					    	resultIncome = endpointIncome.listIncome().execute();
					    	mIncomeListEngine=resultIncome.getItems();
					    	
						   //test if list is empty in case no data in datastore  for avoiding erreur
						   if(!(mIncomeListEngine==null))
							 {
								 for(int i=0;i<mIncomeListEngine.size();i++) 
								 {
									 
									 mIncomeListEngine.get(i).setIncomekey(mIncomeListEngine.get(i).getKey().getId().longValue());
									 MainActivity.dbHandel.addIncome(mIncomeListEngine.get(i));
								 }
							 }
						    	   
						 /////////////////////////////////////////////////////////////////////////////
						 ///////Expense sync code
						 ////////////////////////////////////////////////////////////////////////////
						 
						//get all expenses from engine datastore
							resultExpense = endpointExpense.listExpense().execute();
							mExpenseListEngine=resultExpense.getItems();
		    	 
				    	 //test if list is empty in case no data in datastore  for avoiding erreur
					    	 if(!(mExpenseListEngine==null))
				    		 {
							     for(int i=0;i<mExpenseListEngine.size();i++) 
								 {
									 
							    	 mExpenseListEngine.get(i).setExpensekey(mExpenseListEngine.get(i).getKey().getId().longValue());
									 MainActivity.dbHandel.addExpense(mExpenseListEngine.get(i));
								 }
				    		 }
					    	
						 /////////////////////////////////////////////////////////////////////////////
						 ///////Category sync code
						 ////////////////////////////////////////////////////////////////////////////
						 
						//get all categories from engine datastore
							   result = endpoint.listCategory().execute();
							   mCategoryListEngine=result.getItems();
						 
						 //test if list is empty in case no data in datastore  for avoiding erreur
							 if(!(mCategoryListEngine==null))
							 {
								 for(int i=0;i<mCategoryListEngine.size();i++) 
								 {
									 
									 mCategoryListEngine.get(i).setCategorykey(mCategoryListEngine.get(i).getKey().getId().longValue());
									 MainActivity.dbHandel.addCategory(mCategoryListEngine.get(i));
								 }
							 }
						    	
					       
					     } catch (IOException e) {
					       // TODO Auto-generated catch block
					       e.printStackTrace();
					       
					     }
					     
					    
					     return null;   
			 		  
		   }
		   
		   
		   
		
		 }
		 

}