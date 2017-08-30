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
 * Created by Dougie Richardson (dr4485) on 18/06/17.
 */

public class UserDatabaseManager extends SQLiteOpenHelper{
    /*  Inner classes are used to define the SQL schema as a contract, each relates to storing
    an object as a specific table. It also implements the BaseColumns interface which provides
    access to th _ID  field, an autoincremented integer value that uniquely identifies each row
    in a table. This is _not_ required however is recommended in recent API versions.
 */
    public class UserEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "user_entry";

        // Column names
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_AGE = "age";
        public static final String COLUMN_HEIGHT = "height";
        public static final String COLUMN_WEIGHT = "weight";
        public static final String COLUMN_ACTIVITY = "activityLevel";
    }

    /*  Define commonly used SQL statements */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                    UserEntry._ID + " INTEGER PRIMARY KEY, " +    // This is the BaseColumns _ID!
                    UserEntry.COLUMN_NAME + " TEXT, " +
                    UserEntry.COLUMN_AGE + " INTEGER, " +
                    UserEntry.COLUMN_HEIGHT + " REAL, " +
                    UserEntry.COLUMN_WEIGHT + " REAL, " +
                    UserEntry.COLUMN_ACTIVITY + " INTEGER)";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME;

    private static final String SQL_SELECT_QUERY =
            "SELECT * FROM " + UserEntry.TABLE_NAME;

    // A change in schema needs an increment in database version!
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "User.db";

    public UserDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        onCreate(db);
    }

    /*  Method takes an User object and stores in the database. */
    public User addUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserEntry.COLUMN_NAME, user.getName());
        values.put(UserEntry.COLUMN_AGE, user.getAge());
        values.put(UserEntry.COLUMN_HEIGHT, user.getHeight());
        values.put(UserEntry.COLUMN_WEIGHT, user.getWeight());
        values.put(UserEntry.COLUMN_ACTIVITY, user.getActivityLevel());
        db.insert(UserEntry.TABLE_NAME, null, values);
        db.close();
        return user;
    }

    /*  Method returns a List of User objects representing all the items stored in the
        database.
     */
    public List<User> getAll() {
        List<User> users = new ArrayList<User>();
        SQLiteDatabase db = this.getReadableDatabase();

        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
            to a useful format (in this case a Food Object). Cursor Objects are initialised at
            index -1, which allows us to use the moveToNext() method in a while loop.
          */
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERY, null);
        while (cursor.moveToNext()){
            User user = new User(
                    cursor.getString(0),
                    cursor.getInt(1),
                    cursor.getFloat(2),
                    cursor.getFloat(3),
                    cursor.getInt(4));
            users.add(user);
        }
        return users;
    }
}