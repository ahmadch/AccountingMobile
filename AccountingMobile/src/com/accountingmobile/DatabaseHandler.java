/*created by ahmad chaaban*/
package com.accountingmobile;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.accountingmobile.categoryendpoint.model.Category;
import com.accountingmobile.expenseendpoint.model.Expense;
import com.accountingmobile.incomeendpoint.model.Income;
import com.google.api.client.util.DateTime;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/*
 * This class define the sqlite database with all methods needed.
 */
@SuppressLint("SimpleDateFormat")
public class DatabaseHandler extends SQLiteOpenHelper {

	// All Static variables
	// Database Version
	private static final int DATABASE_VERSION = 1;

	// Database Name
	private static final String DATABASE_NAME = "AccountingMobile";

	// Category table name
	private static final String TABLE_Categories = "Category";

	// Category Table Columns names
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "Name";
	private static final String KEY_Desc = "Description";
	private static final String KEY_App = "Key_app_engine";
	
	
	   
	  
	   
	// Expense table name
		private static final String TABLE_Expenses = "Expense";

		// Expense Table Columns names
		private static final String EXPENSE_ID = "id";
		private static final String EXPENSE_NAME = "Name";
		private static final String EXPENSE_PRICE = "price";
		private static final String EXPENSE_DATE = "expenseDate";
		private static final String EXPENSE_CREATEDDATE = "createdDate";
		private static final String EXPENSE_CATEGORYID = "category_id";
		
	
		// Income table name
		private static final String TABLE_Incomes = "Income";

		// Expense Table Columns names
		private static final String Income_ID = "id";
		private static final String Income_NAME = "Name";
		private static final String Income_Payment = "Payment_Type";
		private static final String Income_Payer = "Payer";
		private static final String Income_PRICE = "price";
		private static final String Income_DATE = "IncomeDate";
		private static final String Income_CREATEDDATE = "createdDate";
		private static final String Income_CATEGORYID = "category_id";
		
		SimpleDateFormat dateFormat;

	public DatabaseHandler(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Creating Tables
	@Override
	public void onCreate(SQLiteDatabase db) {
		String CREATE_Category_TABLE = "CREATE TABLE " + TABLE_Categories + "("
				+ KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
				+ KEY_Desc + " TEXT," + KEY_App + " TEXT" + ")";
		db.execSQL(CREATE_Category_TABLE);
		
		String CREATE_Expense_TABLE = "CREATE TABLE " + TABLE_Expenses + "("
				+ EXPENSE_ID + " INTEGER PRIMARY KEY," + EXPENSE_NAME + " TEXT,"
				+ EXPENSE_PRICE + " REAL,"+ EXPENSE_DATE + " TEXT,"+ EXPENSE_CREATEDDATE + " TEXT," + EXPENSE_CATEGORYID + " INTEGER,"+ KEY_App + " TEXT" + ")";
		db.execSQL(CREATE_Expense_TABLE);
		
		String CREATE_Income_TABLE = "CREATE TABLE " + TABLE_Incomes + "("
				+ Income_ID + " INTEGER PRIMARY KEY," + Income_NAME + " TEXT," + Income_Payment + " TEXT," + Income_Payer + " TEXT,"
				+ Income_PRICE + " REAL,"+ Income_DATE + " TEXT,"+ Income_CREATEDDATE + " TEXT," + Income_CATEGORYID + " INTEGER,"+ KEY_App + " TEXT" + ")";
		db.execSQL(CREATE_Income_TABLE);
	}

	// Upgrading database
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// Drop older table if existed
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_Categories);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_Expenses);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_Incomes);
		// Create tables again
		onCreate(db);
	}

