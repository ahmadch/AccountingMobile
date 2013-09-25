/*created by ahmad chaaban*/
package com.accountingmobile;

import java.util.ArrayList;
import java.util.List;

/*
 * Thi class define all the global variables needed in the application
 */

public class Globals{
	   private static Globals instance;
	 
	   // Global variables
	   
	   //this variable used to know if exist connection when we do a sync
	   private boolean Check_Connection=true;
	   /* The 3 variables below used when syncing the data*/
	   private List<Long> Del_Category_Keys=new ArrayList<Long>();
	   private List<Long> Del_Expense_Keys=new ArrayList<Long>();
	   private List<Long> Del_Income_Keys=new ArrayList<Long>();
	   // Restrict the constructor from being instantiated
	   private Globals(){}
	 
	   
	   public void setCheck_Connection(boolean Check_Connection){
		   this.Check_Connection=Check_Connection;
	   }
	   public boolean getCheck_Connection(){
		   return this.Check_Connection;
	   }
	   
	   public void setDelCatKey(long Del_Category_Key){
		   this.Del_Category_Keys.add(Del_Category_Key);
	   }
	   public List<Long> getDelCatKey(){
		   return this.Del_Category_Keys;
	   }
	   public  void ClearDelCatKey(){
		   this.Del_Category_Keys.clear();
	   }
	   
	   public void setDelExpKey(long Del_Expense_Keys){
		   this.Del_Expense_Keys.add(Del_Expense_Keys);
	   }
	   public List<Long> getDelExpKey(){
		   return this.Del_Expense_Keys;
	   }
	   public  void ClearDelExpKey(){
		   this.Del_Expense_Keys.clear();
	   }
	   
	   public void setDelIncKey(long Del_Income_Keys){
		   this.Del_Income_Keys.add(Del_Income_Keys);
	   }
	   public List<Long> getDelIncKey(){
		   return this.Del_Income_Keys;
	   }
	   public  void ClearDelIncKey(){
		   this.Del_Income_Keys.clear();
	   }
	   public static synchronized Globals getInstance(){
	     if(instance==null){
	       instance=new Globals();
	     }
	     return instance;
	   }
	}