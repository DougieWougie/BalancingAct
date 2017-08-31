package eu.lynxworks.balancingact;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActiveDay {
    // Attributes
    private String date;
    private float caloriesConsumed;
    private float caloriesExpended;
    private int stepsTaken;

    // Getters
    public String getDate()   { return date; }
    public float getCaloriesConsumed()  { return caloriesConsumed; }
    public float getCaloriesExpended()  { return caloriesExpended; }
    public int getStepsTaken()          { return stepsTaken; }

    // Setters
    private void setDate(String date) {
        this.date = date;
    }
    private void setCaloriesConsumed(float caloriesConsumed) {
        this.caloriesConsumed = caloriesConsumed;
    }

    private void setCaloriesExpended(float caloriesExpended) {
        this.caloriesExpended = caloriesExpended;
    }

    private void setStepsTaken(int stepsTaken) {
        this.stepsTaken = stepsTaken;
    }

    /*  Constructors - there are  three circumstances:
            (1) no parameters for today's date and all other attributes are set to zero;
            (2) a specific date (for when we want to record activity on a date in the past and all
                other attributes are set to zero; and
            (3) all parameters provided used when retrieving data from an SQL database.000000

     */
    public ActiveDay(){
        // Dates are stored as Strings in a specific format to keep things simple.
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            date = simpleDateFormat.parse(date.toString());
            setDate(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setCaloriesConsumed(0);
        setCaloriesExpended(0);
        setStepsTaken(0);
    }

    public ActiveDay(Date date){
        // Dates are stored as Strings in a specific format to keep things simple.
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            date = simpleDateFormat.parse(date.toString());
            setDate(simpleDateFormat.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        setCaloriesConsumed(0);
        setCaloriesExpended(0);
        setStepsTaken(0);
    }
    public ActiveDay(String date, float caloriesConsumed, float caloriesExpended, int stepsTaken){
        setDate(date);
        setCaloriesConsumed(caloriesConsumed);
        setCaloriesExpended(caloriesExpended);
        setStepsTaken(stepsTaken);
    }
}
