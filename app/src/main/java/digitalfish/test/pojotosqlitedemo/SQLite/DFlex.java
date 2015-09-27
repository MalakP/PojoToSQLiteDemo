package digitalfish.test.pojotosqlitedemo.SQLite;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;


/**
 * 2015-07-21.
 * Helper class for transforming Java objects into SQLite database tables using reflections. It provides basic methods
 * to write and read data from database.
 * execution draft copied from: http://pygmalion.nitri.de/create-sqlite-tables-from-data-classes-on-android-154.html
 */
public class DFlex implements IDFlex{

    SQLiteDatabase db;
    List<Class<?>> mAllowedTypes;

    public DFlex(SQLiteDatabase db) {
        this.db = db;

        mAllowedTypes = new ArrayList<>();
        mAllowedTypes.add(Long.class);
        mAllowedTypes.add(Integer.class);
        mAllowedTypes.add(Boolean.class);
        mAllowedTypes.add(String.class);
    }



    public void createTableFromClass(String className) {

        Field[] fields;

        StringBuilder queryBuilder = new StringBuilder();

        try {
            Class<?> clazz = Class.forName(className);
            fields = clazz.getDeclaredFields();
            String name = clazz.getSimpleName();
            queryBuilder.append("CREATE TABLE " + name + " (");
        } catch (Exception e) {
            Log.e("ERROR", e.getMessage());
            return;
        }

        boolean firstField = true;

        for (Field field : fields) {
            if(!mAllowedTypes.contains(field.getType())) {
                Log.w("DFlex","Field type: "+field.getType()+" is not allowed");
                continue;
            }
            if (!firstField) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(field.getName() + " ");

            if (String.class.isAssignableFrom(field.getType())) {
                queryBuilder.append("TEXT");
            }
            else
            if (field.getType() == Long.class || field.getType()==Integer.class ||
                    field.getType() == Boolean.class) {
                queryBuilder.append("INTEGER");
            }
            Annotation annotation = field.getAnnotation(Attributes.class);
            if (annotation != null) {
                Attributes attr = (Attributes) annotation;
                if (attr.primaryKey())
                    queryBuilder.append(" PRIMARY KEY");
            }

            firstField = false;
        }

        queryBuilder.append(");");

        String query = queryBuilder.toString();
        db.execSQL(query);
    }
    public String getClassName(String className){

        String name="";
        try {
            Class<?> clazz;
            clazz = Class.forName(className);
            name = clazz.getSimpleName();

        } catch (ClassNotFoundException e) {
            Log.e("ABDH","problrem dropping table: "+name);
        }
        return name;
    }

    public void addObjectDataToTable(Object pObject, String pClassName) {
        Field[] fields;
        String lTableName;

        try {
            Class<?> clazz = Class.forName(pClassName);
            fields = clazz.getDeclaredFields();
            lTableName = clazz.getSimpleName();

        } catch (ClassNotFoundException e) {
            Log.e("ERROR", e.getMessage());
            return;
        }

        ContentValues values = new ContentValues();

        for(Field field:fields){
            field.setAccessible(true);
            try {
                if (String.class.isAssignableFrom(field.getType()) && field.get(pObject)!=null) {
                    values.put(field.getName(), (String)field.get(pObject));
                }
                else
                if (field.getType() == Long.class && field.get(pObject)!=null) {
                    values.put(field.getName(), (Long)field.get(pObject));
                }else
                if (field.getType() == Boolean.class && field.get(pObject)!=null) {
                    values.put(field.getName(), (Boolean)field.get(pObject)?1:0);
                }

            } catch (IllegalAccessException e) {
                Log.e("ERROR", e.getMessage());
            }
        }

        db.insert(lTableName, null, values);
    }

    public Object getDataToObject(Cursor pCursor, String pClassName) {

        Field[] fields;

        Class<?> clazz;
        Object object;
        try {
            clazz =Class.forName(pClassName);
            fields = clazz.getDeclaredFields();
            Constructor<?> ctor = clazz.getConstructor();
            object = ctor.newInstance();

        } catch (ClassNotFoundException e) {
            Log.e("ERROR:", e.getMessage());
            return null;
        } catch (InvocationTargetException e) {
            Log.e("ERROR:", e.getMessage());
            return null;
        } catch (NoSuchMethodException e) {
            Log.e("ERROR:", e.getMessage());
            return null;
        } catch (InstantiationException e) {
            Log.e("ERROR:", e.getMessage());
            return null;
        } catch (IllegalAccessException e) {
            Log.e("ERROR:", e.getMessage());
            return null;
        }

        if(pCursor.getCount()==0) {  //in case no data, set object to null
            return null;
        }

        int count = 0;
        for(Field field:fields){
            field.setAccessible(true);
            try {
                if (String.class.isAssignableFrom(field.getType()) && !pCursor.isAfterLast()) {
                    field.set(object, pCursor.getString(count));
                    count++;
                }else
                if ((field.getType() == Long.class ) && !pCursor.isAfterLast()) {
                    field.set(object, pCursor.getLong(count));
                    count++;
                }else
                if((field.getType() == Integer.class) &&
                        !pCursor.isAfterLast()){
                    field.set(object, pCursor.getInt(count));
                    count++;
                }else
                if((field.getType() == Boolean.class) &&
                        !pCursor.isAfterLast()){
                    field.set(object, pCursor.getInt(count)!=0);
                    count++;
                }

            } catch (IllegalAccessException e) {
                Log.e("ERROR:", e.getMessage());
            }

        }
        return object;
    }
}
