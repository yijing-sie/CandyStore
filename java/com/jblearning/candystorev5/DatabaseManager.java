package com.jblearning.candystorev5;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;


public class DatabaseManager extends SQLiteOpenHelper {
  private static final String DATABASE_NAME = "DB";
  private static final int DATABASE_VERSION = 1;
  private static final String TABLE_CANDY = "candy";
  private static final String TABLE_TRANSACTION = "transactions";
  private static final String CUSTOMER_ID = "customer";
  private static final String ID = "id";
  private static final String NAME = "name";
  private static final String PRICE = "price";
  private static final String TOTAL = "total";
	
  public DatabaseManager( Context context ) {
    //the third para is Factory can be used to create Cursor objects, use null for default
    super( context, DATABASE_NAME, null, DATABASE_VERSION );
  }

  //automatically called everytime when we create a new database
  public void onCreate( SQLiteDatabase db ) {
    // build sql create statement
    String sqlCreate = "create table " + TABLE_CANDY + "( " + ID;
    sqlCreate += " integer primary key autoincrement, " + NAME;
    sqlCreate += " text, " + PRICE + " real )" ;
    //sqlCreate =
    //create table candy( 123456789 integer primary key autoincrement, vanilla text, 1.3 real )
    String transCreate = "create table " + TABLE_TRANSACTION + "( " + CUSTOMER_ID;
    transCreate += " integer primary key autoincrement, ";
    transCreate += TOTAL + " real )" ;
    //transCreate =
    //create table transaction( 123456789 integer primary key autoincrement, 1.3 real )
    db.execSQL( sqlCreate );
    db.execSQL( transCreate );
  }
 //called automatically when the database needed to be upgraded
  public void onUpgrade( SQLiteDatabase db,
                         int oldVersion, int newVersion ) {
    // Drop old table if it exists
    db.execSQL( "drop table if exists " + TABLE_CANDY );
    db.execSQL( "drop table if exists " + TABLE_TRANSACTION );
    // Re-create tables
    onCreate( db );
  }
  public void totals ( double total ) {
    SQLiteDatabase db = this.getWritableDatabase( );
    String sqlInsert = "insert into " + TABLE_TRANSACTION;
    sqlInsert += " values( null, '" + total +"' )";

    db.execSQL( sqlInsert );
    db.close( );
  }
  public void insert( Candy candy ) {
    SQLiteDatabase db = this.getWritableDatabase( );
    String sqlInsert = "insert into " + TABLE_CANDY;
    //the id of the candy is not determined by us, so
    //we set id = null, since it'll be generated automatically later
    sqlInsert += " values( null, '" + candy.getName( );
    sqlInsert += "', '" + candy.getPrice( ) + "' )";
 
    db.execSQL( sqlInsert );
    db.close( );
  }
   
  public void deleteById( int id ) {
    SQLiteDatabase db = this.getWritableDatabase( );
    String sqlDelete = "delete from " + TABLE_CANDY;
    sqlDelete += " where " + ID + " = " + id;
    
    db.execSQL( sqlDelete );
    db.close( );
  }

  public void updateById( int id, String name, double price ) {
    SQLiteDatabase db = this.getWritableDatabase();
 
    String sqlUpdate = "update " + TABLE_CANDY;
    sqlUpdate += " set " + NAME + " = '" + name + "', ";
    sqlUpdate += PRICE + " = '" + price + "'";
    sqlUpdate += " where " + ID + " = " + id;

    db.execSQL( sqlUpdate );
    db.close( );
  }
  public double totalRevenue(){
    String sqlQuery = "select * from " + TABLE_TRANSACTION;
    SQLiteDatabase db = this.getWritableDatabase( );
    Cursor cursor = db.rawQuery( sqlQuery, null );
    double totals = 0.0;
    while( cursor.moveToNext( ) ) {
      double total = cursor.getDouble( 1 ) ;
      totals += total;
      }
      db.close( );
      return totals;
    }
  public ArrayList<Candy> selectAll( ) {
    //* : all
    String sqlQuery = "select * from " + TABLE_CANDY;
    //db was initialized here
    SQLiteDatabase db = this.getWritableDatabase( );
    //like a pointer, pointing from fst row
    Cursor cursor = db.rawQuery( sqlQuery, null );
    
    ArrayList<Candy> candies = new ArrayList<Candy>( );
    while( cursor.moveToNext( ) ) {
      Candy currentCandy
              //first we retrive id as string and then parse it into int
          = new Candy( Integer.parseInt( cursor.getString( 0 ) ),
        		        cursor.getString( 1 ), cursor.getDouble( 2 ) );
      candies.add( currentCandy );
    }
    db.close( );
    return candies;
  }
    
  public Candy selectById( int id ) {
    String sqlQuery = "select * from " + TABLE_CANDY;
    sqlQuery += " where " + ID + " = " + id;
    
    SQLiteDatabase db = this.getWritableDatabase( );
    Cursor cursor = db.rawQuery( sqlQuery, null );
 
    Candy candy = null;
    //we expect here will only be one or zero row
    //'cauz every id is unique
    if( cursor.moveToFirst( ) )
      candy = new Candy( Integer.parseInt( cursor.getString( 0 ) ),
		              cursor.getString( 1 ), cursor.getDouble( 2 ) );
    return candy;
  }
}
