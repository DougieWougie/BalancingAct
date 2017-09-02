package eu.lynxworks.balancingact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

class DayDatabaseManager extends SQLiteOpenHelper {
    /*  Inner classes are used to define the SQL schema as a contract, each relates to storing
    an object as a specific table. It also implements the BaseColumns interface which provides
    access to th _ID  field, an auto incremented integer value that uniquely identifies each row
    in a table. This is _not_ required however is recommended in recent API versions.
 */
    public class DayEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "day_entry";

        // Column names
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_CALORIESIN = "caloriesConsumed";
        public static final String COLUMN_CALORIESOUT = "caloriesExpended";
        public static final String COLUMN_STEPS = "steps";
    }

    /*  Define commonly used SQL statements */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DayEntry.TABLE_NAME + " (" +
                    DayEntry._ID + "INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                    DayEntry.COLUMN_DATE + " TEXT," +
                    DayEntry.COLUMN_CALORIESIN + " REAL," +
                    DayEntry.COLUMN_CALORIESOUT + " REAL," +
                    DayEntry.COLUMN_STEPS + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DayEntry.TABLE_NAME;

    private static final String SQL_SELECT_QUERY =
            "SELECT * FROM " + DayEntry.TABLE_NAME;

    // A change in schema needs an increment in database version!
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BalancingAct.db";

    public DayDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DayEntry.TABLE_NAME);
        onCreate(db);
    }

//    /*  TODO: these require either serialization or some sort of adapter.
// Method takes an Day object and stores in the database. */
//    public Day addDay(Day day){
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
//        values.put(DayEntry.COLUMN_DATE, day.getDate());
//        values.put(DayEntry.COLUMN_CALORIESIN, day.getCaloriesConsumed());
//        values.put(DayEntry.COLUMN_CALORIESOUT, day.getCaloriesExpended());
//        values.put(DayEntry.COLUMN_STEPS, day.getStepsTaken());
//        db.insert(DayEntry.TABLE_NAME, null, values);
//        db.close();
//        return day;
//    }
//
//    /*  Method returns a List of Day objects representing all the items stored in the
//        database.
//     */
//    public List<Day> getAll() {
//        List<Day> days = new ArrayList<>();
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
//            to a useful format (in this case a Food Object). Cursor Objects are initialised at
//            index -1, which allows us to use the moveToNext() method in a while loop.
//          */
//        Cursor cursor = db.rawQuery(SQL_SELECT_QUERY, null);
//        while (cursor.moveToNext()){
//            Day day = new Day(
//                    cursor.getString(0),
//                    cursor.getFloat(1),
//                    cursor.getFloat(2),
//                    cursor.getInt(3));
//            days.add(day);
//        }
//        return days;
//    }
}