/////////////////////////////////////////////////////////////////////////////////////////////////////
////////CRUD Category
/////////////////////////////////////////////////////////////////////////////////////////////////////	
	// Adding new Category
	void addCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		//values.put(KEY_ID, category.getId());
		values.put(KEY_NAME, category.getName()); // Category Name
		values.put(KEY_Desc, category.getDescription()); // Category Desc
		if(category.getCategorykey()!=null)
		{
		values.put(KEY_App, String.valueOf(category.getCategorykey())); // Category Key
		}
		// Inserting Row
		db.insert(TABLE_Categories, null, values);
		db.close(); // Closing database connection
	}

	// Getting single Category
	Category getCategory(int id) {
		SQLiteDatabase db = this.getReadableDatabase();

		Cursor cursor = db.query(TABLE_Categories, new String[] { KEY_ID,
				KEY_NAME, KEY_Desc,KEY_App }, KEY_ID + "=?",
				new String[] { String.valueOf(id) }, null, null, null, null);
		if (cursor != null)
			cursor.moveToFirst();

		Category category = new Category();
		category.setCategoryId(Integer.parseInt(cursor.getString(0)));
		category.setName(cursor.getString(1));
		category.setDescription( cursor.getString(2));
		category.setCategorykey(Long.parseLong(cursor.getString(3)));
		
		// return category
		return category;
	}
	
	// Getting single Category
		Category getCategoryByKey(long id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_Categories, new String[] { KEY_ID,
					KEY_NAME, KEY_Desc,KEY_App }, KEY_App + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();

			Category category = new Category();
			category.setCategoryId(Integer.parseInt(cursor.getString(0)));
			category.setName(cursor.getString(1));
			category.setDescription( cursor.getString(2));
			category.setCategorykey(Long.parseLong(cursor.getString(3)));
			
			// return category
			return category;
		}
	
	// Getting All Categories
	public List<Category> getAllCategory() {
		
		List<Category> categoryList = new ArrayList<Category>();
		// Select All Query
		String selectQuery = "SELECT  * FROM " + TABLE_Categories;

		SQLiteDatabase db = this.getWritableDatabase();
		Cursor cursor = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (cursor.moveToFirst()) {
			do {
				Log.d("Name: ", cursor.getString(0)+"/"+cursor.getString(1)+"/"+cursor.getString(2)+"/"+cursor.getString(3));
				Category category = new Category();
				category.setCategoryId(Integer.parseInt(cursor.getString(0)));
				category.setName(cursor.getString(1));
				category.setDescription(cursor.getString(2));
				if (cursor.getString(3)!=null)
				{
				category.setCategorykey(Long.parseLong(cursor.getString(3)));
				}
				// Adding category to list
				categoryList.add(category);
			} while (cursor.moveToNext());
		}

		// return category list
		return categoryList;
	}

	// Updating single category
	public long updateCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();

		ContentValues values = new ContentValues();
		values.put(KEY_NAME, category.getName());
		values.put(KEY_Desc, category.getDescription());

		// updating row
		return db.update(TABLE_Categories, values, KEY_ID + " = ?",
				new String[] { String.valueOf(category.getCategoryId()) });
	}

	// Deleting single category
	public void deleteCategory(Category category) {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_Categories, KEY_ID + " = ?",
				new String[] { String.valueOf(category.getCategoryId()) });
		db.close();
	}

	// Deleting ALL category
	public void deleteAllCategory() {
		SQLiteDatabase db = this.getWritableDatabase();
		db.delete(TABLE_Categories,null,null);
		db.close();
	}
	// Getting categories Count
	public int getCategoriesCount() {
		String countQuery = "SELECT  * FROM " + TABLE_Categories;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		cursor.close();

		// return count
		return cursor.getCount();
	}
	
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////CRUD Expense
/////////////////////////////////////////////////////////////////////////////////////////////////////	
	// Adding new Expense
		void addExpense(Expense expense) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			
			values.put(EXPENSE_NAME, expense.getName()); // EXPENSE Name
			values.put(EXPENSE_PRICE, expense.getPrice()); // EXPENSE Price
			values.put(EXPENSE_DATE,expense.getExpenseDate().toString().substring(0, 10)); // EXPENSE Date
			values.put(EXPENSE_CREATEDDATE, expense.getCreatedDate().toString().substring(0, 10)); // EXPENSE CreatedDate
			values.put(EXPENSE_CATEGORYID, expense.getCategoryId()); // EXPENSE CategoryId
			if(expense.getExpensekey()!=null)
			{
			values.put(KEY_App, String.valueOf(expense.getExpensekey())); // EXPENSE Key
			}
		
			 
			// Inserting Row
			db.insert(TABLE_Expenses, null, values);
			db.close(); // Closing database connection
		}
		
		// Getting single Expense
		Expense getExpense(int id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_Expenses, new String[] { EXPENSE_ID,
					EXPENSE_NAME, EXPENSE_PRICE,EXPENSE_DATE,EXPENSE_CREATEDDATE,EXPENSE_CATEGORYID,KEY_App }, EXPENSE_ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Expense expense = new Expense();
			expense.setExpenseId(Integer.parseInt(cursor.getString(0)));
			expense.setName(cursor.getString(1));
			expense.setPrice(Double.parseDouble(cursor.getString(2)));
			try {
				expense.setExpenseDate(new DateTime(dateFormat.parse(cursor.getString(3))));
				expense.setCreatedDate(new DateTime(dateFormat.parse(cursor.getString(4))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			expense.setCategoryId(Long.parseLong(cursor.getString(5)));
			expense.setExpensekey(Long.parseLong(cursor.getString(6)));
			// return expense
			return expense;
		}
		
		// Getting All Expenses
		public List<Expense> getAllExpense() {
			//onUpgrade(this.getWritableDatabase(), 1, 1);
			List<Expense> expenseList = new ArrayList<Expense>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_Expenses;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Log.d("Name: ", cursor.getString(0)+"/"+cursor.getString(1)+"/"+cursor.getString(2)+"/"+cursor.getString(3)+"/"+cursor.getString(4)+"/"+cursor.getString(5)+"/"+cursor.getString(6));
					Expense expense = new Expense();
					expense.setExpenseId(Integer.parseInt(cursor.getString(0)));
					expense.setName(cursor.getString(1));
					expense.setPrice(Double.parseDouble(cursor.getString(2)));
					try {
						dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						expense.setExpenseDate(new DateTime(dateFormat.parse(cursor.getString(3))));
						expense.setCreatedDate(new DateTime(dateFormat.parse(cursor.getString(4))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					expense.setCategoryId(Long.parseLong(cursor.getString(5)));
					if (cursor.getString(6)!=null)
					{
					expense.setExpensekey(Long.parseLong(cursor.getString(6)));			
					}
					// Adding expense to list
					expenseList.add(expense);
				} while (cursor.moveToNext());
			}

			// return expense list
			return expenseList;
		}
		
		// Getting All Expenses by category
		public List<Expense> getAllExpenseByCategory(long cat_id) {
			//onUpgrade(this.getWritableDatabase(), 1, 1);
			List<Expense> expenseList = new ArrayList<Expense>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_Expenses + " WHERE " + EXPENSE_CATEGORYID + "=" + String.valueOf(cat_id) ;
			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Log.d("Name: ", cursor.getString(0)+"/"+cursor.getString(1)+"/"+cursor.getString(2)+"/"+cursor.getString(3)+"/"+cursor.getString(4)+"/"+cursor.getString(5)+"/"+cursor.getString(6));
					Expense expense = new Expense();
					expense.setExpenseId(Integer.parseInt(cursor.getString(0)));
					expense.setName(cursor.getString(1));
					expense.setPrice(Double.parseDouble(cursor.getString(2)));
					try {
						dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						expense.setExpenseDate(new DateTime(dateFormat.parse(cursor.getString(3))));
						expense.setCreatedDate(new DateTime(dateFormat.parse(cursor.getString(4))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					expense.setCategoryId(Long.parseLong(cursor.getString(5)));
					if (cursor.getString(6)!=null)
					{
					expense.setExpensekey(Long.parseLong(cursor.getString(6)));	
					}
					// Adding expense to list
					expenseList.add(expense);
				} while (cursor.moveToNext());
			}

			// return expense list
			return expenseList;
		}

		
		
		// Updating single expense
		public long updateExpense(Expense expense) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			
			values.put(EXPENSE_NAME, expense.getName()); // EXPENSE Name
			values.put(EXPENSE_PRICE, expense.getPrice()); // EXPENSE Price
			values.put(EXPENSE_DATE,expense.getExpenseDate().toString().substring(0, 10)); // EXPENSE Date
			values.put(EXPENSE_CREATEDDATE, expense.getCreatedDate().toString().substring(0, 10)); // EXPENSE CreatedDate
			values.put(EXPENSE_CATEGORYID, expense.getCategoryId()); // EXPENSE CategoryId

			// updating row
			return db.update(TABLE_Expenses, values, EXPENSE_ID + " = ?",
					new String[] { String.valueOf(expense.getExpenseId()) });
		}

		// Deleting single expense
		public void deleteExpense(Expense expense) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_Expenses, EXPENSE_ID + " = ?",
					new String[] { String.valueOf(expense.getExpenseId()) });
			db.close();
		}

		// Deleting ALL expense
		public void deleteAllExpense() {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_Expenses,null,null);
			db.close();
		}
		// Getting expenses Count
		public int getExpenseCount() {
			String countQuery = "SELECT  * FROM " + TABLE_Expenses;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cursor.close();

			// return count
			return cursor.getCount();
		}
/////////////////////////////////////////////////////////////////////////////////////////////////////
////////CRUD Income
/////////////////////////////////////////////////////////////////////////////////////////////////////
// Adding new Income
		void addIncome(Income income) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			
			values.put(Income_NAME, income.getName()); // Income Name
			values.put(Income_Payment, income.getPaymentType()); // Income Payment
			values.put(Income_Payer, income.getPayer()); // Income Payer
			values.put(Income_PRICE, income.getPrice()); // Income Price
			values.put(Income_DATE,income.getIncomeDate().toString().substring(0, 10)); // Income Date
			values.put(Income_CREATEDDATE, income.getCreatedDate().toString().substring(0, 10)); // Income CreatedDate
			values.put(Income_CATEGORYID, income.getCategoryId()); // Income CategoryId
			if(income.getIncomekey()!=null)
			{
			values.put(KEY_App, String.valueOf(income.getIncomekey())); // Income Key
			}
			// Inserting Row
			db.insert(TABLE_Incomes, null, values);
			db.close(); // Closing database connection
		}
		
		// Getting single Income
		Income getIncome(int id) {
			SQLiteDatabase db = this.getReadableDatabase();

			Cursor cursor = db.query(TABLE_Incomes, new String[] { Income_ID,
					Income_NAME, Income_Payment,Income_Payer,Income_PRICE,Income_DATE,Income_CREATEDDATE,Income_CATEGORYID,KEY_App}, Income_ID + "=?",
					new String[] { String.valueOf(id) }, null, null, null, null);
			if (cursor != null)
				cursor.moveToFirst();
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Income income = new Income();
			income.setIncomeId(Integer.parseInt(cursor.getString(0)));
			income.setName(cursor.getString(1));
			income.setPaymentType(cursor.getString(2));
			income.setPayer(cursor.getString(3));
			income.setPrice(Double.parseDouble(cursor.getString(4)));
			try {
				income.setIncomeDate(new DateTime(dateFormat.parse(cursor.getString(5))));
				income.setCreatedDate(new DateTime(dateFormat.parse(cursor.getString(6))));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			income.setCategoryId(Long.parseLong(cursor.getString(7)));
			if (cursor.getString(8)!=null)
			{
			income.setIncomekey(Long.parseLong(cursor.getString(8)));
			}
			// return expense
			return income;
		}
		
		// Getting All Incomes
		public List<Income> getAllIncome() {
			//onUpgrade(this.getWritableDatabase(), 1, 1);
			List<Income> incomeList = new ArrayList<Income>();
			// Select All Query
			String selectQuery = "SELECT  * FROM " + TABLE_Incomes;

			SQLiteDatabase db = this.getWritableDatabase();
			Cursor cursor = db.rawQuery(selectQuery, null);
			
			// looping through all rows and adding to list
			if (cursor.moveToFirst()) {
				do {
					Log.d("Name: ", cursor.getString(0)+"/"+cursor.getString(1)+"/"+cursor.getString(2)+"/"+cursor.getString(3)+"/"+cursor.getString(4)+"/"+cursor.getString(5)+"/"+cursor.getString(6)+"/"+cursor.getString(7));
					Income income = new Income();
					income.setIncomeId(Integer.parseInt(cursor.getString(0)));
					income.setName(cursor.getString(1));
					income.setPaymentType(cursor.getString(2));
					income.setPayer(cursor.getString(3));
					income.setPrice(Double.parseDouble(cursor.getString(4)));
					try {
						dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						income.setIncomeDate(new DateTime(dateFormat.parse(cursor.getString(5))));
						income.setCreatedDate(new DateTime(dateFormat.parse(cursor.getString(6))));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					income.setCategoryId(Long.parseLong(cursor.getString(7)));
					if (cursor.getString(8)!=null)
					{
					income.setIncomekey(Long.parseLong(cursor.getString(8)));
					}

					
					// Adding income to list
					incomeList.add(income);
				} while (cursor.moveToNext());
			}

			// return income list
			return incomeList;
		}
		
		
		
		// Getting All Incomes by category
				public List<Income> getAllIncomeByCategory(long cat_id) {
					//onUpgrade(this.getWritableDatabase(), 1, 1);
					List<Income> incomeList = new ArrayList<Income>();
					// Select All Query
					String selectQuery = "SELECT  * FROM " + TABLE_Incomes + " WHERE " + Income_CATEGORYID + "=" + String.valueOf(cat_id) ;

					SQLiteDatabase db = this.getWritableDatabase();
					Cursor cursor = db.rawQuery(selectQuery, null);
					
					// looping through all rows and adding to list
					if (cursor.moveToFirst()) {
						do {
							Log.d("Name: ", cursor.getString(0)+"/"+cursor.getString(1)+"/"+cursor.getString(2)+"/"+cursor.getString(3)+"/"+cursor.getString(4)+"/"+cursor.getString(5)+"/"+cursor.getString(6)+"/"+cursor.getString(7));
							Income income = new Income();
							income.setIncomeId(Integer.parseInt(cursor.getString(0)));
							income.setName(cursor.getString(1));
							income.setPaymentType(cursor.getString(2));
							income.setPayer(cursor.getString(3));
							income.setPrice(Double.parseDouble(cursor.getString(4)));
							try {
								dateFormat = new SimpleDateFormat("yyyy-MM-dd");
								income.setIncomeDate(new DateTime(dateFormat.parse(cursor.getString(5))));
								income.setCreatedDate(new DateTime(dateFormat.parse(cursor.getString(6))));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							income.setCategoryId(Long.parseLong(cursor.getString(7)));
							income.setIncomekey(Long.parseLong(cursor.getString(8)));
							
							// Adding income to list
							incomeList.add(income);
						} while (cursor.moveToNext());
					}

					// return income list
					return incomeList;
				}

		
		// Updating single income
		public long updateIncome(Income income) {
			SQLiteDatabase db = this.getWritableDatabase();

			ContentValues values = new ContentValues();
			
			values.put(Income_NAME, income.getName()); // Income Name
			values.put(Income_Payment, income.getPaymentType()); // Income Payment
			values.put(Income_Payer, income.getPayer()); // Income Payer
			values.put(Income_PRICE, income.getPrice()); // Income Price
			values.put(Income_DATE,income.getIncomeDate().toString().substring(0, 10)); // Income Date
			values.put(Income_CREATEDDATE, income.getCreatedDate().toString().substring(0, 10)); // Income CreatedDate
			values.put(Income_CATEGORYID, income.getCategoryId()); // Income CategoryId

			// updating row
			return db.update(TABLE_Incomes, values, Income_ID + " = ?",
					new String[] { String.valueOf(income.getIncomeId()) });
		}

		// Deleting single income
		public void deleteIncome(Income income) {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_Incomes, Income_ID + " = ?",
					new String[] { String.valueOf(income.getIncomeId()) });
			db.close();
		}

		// Deleting ALL income
		public void deleteAllIncome() {
			SQLiteDatabase db = this.getWritableDatabase();
			db.delete(TABLE_Incomes,null,null);
			db.close();
		}
				
		// Getting incomes Count
		public int getIncomeCount() {
			String countQuery = "SELECT  * FROM " + TABLE_Incomes;
			SQLiteDatabase db = this.getReadableDatabase();
			Cursor cursor = db.rawQuery(countQuery, null);
			cursor.close();

			// return count
			return cursor.getCount();
		}


}
