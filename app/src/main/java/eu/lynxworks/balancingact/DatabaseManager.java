package eu.lynxworks.balancingact;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 *  This class handles all SQLite database actions within the application. In an earlier iteration,
 *  this was achieved by a hnadler for each class however although that was highly cohesive, the
 *  low coupling made handling table joining and the functions of relational database poor.
 *  There were also significant overheads as mulitple databse close calls were being made - these
 *  have a significant overhead in Android, so now a single manager can be created in MainActivity
 *  allowing us to close the database one time at when that is destroyed.
 */

public class DatabaseManager extends SQLiteOpenHelper {
    /*  Inner classes are used to define the SQL schema as a contract, each relates to storing
        an object as a specific table. It also implements the BaseColumns interface which provides
        access to th _ID  field, an auto incremented integer value that uniquely identifies each row
        in a table. This is _not_ required however is recommended in recent API versions.
    */
    private class ExerciseEntry implements BaseColumns {
        /*  Table name */
        private static final String TABLE = "exercise_entry";

        /*  Column names */
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_DURATION = "duration";
        private static final String COLUMN_CALORIES = "calories";

        /*  Foreign key used to join Exercise and the Day that it was done on. */
        private static final String COLUMN_DAY = "day";

        /*  SQL statements  */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ExerciseEntry.TABLE + " (" +
                        ExerciseEntry._ID + "INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                        ExerciseEntry.COLUMN_NAME + " TEXT," +
                        ExerciseEntry.COLUMN_DURATION + " REAL," +
                        ExerciseEntry.COLUMN_CALORIES + " REAL," +
                        ExerciseEntry.COLUMN_DAY + " INTEGER);";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ExerciseEntry.TABLE;
        private static final String SQL_SELECT_QUERY =
                "SELECT * FROM " + ExerciseEntry.TABLE;
        private static final String SQL_DAY_QUERY =
                "SELECT * FROM " + ExerciseEntry.TABLE +
                        "JOIN ";
    }

    private class UserEntry implements BaseColumns {
        /*  Table name */
        private static final String TABLE = "user_entry";

        /*  Column names */
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_AGE = "age";
        private static final String COLUMN_HEIGHT = "height";
        private static final String COLUMN_WEIGHT = "weight";
        private static final String COLUMN_SEX = "sex";
        private static final String COLUMN_ACTIVITY = "activityLevel";

        /*  Define commonly used SQL statements */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + UserEntry.TABLE + " (" +
                        UserEntry._ID + " INTEGER PRIMARY KEY, " +    // This is the BaseColumns _ID!
                        UserEntry.COLUMN_NAME + " TEXT, " +
                        UserEntry.COLUMN_AGE + " INTEGER, " +
                        UserEntry.COLUMN_HEIGHT + " REAL, " +
                        UserEntry.COLUMN_WEIGHT + " REAL, " +
                        UserEntry.COLUMN_SEX + " TEXT, " +
                        UserEntry.COLUMN_ACTIVITY + " INTEGER)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + UserEntry.TABLE;

        private static final String SQL_SELECT_QUERY =
                "SELECT * FROM " + UserEntry.TABLE;
    }

    private class DayEntry implements BaseColumns {
        /*  Table name */
        private static final String TABLE = "day_entry";

        /*  Column names */
        private static final String COLUMN_DATE = "date";
        private static final String COLUMN_CALORIESIN = "caloriesConsumed";
        private static final String COLUMN_CALORIESOUT = "caloriesExpended";
        private static final String COLUMN_STEPS = "steps";

        /*  Define commonly used SQL statements */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + DayEntry.TABLE + " (" +
                        DayEntry._ID + "INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                        DayEntry.COLUMN_DATE + " TEXT," +
                        DayEntry.COLUMN_CALORIESIN + " REAL," +
                        DayEntry.COLUMN_CALORIESOUT + " REAL," +
                        DayEntry.COLUMN_STEPS + " INTEGER)";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DayEntry.TABLE;

        private static final String SQL_SELECT_QUERY =
                "SELECT * FROM " + DayEntry.TABLE;
    }

    private class FoodEntry implements BaseColumns {
        /*  Table name */
        private static final String TABLE = "food_entry";

        /*  Column names */
        private static final String COLUMN_BARCODE = "barcode";
        private static final String COLUMN_PRODUCTNAME = "productName";
        private static final String COLUMN_QUANTITY = "quantity";
        private static final String COLUMN_BRAND = "brand";
        private static final String COLUMN_SALT = "salt";
        private static final String COLUMN_ENERGY = "energy";
        private static final String COLUMN_CARBOHYDRATE = "carbohydrate";
        private static final String COLUMN_PROTEIN = "proteins";
        private static final String COLUMN_FAT = "fat";
        private static final String COLUMN_FIBRE = "fibre";
        private static final String COLUMN_SUGAR = "sugar";

        /*  Define commonly used SQL statements */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + FoodEntry.TABLE + " (" +
                        FoodEntry._ID + "INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                        FoodEntry.COLUMN_BARCODE + " INTEGER," +
                        FoodEntry.COLUMN_PRODUCTNAME + " TEXT," +
                        FoodEntry.COLUMN_QUANTITY + " REAL," +
                        FoodEntry.COLUMN_BRAND + " TEXT, " +
                        FoodEntry.COLUMN_SALT + " REAL," +
                        FoodEntry.COLUMN_ENERGY + " REAL," +
                        FoodEntry.COLUMN_CARBOHYDRATE + " REAL," +
                        FoodEntry.COLUMN_PROTEIN + " REAL," +
                        FoodEntry.COLUMN_FAT + " REAL," +
                        FoodEntry.COLUMN_FIBRE + " REAL," +
                        FoodEntry.COLUMN_SUGAR + " REAL)";
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + FoodEntry.TABLE;

        private static final String SQL_SELECT_QUERY =
                "SELECT * FROM " + FoodEntry.TABLE;
    }
    
    /*  Changing the schema requires that DATABASE_VERSION is incremented or the tables will
       not be updated correctly.
    */
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "BalancingAct.db";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ExerciseEntry.SQL_CREATE_ENTRIES);
        db.execSQL(UserEntry.SQL_CREATE_ENTRIES);
        db.execSQL(DayEntry.SQL_CREATE_ENTRIES);
        db.execSQL(FoodEntry.SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DayEntry.TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + FoodEntry.TABLE);
        onCreate(db);
    }

    public void drop() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL(ExerciseEntry.SQL_DELETE_ENTRIES);
        db.execSQL(UserEntry.SQL_DELETE_ENTRIES);
        db.execSQL(DayEntry.SQL_DELETE_ENTRIES);
        db.execSQL(FoodEntry.SQL_DELETE_ENTRIES);
    }
}