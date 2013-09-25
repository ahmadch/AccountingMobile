/*created by ahmad chaaban*/
package com.accountingmobile;

import java.util.List;
import com.accountingmobile.incomeendpoint.model.Income;
import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

/*
 * display the incomes filtered by the BilanFilterActivity
 */

public class IncomeTabActivity extends Activity {
	private List<Income> mIncomeList;
	TypeAdapter adapter;
	protected static double Income_Amount; 
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income_layout);
        
        if (BilanFilterActivty.cat_id==1)
  	  {
        	 mIncomeList= MainActivity.dbHandel.getAllIncome();
  	  }
  	  else
  	  {
  		  	mIncomeList= MainActivity.dbHandel.getAllIncomeByCategory(BilanFilterActivty.cat_id);
  	  }
       
    	// Create the list
    	Income_Amount=0;
    	for(int i = 0; i < mIncomeList.size(); ++i) {
    		Income_Amount=Income_Amount+ mIncomeList.get(i).getPrice();
    	}
		ListView listViewIncome = (ListView)findViewById(R.id.list_income_cat);
		 adapter=new TypeAdapter(mIncomeList, getLayoutInflater(),false);
		 
		 listViewIncome.setAdapter(adapter);

    }
        
}
