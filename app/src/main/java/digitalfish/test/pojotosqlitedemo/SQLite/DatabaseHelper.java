package digitalfish.test.pojotosqlitedemo.SQLite;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import digitalfish.test.pojotosqlitedemo.DataClasses.User;
import digitalfish.test.pojotosqlitedemo.DataClasses.Visit;


/**
 * Created by Piotr Malak on 2015-06-15.
 * Classic DatabaseHelper extended by mostly used methods to manipulate data.
 * All operations are performed on Java Classes.
 */
public class DatabaseHelper extends SQLiteOpenHelper {

    static DatabaseHelper sInstance;

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    public static final String DATABASE_NAME = "POJO2SQL";


    DFlex dFlex;


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public static DatabaseHelper getInstance(Context context) {

        if (sInstance == null)
            sInstance = new DatabaseHelper(context);

        return sInstance;
    }
    //----------------------------------------------------------------------------------------------
    @Override
    public void onCreate(SQLiteDatabase db) {

        //Automatically created tables based on Classes
        dFlex = new DFlex(db);
        dFlex.createTableFromClass(Visit.class.getName());
        dFlex.createTableFromClass(User.class.getName());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        Log.w("MB", DatabaseHelper.class.getName() +
                ": Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        // Drop older table if existed

        //Drop current tables if new version of database.
        String name= dFlex.getClassName(Visit.class.getName());
        if(name.length()>0)
            db.execSQL("DROP TABLE IF EXISTS " + name );

        name= dFlex.getClassName(User.class.getName());
        if(name.length()>0)
            db.execSQL("DROP TABLE IF EXISTS " + name );

        // Create tables again
        onCreate(db);
    }

    public synchronized void addObjectToTable(Object pObject, String pClassName, boolean pDeleteCurrentContent) {

        SQLiteDatabase db = this.getWritableDatabase();
        dFlex = new DFlex(db);
        String name= dFlex.getClassName(pClassName);
        if(name.length()>0 && pDeleteCurrentContent)
            db.delete(name, null, null);
        dFlex.addObjectDataToTable(pObject, pClassName);

        db.close(); // Closing database connection
    }
    public synchronized Object getObjectFromTable(String pClassName){
        return getObjectFromTable(pClassName, null, null);
    }

    public synchronized Object getObjectFromTable(String pClassName, String pSelection, String[]pArguments) {

        SQLiteDatabase db = this.getReadableDatabase();
        dFlex = new DFlex(db);
        String name= dFlex.getClassName(pClassName);

        Cursor cursor = db.query(name, null, pSelection,
                pArguments, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        Object lObject = dFlex.getDataToObject(cursor, pClassName);

        cursor.close();
        db.close();
        return lObject;
    }

    public synchronized void addListOfObjects(List<?> pObjects, String pClassName, boolean pDeleteCurrentContent){

        SQLiteDatabase db = this.getWritableDatabase();
        dFlex = new DFlex(db);
        String name= dFlex.getClassName(pClassName);
        if(name.length()>0 && pDeleteCurrentContent)
            db.delete(name, null, null);
        db.beginTransaction();
        try {
            for (Object lObject : pObjects) {
                dFlex.addObjectDataToTable(lObject, pClassName);
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        db.close();
    }
    public synchronized List<Object> getListOfObjects(String pClasName){
        return getListOfObjects(pClasName, null, null);
    }

    public synchronized List<Object> getListOfObjects(String pClasName, String pSelection, String[]pArguments ){

        List<Object>lObjects = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        dFlex = new DFlex(db);
        String name= dFlex.getClassName(pClasName);

        Cursor cursor = db.query(name, null, pSelection,
                pArguments, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        while (!cursor.isAfterLast()) {
            Object lObject = dFlex.getDataToObject(cursor, pClasName);
            if(lObject!=null)
                lObjects.add(lObject);

            cursor.moveToNext();
        }

        cursor.close();
        db.close();
        return lObjects;

    }

    public synchronized boolean delete(String pClassName, String pSelection, String[]pArguments){
        SQLiteDatabase db = this.getWritableDatabase();
        dFlex = new DFlex(db);
        String name= dFlex.getClassName(pClassName);
        boolean res = db.delete(name, pSelection, pArguments)>0;
        db.close();
        return res;
    }
}
