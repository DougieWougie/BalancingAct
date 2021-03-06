package eu.lynxworks.balancingact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles all SQLite database actions within the application. In an earlier iteration,
 * this was achieved by a handler for each class however although that was highly cohesive, the
 * low coupling made handling table joining and the functions of relational database poor.
 * There were also significant overheads as multiple database close calls were being made - these
 * have a significant overhead in Android, so now a single manager can be created in MainActivity
 * allowing us to close the database one time at when that is destroyed.
 */

class DatabaseManager extends SQLiteOpenHelper {
    /*  Inner classes are used to define the SQL schema as a contract, each relates to storing
        an object as a specific table. It also implements the BaseColumns interface which provides
        access to the _ID  field, an auto incremented integer value that uniquely identifies each row
        in a table. This is _not_ required however is recommended in recent API versions.
    */
    private class ExerciseEntry implements BaseColumns {
        /*  Table name */
        private static final String TABLE = "exercise_entry";

        /*  Column names */
        private static final String COLUMN_NAME = "name";
        private static final String COLUMN_DURATION = "duration";
        private static final String COLUMN_CALORIES = "calories";
        private static final String COLUMN_DATE = "date";

        /*  SQL statements  */
        private static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE " + ExerciseEntry.TABLE + " (" +
                        ExerciseEntry._ID + "INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                        ExerciseEntry.COLUMN_NAME + " TEXT," +
                        ExerciseEntry.COLUMN_DURATION + " REAL," +
                        ExerciseEntry.COLUMN_CALORIES + " REAL," +
                        ExerciseEntry.COLUMN_DATE + " INTEGER);";
        private static final String SQL_SELECT_QUERY =
                "SELECT * FROM " + ExerciseEntry.TABLE;
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
                        DayEntry._ID + " INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                        DayEntry.COLUMN_DATE + " TEXT," +
                        DayEntry.COLUMN_CALORIESIN + " INTEGER," +
                        DayEntry.COLUMN_CALORIESOUT + " INTEGER," +
                        DayEntry.COLUMN_STEPS + " INTEGER)";

        private static final String SQL_SELECT_QUERY =
                "SELECT * FROM " + DayEntry.TABLE;
    }

    private class FoodEntry implements BaseColumns {
        /*  Table name */
        private static final String TABLE = "food_entry";

        /*  Column names */
        private static final String COLUMN_DATE = "date";
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
                        FoodEntry.COLUMN_DATE + " TEXT," +
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
                        FoodEntry.COLUMN_SUGAR + " REAL);";

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

    /*  Method takes a date, creates a new Day object and saves it to the database. */
    public long addDay(Day day) {
        long key = 0;
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DayEntry.COLUMN_DATE, day.getTheDate());
            values.put(DayEntry.COLUMN_CALORIESIN, day.getCaloriesIn());
            values.put(DayEntry.COLUMN_CALORIESOUT, day.getCaloriesOut());
            values.put(DayEntry.COLUMN_STEPS, day.getSteps());
            key = db.insert(DayEntry.TABLE, null, values);
            db.close();
        } catch (Exception e) {
            Log.d("EXCEPTION", "Thrown in DatabaseManager->saveDay", e);
        }
        return key;
    }

