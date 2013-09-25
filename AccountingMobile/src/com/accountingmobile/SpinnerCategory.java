/*created by ahmad chaaban*/
package com.accountingmobile;


/*Fake class to fill the spinner */
public class SpinnerCategory {
	public int id = 0;
	public String name = "";
	public long cat_key;
	

	// A simple constructor for populating  variables 
	public SpinnerCategory( int _id, String _name,long _cat_key)
	{
	    id = _id;
	    name = _name;
	    cat_key =_cat_key;
	   
	}

	// The toString method is extremely important to making this class work with a Spinner
	// (or ListView) object because this is the method called when it is trying to represent
	// this object within the control.  If you do not have a toString() method, you WILL
	// get an exception.
	public String toString()
	{
	    return( name  );
	}
	}
