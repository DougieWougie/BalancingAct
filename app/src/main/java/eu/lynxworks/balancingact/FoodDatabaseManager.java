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
 * Manages the SQLite database used to record food objects.
 */

public class FoodDatabaseManager extends SQLiteOpenHelper {
    /*  Inner classes are used to define the SQL schema as a contract, each relates to storing
        an object as a specific table. It also implements the BaseColumns interface which provides
        access to th _ID  field, an autoincremented integer value that uniquely identifies each row
        in a table. This is _not_ required however is recommended in recent API versions.
     */
    public class FoodEntry implements BaseColumns {
        // Table name
        public static final String TABLE_NAME = "food_entry";

        // Column names
        public static final String COLUMN_BARCODE = "barcode";
        public static final String COLUMN_PRODUCTNAME = "productName";
        public static final String COLUMN_QUANTITY = "quantity";
        public static final String COLUMN_BRAND = "brand";
        public static final String COLUMN_SALT = "salt";
        public static final String COLUMN_ENERGY = "energy";
        public static final String COLUMN_CARBOHYDRATE = "carbohydrate";
        public static final String COLUMN_PROTEIN = "proteins";
        public static final String COLUMN_FAT = "fat";
        public static final String COLUMN_FIBRE = "fibre";
        public static final String COLUMN_SUGAR = "sugar";
    }

    /*  Define commonly used SQL statements */
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FoodEntry.TABLE_NAME + " (" +
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
            "DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME;

    private static final String SQL_SELECT_QUERY =
            "SELECT * FROM " + FoodEntry.TABLE_NAME;

    // A change in schema needs an increment in database version!
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Food.db";

    public FoodDatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + FoodEntry.TABLE_NAME);
        onCreate(db);
    }

    /*  Method takes a Food object and stores in the database. */
    public Food addFood(Food food){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
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
        db.insert(FoodEntry.TABLE_NAME, null, values);
        db.close();
        return food;
    }

    /*  Method returns a List of Food objects representing all the items stored in the
        database.
     */
    public List<Food> getAll() {
        List<Food> foods = new ArrayList<Food>();
        SQLiteDatabase db = this.getReadableDatabase();

        /*  SQLite database return SQL queries as Cursor Objects. This needs to be adapted
            to a useful format (in this case a Food Object). Cursor Objects are initialised at
            index -1, which allows us to use the moveToNext() method in a while loop.
          */
        Cursor cursor = db.rawQuery(SQL_SELECT_QUERY, null);
        try{
            while (cursor.moveToNext()){
                Food food = new Food.Builder(
                        cursor.getString(2),
                        cursor.getFloat(3),
                        cursor.getFloat(6))
                        .barcode(cursor.getLong(1))
                        .brand(cursor.getString(4))
                        .salt(cursor.getFloat(5))
                        .carbohydrate(cursor.getFloat(7))
                        .protein(cursor.getFloat(8))
                        .fat(cursor.getFloat(9))
                        .fibre(cursor.getFloat(10))
                        .sugar(cursor.getFloat(11))
                        .build();
                foods.add(food);
            }
        }
        finally {
            cursor.close();
        }
        return foods;
    }
}