    /*  Method takes a date and returns the corresponding Day entry in the database. */
    public void saveDay(Day day) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(DayEntry.COLUMN_DATE, day.getTheDate());
            values.put(DayEntry.COLUMN_CALORIESIN, day.getCaloriesIn());
            values.put(DayEntry.COLUMN_CALORIESOUT, day.getCaloriesOut());
            values.put(DayEntry.COLUMN_STEPS, day.getSteps());
            db.update(DayEntry.TABLE, values, DayEntry._ID + "=?", new String[]{String.valueOf(day.getID())});
            db.close();
        } catch (Exception e) {
            Log.d("EXCEPTION", "Thrown in DatabaseManager->saveDay", e);
        }
    }

    /*  Method takes a date and returns the corresponding Day entry in the database. */
    public List<Day> getAllDays() {
        List<Day> days = new ArrayList<>();
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(DayEntry.SQL_SELECT_QUERY, null);
            while (cursor.moveToNext()) {
                Day day = new Day(
                        cursor.getInt(cursor.getColumnIndex(DayEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(DayEntry.COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DayEntry.COLUMN_CALORIESIN)),
                        cursor.getInt(cursor.getColumnIndex(DayEntry.COLUMN_CALORIESOUT)),
                        cursor.getInt(cursor.getColumnIndex(DayEntry.COLUMN_STEPS)));
                days.add(day);
            }
            db.close();
            cursor.close();
        } catch (Exception e) {
            Log.d("EXCEPTION", "Thrown in DatabaseManager->addDay", e);
        }
        return days;
    }

    public Day getDayIfExists(String date) {
        String theQuery = DayEntry.SQL_SELECT_QUERY + " WHERE "
                + DayEntry.COLUMN_DATE + "='" + date + "';";
        Day day = null;
        try (SQLiteDatabase db = this.getReadableDatabase()) {
            Cursor cursor = db.rawQuery(theQuery, null);
            if (cursor.moveToFirst()) {
                day = new Day(
                        cursor.getInt(cursor.getColumnIndex(DayEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(DayEntry.COLUMN_DATE)),
                        cursor.getInt(cursor.getColumnIndex(DayEntry.COLUMN_CALORIESIN)),
                        cursor.getInt(cursor.getColumnIndex(DayEntry.COLUMN_CALORIESOUT)),
                        cursor.getInt(cursor.getColumnIndex(DayEntry.COLUMN_STEPS)));
            } else {
                day = null;
            }
            cursor.close();
        } catch (Exception e) {
            Log.d("EXCEPTION", "Thrown in DatabaseManager->getDay", e);
        }
        return day;
    }

    /*  Method takes a Food object and stores in the database. */
    public void addFood(Food food) {
        /*  Thanks to Android using JDK 6/7 and not 8 we have to mess around storing dates as
            strings.
         */
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(FoodEntry.COLUMN_DATE, food.getDate());
        values.put(FoodEntry.COLUMN_BARCODE, food.getBarcode());
        values.put(FoodEntry.COLUMN_PRODUCTNAME, food.getProductName());
        values.put(FoodEntry.COLUMN_QUANTITY, food.getQuantity());
        values.put(FoodEntry.COLUMN_BRAND, food.getBrand());
        values.put(FoodEntry.COLUMN_SALT, food.getSalt());
        values.put(FoodEntry.COLUMN_ENERGY, food.getEnergy());
        values.put(FoodEntry.COLUMN_CARBOHYDRATE, food.getCarbohydrate());
        values.put(FoodEntry.COLUMN_PROTEIN, food.getProtein());
        values.put(FoodEntry.COLUMN_FAT, food.getFat());
        values.put(FoodEntry.COLUMN_FIBRE, food.getFibre());
        values.put(FoodEntry.COLUMN_SUGAR, food.getSugar());
        db.insert(FoodEntry.TABLE, null, values);
        db.close();
    }

    /*  Method takes an Exercise object and stores in the database. Note that the Day foreign
        key is not added here - that's done by a DAO object elsewhere.
    */
    public void addExercise(Exercise exercise) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_NAME, exercise.getName());
        values.put(ExerciseEntry.COLUMN_DURATION, exercise.getDuration());
        values.put(ExerciseEntry.COLUMN_CALORIES, exercise.getCalories());
        values.put(ExerciseEntry.COLUMN_DATE, exercise.getDay());
        db.insert(ExerciseEntry.TABLE, null, values);
        db.close();
    }

    /*  Method takes an User object and stores in the database. */
    public long addUser(User user) {
        long key;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, user.getName());
        values.put(UserEntry.COLUMN_AGE, user.getAge());
        values.put(UserEntry.COLUMN_HEIGHT, user.getHeight());
        values.put(UserEntry.COLUMN_WEIGHT, user.getWeight());
        values.put(UserEntry.COLUMN_SEX, user.getSex());
        values.put(UserEntry.COLUMN_ACTIVITY, user.getActivityLevel());
        key = db.insert(UserEntry.TABLE, null, values);
        db.close();
        return key;
    }

    /*  Method updates a specific record. */
    public void updateUser(User user) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues values = new ContentValues();
            values.put(UserEntry.COLUMN_NAME, user.getName());
            values.put(UserEntry.COLUMN_AGE, user.getAge());
            values.put(UserEntry.COLUMN_HEIGHT, user.getHeight());
            values.put(UserEntry.COLUMN_WEIGHT, user.getWeight());
            values.put(UserEntry.COLUMN_SEX, user.getSex());
            values.put(UserEntry.COLUMN_ACTIVITY, user.getActivityLevel());
            db.update(UserEntry.TABLE, values, UserEntry._ID + "=?", new String[]{String.valueOf(user.getID())});
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*  Method returns a List of Food objects representing all the items stored in the
        database for a specific day.
    */
    public List<Food> getDayFood(String date) {
        List<Food> foods = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
            to a useful format (in this case a Food Object). Cursor Objects are initialised at
            index -1, which allows us to use the moveToNext() method in a while loop.

            Try/Finally is replaceable with automatic resource management.
          */
        String SQL_QUERY = FoodEntry.SQL_SELECT_QUERY + " WHERE "
                + FoodEntry.COLUMN_DATE + "='" + date + "';";

        try (Cursor cursor = db.rawQuery(SQL_QUERY, null)) {
            while (cursor.moveToNext()) {
                Food food = new Food.Builder(
                        cursor.getString(cursor.getColumnIndex(FoodEntry.COLUMN_DATE)),
                        cursor.getString(cursor.getColumnIndex(FoodEntry.COLUMN_PRODUCTNAME)),
                        cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_QUANTITY)),
                        cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_ENERGY)))
                        .barcode(cursor.getString(cursor.getColumnIndex(FoodEntry.COLUMN_BARCODE)))
                        .brand(cursor.getString(cursor.getColumnIndex(FoodEntry.COLUMN_BRAND)))
                        .salt(cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_SALT)))
                        .carbohydrate(cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_CARBOHYDRATE)))
                        .protein(cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_PRODUCTNAME)))
                        .fat(cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_FAT)))
                        .fibre(cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_FIBRE)))
                        .sugar(cursor.getFloat(cursor.getColumnIndex(FoodEntry.COLUMN_SUGAR)))
                        .build();
                foods.add(food);
            }
        } catch (Exception e) {
            Log.d("EXCEPTION", "Exception in DatabaseManager->getDayFood", e);
        }
        return foods;
    }


    /*  Method returns a List of Exercise objects representing all the items stored in the
    database.
*/
    public List<Exercise> getDayExercise(String date) {
        List<Exercise> exercises = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
            to a useful format (in this case a Food Object). Cursor Objects are initialised at
            index -1, which allows us to use the moveToNext() method in a while loop.

            Cursor access is troublesome, so we utilise the getString with getColumnIndex - this
            prevents accessing the wrong field.
          */

        String theQuery = ExerciseEntry.SQL_SELECT_QUERY + " WHERE "
                + ExerciseEntry.COLUMN_DATE + "='" + date + "';";

        Cursor cursor = db.rawQuery(theQuery, null);
        while (cursor.moveToNext()) {
            Exercise exercise = new Exercise(
                    cursor.getString(cursor.getColumnIndex(ExerciseEntry.COLUMN_NAME)),
                    cursor.getFloat(cursor.getColumnIndex(ExerciseEntry.COLUMN_DURATION)),
                    cursor.getFloat(cursor.getColumnIndex(ExerciseEntry.COLUMN_CALORIES)),
                    cursor.getString(cursor.getColumnIndex(ExerciseEntry.COLUMN_DATE)));
            exercises.add(exercise);
        }
        cursor.close();
        return exercises;
    }

    /*  Method returns a List of User objects representing all the items stored in the
        database.
    */
    public List<User> getAllUser() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
            to a useful format (in this case a Food Object). Cursor Objects are initialised at
            index -1, which allows us to use the moveToNext() method in a while loop.
          */
        try {
            Cursor cursor = db.rawQuery(UserEntry.SQL_SELECT_QUERY, null);
            while (cursor.moveToNext()) {
                User user = new User(
                        cursor.getLong(cursor.getColumnIndex(UserEntry._ID)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_NAME)),
                        cursor.getInt(cursor.getColumnIndex(UserEntry.COLUMN_AGE)),
                        cursor.getFloat(cursor.getColumnIndex(UserEntry.COLUMN_HEIGHT)),
                        cursor.getFloat(cursor.getColumnIndex(UserEntry.COLUMN_WEIGHT)),
                        cursor.getString(cursor.getColumnIndex(UserEntry.COLUMN_SEX)),
                        cursor.getInt(cursor.getColumnIndex(UserEntry.COLUMN_ACTIVITY)));
                users.add(user);
            }
            cursor.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return users;
    }
}