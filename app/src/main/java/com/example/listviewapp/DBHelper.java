package com.example.listviewapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    Context context;
    private  final  static  String DB_NAME = "myDB";
    private  final  static int DB_VERSION = 1;
    private  final  static String TABLE_NAME = "myTable";
    private  final  static String COLUMN_ID = "id";
    private  final  static String COLUMN_NAME = "name";
    private  final  static String COLUMN_MOBILE = "mobile";


    public DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE myTable (id integer PRIMARY KEY AUTOINCREMENT, name TEXT , mobile TEXT UNIQUE)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
     String query = "DROP TABLE IF EXISTS myTable";
     db.execSQL(query);
     onCreate(db);

    }

    public boolean addItem(String name, String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_MOBILE, mobile);

        long id = db.insert(TABLE_NAME, null, values);
        if(id == -1){
            Log.e("Tag","Insert fail");
            return false;
        }else {
            Log.e("Tag","Insert success");
        }
//        db.close();
        return true;
    }

//    public ArrayList<Person> getSomeData (){
//        ArrayList<Person> arrayList = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//        String query = "Select * from myTable LIMIT 2";
//        Cursor cursor = db.rawQuery(query, null);
//
//        if(cursor != null) {
//            int roeCount = cursor.getCount();
//            if(roeCount>2) {
//                cursor.moveToPosition(2);
//                while (!cursor.isAfterLast()){
//                    int id = cursor.getInt(cursor.getColumnIndex("2"));
//                    db.delete("myTable", "id=?", new String[] {String.valueOf(id)});
//                    cursor.moveToNext();
//                }
//            }
//        }

//        while (cursor.moveToNext()) {
//            String name = cursor.getString(1);
//            String mobile = cursor.getString(2);
//            int id = cursor.getInt(0);
//            Person person = new Person(id,name,mobile);
//            arrayList.add(person);
//        }
//        return arrayList;
//
//    }



    public ArrayList<Person> getAllData (){
        ArrayList<Person> arrayList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * from "+TABLE_NAME;
        Cursor cursor = db.rawQuery(query, null);

        while (cursor.moveToNext()) {
            String name = cursor.getString(1);
            String mobile = cursor.getString(2);
            int id = cursor.getInt(0);
            Person person = new Person(id,name,mobile);
            arrayList.add(person);
        }
        return arrayList;

    }

    public int getIdValue (String name, String mobile){
        SQLiteDatabase db = this.getReadableDatabase();
       // String query = "Select * from "+TABLE_NAME ;
        Cursor cursor = db.rawQuery("Select * from myTable where name = ? AND mobile =?", new String[] {name,mobile});

        if (cursor.getCount() > 0) {
            int id = cursor.getInt(0);
            return id;
        }
        return -1;
    }

    public boolean updateUserData(int idVal,String name,String mobile) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
      Log.e("idVal",idVal+"");
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_MOBILE, mobile);
        Cursor cursor = db.rawQuery("Select * from myTable where id = ?", new String[] {String.valueOf(idVal)});
        Log.e("cursor",cursor.getCount()+"");
        if(cursor.getCount()>0) {
          long result = db.update("myTable",contentValues,"id = ?", new String[] {String.valueOf(idVal)});
            Log.e("result", result+"");
          if (result == -1){

                Log.e("updateTag","update fail");
                return false;
            } else {

                Log.e("updateTag","update success");
                return true;
            }
        }else {
            return false;
        }

    }

    public boolean deleteUserData(String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("Select * from myTable where name = ?", new String[] {name} );
        if(cursor.getCount()>0) {
            long result = db.delete("myTable","name=?", new String[] {name});
            if (result == -1){
                Log.e("deleteTag","delete fail");
                return false;
            } else {

                Log.e("deleteTag","delete success");
                return true;
            }
        }else {
            return false;
        }
    }

    public boolean deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String tableName = "myTable";
        String selectQuery = "Select * From myTable";
        Cursor cursor = db.rawQuery(selectQuery,null);


        int rowCount = cursor.getCount();
        if(rowCount>2) {
            int rowsToDelete = rowCount-2;
            String deleteQuery = "DELETE FROM "+ tableName +
                                 " WHERE id NOT IN (SELECT id FROM " + tableName +
                                  "ORDER  id LIMIT 2)";

            String deleteQ = "DELETE FROM myTable WHERE id NOT IN (SELECT id FROM myTable ORDER BY id LIMIT 2)";

            String delQuery = "DELETE FROM myTale WHERE ID NOT IN (SELECT TOP 2 ID FROM myTable)";
            db.execSQL(deleteQ);

        }else {
            return false;
        }
        return true;
    }
}
