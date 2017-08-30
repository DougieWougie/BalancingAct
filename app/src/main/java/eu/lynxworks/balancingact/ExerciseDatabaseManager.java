package eu.lynxworks.balancingact;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dougie Richardson (dr4485) on 30/08/17.
 */

public class ExerciseDatabaseManager extends SQLiteOpenHelper {
    /*  Inner classes are used to define the SQL schema as a contract, each relates to storing
        an object as a specific table. It also implements the BaseColumns interface which provides
        access to th _ID  field, an autoincremented integer value that uniquely identifies each row
        in a table. This is _not_ required however is recommended in recent API versions.
     */
    public class ExerciseEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "exercise_entry";

        // Column names
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_DURATION = "duration";
        public static final String COLUMN_CALORIES = "calories";
    }

    /*  Define commonly used SQL statements */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + ExerciseEntry.TABLE_NAME + " (" +
                    ExerciseEntry._ID + "INTEGER PRIMARY KEY," +    // This is the BaseColumns _ID!
                    ExerciseEntry.COLUMN_NAME + " TEXT," +
                    ExerciseEntry.COLUMN_DURATION + " REAL," +
                    ExerciseEntry.COLUMN_CALORIES + " REAL)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME;

    private static final String SQL_SELECT_QUERY =
            "SELECT * FROM " + ExerciseEntry.TABLE_NAME;

    // A change in schema needs an increment in database version!
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Exercise.db";

    public ExerciseDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ExerciseEntry.TABLE_NAME);
        onCreate(db);
    }

    /*  Method takes an Exercise object and stores in the database. */
    public Exercise addExercise(Exercise exercise){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(ExerciseEntry.COLUMN_NAME, exercise.getName());
        values.put(ExerciseEntry.COLUMN_DURATION, exercise.getDuration());
        db.insert(ExerciseEntry.TABLE_NAME, null, values);
        db.close();
        return exercise;
    }

    /*  Method returns a List of Exercise objects representing all the items stored in the
        database.
     */
    public List<Exercise> getAll() {
        List<Exercise> exercises = new ArrayList<Exercise>();
        SQLiteDatabase db = this.getReadableDatabase();

        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
            to a useful format (in this case a Food Object). Cursor Objects are initialised at
            index -1, which allows us to use the moveToNext() method in a while loop.
          */
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERY, null);
        while (cursor.moveToNext()){
            Exercise exercise = new Exercise(
                    cursor.getString(0),
                    cursor.getFloat(1),
                    cursor.getFloat(2));
            exercises.add(exercise);
        }
        return exercises;
    }
}
