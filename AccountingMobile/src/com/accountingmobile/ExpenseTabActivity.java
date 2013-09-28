/*created by ahmad chaaban*/
package com.accountingmobile;

import java.util.List;



import com.accountingmobile.R;
import com.accountingmobile.expenseendpoint.model.Expense;
import android.app.ActivityGroup;
import android.os.Bundle;
import android.widget.ListView;

/*
 * display the expenses filtered by the BilanFilterActivity
 */
public class ExpenseTabActivity extends ActivityGroup {
	private List<Expense> mExpenseList;
	TypeAdapter adapter;
	protected static double Expense_Amount;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expense_layout);
        
        
   	 
  	  if (BilanFilterActivty.cat_id==1)
  	  {
  		mExpenseList = MainActivity.dbHandel.getAllExpense();
  	  }
  	  else
  	  {
  		mExpenseList =MainActivity.dbHandel.getAllExpenseByCategory(BilanFilterActivty.cat_id);
  	  }
        Expense_Amount=0;
    	for(int i = 0; i < mExpenseList.size(); ++i) {
    		Expense_Amount=Expense_Amount+ mExpenseList.get(i).getPrice();
    	}
    	// Create the list
		ListView listViewExpense = (ListView)findViewById(R.id.list_expense_cat);
		 adapter=new TypeAdapter(mExpenseList, getLayoutInflater(),true);
		 
		 listViewExpense.setAdapter(adapter);

    }
     
    
}
