package digitalfish.test.pojotosqlitedemo.SQLite;

import android.database.Cursor;

/**
 * An interface to the DFlex class
 * Created by Piotr Malak on 2015-09-27.
 */
public interface IDFlex {

    /**
     * Creates SQLite Database table based on POJO class;
     * @param className - a POJO class name
     */
    void createTableFromClass(String className);

    /**
     * Adds object determined by pClassName to table named pClassName
     * @param pObject - an object to save in database
     * @param pClassName - name of class.
     */
    void addObjectDataToTable(Object pObject, String pClassName);

    /**
     * Checks if class is present in the project and returns its name if yes. Empty string otherwise.
     * @param className class name to check
     * @return class name
     */
    String getClassName(String className);

    /**
     * Reads data from db, creates class instance and adds data to it.
     * @param pCursor db cursor containing requested data.
     * @param pClassName class/table name
     * @return created object.
     */
    Object getDataToObject(Cursor pCursor, String pClassName);
}
