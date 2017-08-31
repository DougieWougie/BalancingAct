package eu.lynxworks.balancingact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

class ActiveDayDatabaseManager extends SQLiteOpenHelper {
    /*  Inner classes are used to define the SQL schema as a contract, each relates to storing
    an object as a specific table. It also implements the BaseColumns interface which provides
    access to th _ID  field, an auto incremented integer value that uniquely identifies each row
    in a table. This is _not_ required however is recommended in recent API versions.
 */
    public class ActiveDayEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "activeDay_entry";

        // Column names
        public static final String COLUMN_DATE = "name";
        public static final String COLUMN_CALORIESIN = "caloriesConsumed";
        public static final String COLUMN_CALORIESOUT = "caloriesExpended";
        public static final String COLUMN_STEPS = "steps";
    }

    /*  Define commonly used SQL statements */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ActiveDayEntry.TABLE_NAME + " (" +
                    ActiveDayEntry._ID + "INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                    ActiveDayEntry.COLUMN_DATE + " TEXT," +
                    ActiveDayEntry.COLUMN_CALORIESIN + " REAL," +
                    ActiveDayEntry.COLUMN_CALORIESOUT + " REAL," +
                    ActiveDayEntry.COLUMN_STEPS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ActiveDayEntry.TABLE_NAME;

    private static final String SQL_SELECT_QUERY =
            "SELECT * FROM " + ActiveDayEntry.TABLE_NAME;

    // A change in schema needs an increment in database version!
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "ActiveDay.db";

    public ActiveDayDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ActiveDayEntry.TABLE_NAME);
        onCreate(db);
    }

    /*  Method takes an ActiveDay object and stores in the database. */
    public ActiveDay addActiveDay(ActiveDay activeDay){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        values.put(ActiveDayEntry.COLUMN_DATE, activeDay.getDate());
        values.put(ActiveDayEntry.COLUMN_CALORIESIN, activeDay.getCaloriesConsumed());
        values.put(ActiveDayEntry.COLUMN_CALORIESOUT, activeDay.getCaloriesExpended());
        values.put(ActiveDayEntry.COLUMN_STEPS, activeDay.getStepsTaken());
        db.insert(ActiveDayEntry.TABLE_NAME, null, values);
        db.close();
        return activeDay;
    }

    /*  Method returns a List of ActiveDay objects representing all the items stored in the
        database.
     */
    public List<ActiveDay> getAll() {
        List<ActiveDay> activeDays = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
            to a useful format (in this case a Food Object). Cursor Objects are initialised at
            index -1, which allows us to use the moveToNext() method in a while loop.
          */
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERY, null);
        while (cursor.moveToNext()){
            ActiveDay activeDay = new ActiveDay(
                    cursor.getString(0),
                    cursor.getFloat(1),
                    cursor.getFloat(2),
                    cursor.getInt(3));
            activeDays.add(activeDay);
        }
        return activeDays;
    }
}
