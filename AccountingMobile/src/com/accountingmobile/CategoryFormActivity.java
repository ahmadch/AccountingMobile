/*created by ahmad chaaban*/
package com.accountingmobile;


import com.accountingmobile.R;
import com.accountingmobile.categoryendpoint.model.Category;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View.OnClickListener;

/*
 * This activity creaqted for update or insert a category.
 */
public class CategoryFormActivity extends Activity{
	protected static Category updatedCategory;
	Category category;
	EditText etCatName;
	EditText etCatDesc;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.category_form);
		
		etCatName = (EditText)findViewById(R.id.etCategoryName);
		etCatDesc = (EditText)findViewById(R.id.etCategoryDesc);
		if (updatedCategory!=null){
			etCatName.setText(updatedCategory.getName());
			etCatDesc.setText(updatedCategory.getDescription());
		}
		else
		{
			etCatName.setText("");
			etCatDesc.setText("");
		}
		
		 Button btnSave = (Button) findViewById(R.id.Btn_save);
		 btnSave.setOnClickListener(new OnClickListener() {
		 
		   @Override
		   public void onClick(View v) {
			   if(etCatName.getText().toString().trim().equals("")||
					   etCatDesc.getText().toString().trim().equals(""))
			   {
				   showAlert("Fields are required!");
			   }
			   else
			   {
			  
				   if (updatedCategory!=null)
				   {
					   updatedCategory.setName(etCatName.getText().toString().trim());
						
					   updatedCategory.setDescription(etCatDesc.getText().toString().trim());
					   MainActivity.dbHandel.updateCategory(updatedCategory);
					   Toast.makeText(CategoryFormActivity.this,"Category Updated!", Toast.LENGTH_SHORT).show();
					   
				   }
				   else
					   {
					   category= new Category();  
					     
						  
					   category.setName(etCatName.getText().toString().trim());
						
					   category.setDescription(etCatDesc.getText().toString().trim());
					   MainActivity.dbHandel.addCategory(category);
					   Toast.makeText(CategoryFormActivity.this,"Category Added!", Toast.LENGTH_SHORT).show();
					   

					   }

				   Intent MainIntent = new Intent(getBaseContext(),MainActivity.class);
					startActivity(MainIntent);
			  
			   }
		   }
		  });
		  
	}
	
	
	 public void showAlert(final String msg){
	    	CategoryFormActivity.this.runOnUiThread(new Runnable() {
	            public void run() {
	                AlertDialog.Builder builder = new AlertDialog.Builder(CategoryFormActivity.this);
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
