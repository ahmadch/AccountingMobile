/*created by ahmad chaaban*/
package com.accountingmobile;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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
import com.google.api.client.util.DateTime;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
/*
 * Make a sync between sqlite database and datastore app engine
 */
@SuppressLint("SimpleDateFormat")
public class SyncActivity extends Activity{
	Button btnSync;
	private List<Category> mCategoryListSqlite;
	private List<Category> mCategoryListEngine;
	private List<Expense> mExpenseListSqlite;
	private List<Expense> mExpenseListEngine;
	private List<Income> mIncomeListSqlite;
	private List<Income> mIncomeListEngine;
	boolean exist_cat;
	boolean exist_exp;
	boolean exist_inc;
	SimpleDateFormat dateFormat;
	
	// Progress Dialog
		private ProgressDialog pDialog;
	// Progress dialog type (0 - for Horizontal progress bar)
		public static final int progress_bar_type = 0; 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_sync);
		
		
		
		btnSync = (Button) findViewById(R.id.btnSync);
		btnSync.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				 Globals g = Globals.getInstance();
				 if(g.getCheck_Connection())
				 {
					 new callTaskCategories().execute();
				 }
				 else
				 {
					 Toast.makeText(SyncActivity.this,"No Connection Found!", Toast.LENGTH_SHORT).show();
				 }

			}
		});
		
	}
	
	/**
	 * Showing Dialog
	 * */
	@Override
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case progress_bar_type:
			pDialog = new ProgressDialog(this);
			pDialog.setMessage("Syncing Data. Please wait...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
			return pDialog;
		default:
			return null;
		}
	}
	 private class callTaskCategories extends AsyncTask<Void, Integer, Void> {
		 
		 
		 /**
			 * Before starting background thread
			 * Show Progress Bar Dialog
			 * */
			@Override
			protected void onPreExecute() {
				super.onPreExecute();
				showDialog(progress_bar_type);
			}
			
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

			   publishProgress(10); //10% done
			   
			   dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				     try {
			    	 /////////////////////////////////////////////////////////////////////////////
			    	 ///////Income sync code
			    	 ////////////////////////////////////////////////////////////////////////////
			    	 //delete all expenses saved in the global list from the engine datastore
			    	 Globals g = Globals.getInstance();
				    	List<Long> inc_deleted=g.getDelIncKey();
				    	for(int i=0;i<inc_deleted.size();i++)
				    	{
				    		endpointIncome.removeIncome(inc_deleted.get(i)).execute();
				    	}
				    	g.ClearDelIncKey();
				    	
				    	//get all expenses from engine datastore
				    	resultIncome = endpointIncome.listIncome().execute();
				    	mIncomeListEngine=resultIncome.getItems();
			    	 
				    	//get all expenses from sqlite database	   
				    		mIncomeListSqlite=MainActivity.dbHandel.getAllIncome();
				    		
				    		/*sync between sqllite and engine datastore by insert or update:
					    	 1-fetch the sqlite incomes list :
					    	 	if income key is null then insert in engine datastore
					    	 	else
					    	 		1- if income still exist in engine datastore then update
					    	 		2-if not exist then insert in engine store(in case another user delete this key from engine datastore)
					    	 */
					    	 for(int i=0;i<mIncomeListSqlite.size();i++)
					    	 {
					    		 if(String.valueOf(mIncomeListSqlite.get(i).getIncomekey()).equals("null"))
				    			 {
					    			 endpointIncome.insertIncome(mIncomeListSqlite.get(i)).execute();
				    			 }
					    		 else
					    		 {
					    			 exist_inc=false;
					    			//test if list is empty in case no data in datastore for avoiding erreur
					    			 if(!(mIncomeListEngine==null))
					    			 {
						    			 for(int j=0;j<mIncomeListEngine.size();j++)
							    		 {
							    			 if(mIncomeListSqlite.get(i).getIncomekey()==mIncomeListEngine.get(j).getKey().getId().longValue())
							    			 {
							    				 exist_inc=true;
							    				 mIncomeListEngine.get(j).setName(mIncomeListSqlite.get(i).getName());
							    				 mIncomeListEngine.get(j).setPrice(mIncomeListSqlite.get(i).getPrice());
							    				 mIncomeListEngine.get(j).setCategoryId(mIncomeListSqlite.get(i).getCategoryId());
							    				 mIncomeListEngine.get(j).setPayer(mIncomeListSqlite.get(i).getPayer());
							    				 mIncomeListEngine.get(j).setPaymentType(mIncomeListSqlite.get(i).getPaymentType());
							    				 try {
							    					 mIncomeListEngine.get(j).setIncomeDate(new DateTime(dateFormat.parse(mIncomeListSqlite.get(i).getIncomeDate().toString().trim())));
							 						
							 					} catch (ParseException e) {
							 						// TODO Auto-generated catch block
							 						e.printStackTrace();
							 					}
							    				 endpointIncome.updateIncome(mIncomeListEngine.get(j)).execute();
							    				 j=mIncomeListEngine.size();
							    			 }
							
							    		 }
					    			 }
					    			 if(!exist_inc)
					    			 {
					    				 endpointIncome.insertIncome(mIncomeListSqlite.get(i)).execute();
					    			 }
					    		 }
					    		
					    	 }
					    	 publishProgress(30); //30% done
					    	//get the list of incomes synced from engine store
					    	 resultIncome = endpointIncome.listIncome().execute();
					    	 mIncomeListEngine=resultIncome.getItems();
					    	   
					    	   //delete all incomes in sqllite and then fill it from the engine store 
					    	   MainActivity.dbHandel.deleteAllIncome();
					    	   //test if list is empty in case no data in datastore  for avoiding erreur
					    	   if(!(mIncomeListEngine==null))
				    			 {
								     for(int i=0;i<mIncomeListEngine.size();i++) 
									 {
										 
								    	 mIncomeListEngine.get(i).setIncomekey(mIncomeListEngine.get(i).getKey().getId().longValue());
										 MainActivity.dbHandel.addIncome(mIncomeListEngine.get(i));
									 }
				    			 }
					    	   publishProgress(40); //40% done
	    	 /////////////////////////////////////////////////////////////////////////////
	    	 ///////Expense sync code
	    	 ////////////////////////////////////////////////////////////////////////////
	    	 //delete all expenses saved in the global list from the engine datastore
	    	 //Globals g = Globals.getInstance();
		    	List<Long> exp_deleted=g.getDelExpKey();
		    	for(int i=0;i<exp_deleted.size();i++)
		    	{
		    		endpointExpense.removeExpense(exp_deleted.get(i)).execute();
		    	}
		    	g.ClearDelExpKey();
		    	
		    	//get all expenses from engine datastore
		    		resultExpense = endpointExpense.listExpense().execute();
		    		mExpenseListEngine=resultExpense.getItems();
	    	 
		    	//get all expenses from sqlite database	   
		    		mExpenseListSqlite=MainActivity.dbHandel.getAllExpense();
		    		
		    		/*sync between sqllite and engine datastore by insert or update:
			    	 1-fetch the sqlite expenses list :
			    	 	if expense key is null then insert in engine datastore
			    	 	else
			    	 		1- if expense still exist in engine datastore then update
			    	 		2-if not exist then insert in engine store(in case another user delete this key from engine datastore)
			    	 */
			    	 for(int i=0;i<mExpenseListSqlite.size();i++)
			    	 {
			    		 if(String.valueOf(mExpenseListSqlite.get(i).getExpensekey()).equals("null"))
		    			 {
			    			 endpointExpense.insertExpense(mExpenseListSqlite.get(i)).execute();
		    			 }
			    		 else
			    		 {
			    			 exist_exp=false;
			    			//test if list is empty in case no data in datastore  for avoiding erreur
					    	 if(!(mExpenseListEngine==null))
				    		 {
				    			 for(int j=0;j<mExpenseListEngine.size();j++)
					    		 {
					    			 if(mExpenseListSqlite.get(i).getExpensekey()==mExpenseListEngine.get(j).getKey().getId().longValue())
					    			 {
					    				 exist_exp=true;
					    				 mExpenseListEngine.get(j).setName(mExpenseListSqlite.get(i).getName());
					    				 mExpenseListEngine.get(j).setPrice(mExpenseListSqlite.get(i).getPrice());
					    				 mExpenseListEngine.get(j).setCategoryId(mExpenseListSqlite.get(i).getCategoryId());
					    				 try {
					    					 mExpenseListEngine.get(j).setExpenseDate(new DateTime(dateFormat.parse(mExpenseListSqlite.get(i).getExpenseDate().toString().trim())));
					 						
					 					} catch (ParseException e) {
					 						// TODO Auto-generated catch block
					 						e.printStackTrace();
					 					}
					    				 endpointExpense.updateExpense(mExpenseListEngine.get(j)).execute();
					    				 j=mExpenseListEngine.size();
					    			 }
					
					    		 }
				    		 }
			    			 if(!exist_exp)
			    			 {
			    				 endpointExpense.insertExpense(mExpenseListSqlite.get(i)).execute();
			    			 }
			    		 }
			    		
			    	 }
			    	 publishProgress(60); //60% done
			    	//get the list of expenses synced from engine store
			    	 resultExpense = endpointExpense.listExpense().execute();
			    		mExpenseListEngine=resultExpense.getItems();
			    	   
			    	   //delete all expenses in sqllite and then fill it from the engine store 
			    	   MainActivity.dbHandel.deleteAllExpense();
			    	 //test if list is empty in case no data in datastore  for avoiding erreur
				    	 if(!(mExpenseListEngine==null))
			    		 {
						     for(int i=0;i<mExpenseListEngine.size();i++) 
							 {
								 
						    	 mExpenseListEngine.get(i).setExpensekey(mExpenseListEngine.get(i).getKey().getId().longValue());
								 MainActivity.dbHandel.addExpense(mExpenseListEngine.get(i));
							 }
			    		 }
				    	 publishProgress(70); //70% done
				    	 /////////////////////////////////////////////////////////////////////////////
				    	 ///////Category sync code
				    	 ////////////////////////////////////////////////////////////////////////////
				    	 //delete all categories saved in the global list from the engine datastore
				    	 //Globals g = Globals.getInstance();
					    	List<Long> cat_deleted=g.getDelCatKey();
					    	for(int i=0;i<cat_deleted.size();i++)
					    	{
					    		endpoint.removeCategory(cat_deleted.get(i)).execute();
					    	}
					    	g.ClearDelCatKey();
					    	
					    //get all categories from engine datastore
					    	   result = endpoint.listCategory().execute();
					    	   mCategoryListEngine=result.getItems();
				    	 
					    //get all categories from sqlite database	   
					    	   mCategoryListSqlite=MainActivity.dbHandel.getAllCategory();
				    	 
				    	/*sync between sqllite and engine datastore by insert or update:
				    	 1-fetch the sqlite categories list :
				    	 	if category key is null then insert in engine datastore
				    	 	else
				    	 		1- if category still exist in engine datastore then update
				    	 		2-if not exist then insert in engine store(in case another user delete this key from engine datastore)
				    	 */
				    	 for(int i=0;i<mCategoryListSqlite.size();i++)
				    	 {
				    		 if(String.valueOf(mCategoryListSqlite.get(i).getCategorykey()).equals("null"))
			    			 {
				    			 endpoint.insertCategory(mCategoryListSqlite.get(i)).execute();
			    			 }
				    		 else
				    		 {
				    			 exist_cat=false;
				    			//test if list is empty in case no data in datastore  for avoiding erreur
						    	 if(!(mCategoryListEngine==null))
					    		 {
					    			 for(int j=0;j<mCategoryListEngine.size();j++)
						    		 {
						    			 if(mCategoryListSqlite.get(i).getCategorykey()==mCategoryListEngine.get(j).getKey().getId().longValue())
						    			 {
						    				 exist_cat=true;
						    				 mCategoryListEngine.get(j).setName(mCategoryListSqlite.get(i).getName());
						    				 mCategoryListEngine.get(j).setDescription(mCategoryListSqlite.get(i).getDescription());
						    				 endpoint.updateCategory(mCategoryListEngine.get(j)).execute();
						    				 j=mCategoryListEngine.size();
						    			 }
						
						    		 }
					    		 }
				    			 if(!exist_cat)
				    			 {
				    				 endpoint.insertCategory(mCategoryListSqlite.get(i)).execute();
				    			 }
					    		 
				    		 }
				    		
				    	 }
				    	 publishProgress(90); //90% done
				    	 //get the list of categories synced from engine store
				    	 result = endpoint.listCategory().execute();
				    	   mCategoryListEngine=result.getItems();
				    	   
				    	   //delete all categories in sqllite and then fill it from the engine store 
				    	   MainActivity.dbHandel.deleteAllCategory();
				    	 //test if list is empty in case no data in datastore  for avoiding erreur
					    	 if(!(mCategoryListEngine==null))
				    		 {
							     for(int i=0;i<mCategoryListEngine.size();i++) 
								 {
									 
									 mCategoryListEngine.get(i).setCategorykey(mCategoryListEngine.get(i).getKey().getId().longValue());
									 MainActivity.dbHandel.addCategory(mCategoryListEngine.get(i));
								 }
				    		 }
					    	 publishProgress(100); //100% done
				       
				     } catch (IOException e) {
				       // TODO Auto-generated catch block
				       e.printStackTrace();
				       
				     }
				     
				    
				     return null;
		   
		 		  
	   }
	   
	   /**
		 * Updating progress bar
		 * */
		protected void onProgressUpdate(Integer... progress) {
			// setting progress percentage
           pDialog.setProgress(progress[0]);
      }
	   @Override
		protected void onPostExecute(Void result) {
			// dismiss the dialog after the process completed
			dismissDialog(progress_bar_type);
			Toast.makeText(SyncActivity.this,"Sync Completed!", Toast.LENGTH_SHORT).show();
			Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
	   		startActivity(MainIntent);
	   }
	 }
	 
	 



	
}
