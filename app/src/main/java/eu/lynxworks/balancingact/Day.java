package eu.lynxworks.balancingact;

import android.content.Context;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

class Day {
    // TODO: Incorporate BMR!
    /*  Attributes */
    private long ID;
    private String theDate;
    private int caloriesIn;
    private int caloriesOut;
    private List<Exercise> exercises;
    private List<Food> foods;
    private int steps;

    /*  Getters */
    public long getID()                                  { return this.ID; }
    public String getTheDate()                           { return this.theDate; }
    public int getCaloriesIn()                           { return this.caloriesIn; }
    public int getCaloriesOut()                          { return this.caloriesOut; }
    private List<Exercise> getExercises()                { return this.exercises; }
    private List<Food> getFoods()                        { return this.foods; }
    public int getSteps()                                { return this.steps; }

    /*  Setters */
    public  void setID(long anID)                        { this.ID = anID; }
    private void setTheDate(String date)                 { this.theDate = date; }
    private void setCaloriesIn(int calories)             { this.caloriesIn = calories; }
    private void setCaloriesOut(int calories)            { this.caloriesOut = calories; }
    private void setExercises(List<Exercise> exercises)  { this.exercises = exercises; }
    private void setFoods(List<Food> foods)              { this.foods = foods; }
    private void setSteps(int steps)                     { this.steps = steps; }

    /*  Constructor used when creating a new Day. */
    public Day(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            setTheDate(simpleDateFormat.format(date));
        } catch (Exception e) {
            Log.d("Dougie", "Exception in ", e);
        }
        setCaloriesIn(0);
        setCaloriesOut(0);
        setSteps(0);
    }

    /*  Constructor that takes more parameters - used mainly to recreate objects from
        database managers.
     */
    public Day(long anID, String date, int caloriesIn, int caloriesOut, int steps){
        setID(anID);
        setTheDate(date);
        setCaloriesIn(caloriesIn);
        setCaloriesOut(caloriesOut);
        setSteps(steps);
    }

    public void update(Context context){
        try {
            DatabaseManager dbManager = new DatabaseManager(context);
            setExercises(dbManager.getDayExercise(getTheDate()));
            setFoods(dbManager.getDayFood(getTheDate()));
        }
        catch (Exception e){
            Log.d("EXCEPTION", "Thrown in Day->update()", e);
        }
        setCaloriesIn(caloriesConsumed());
        setCaloriesOut(caloriesExpended());
    }

    /*  The next two methods iterate through the associated data and total the amount
        of calories consumed and expended.
     */
    private int caloriesConsumed(){
        int totalCalories = 0;
        for(Food food:getFoods()){
            totalCalories += food.getEnergy();
        }
        return totalCalories;
    }

    private int caloriesExpended(){
        int totalCalories = 0;
        for(Exercise exercise:getExercises()){
            totalCalories += exercise.getCalories();
        }
        return totalCalories;
    }

    /*  This method works out the calorie balance. */
    public int calorieBalance() {
        return (getCaloriesIn() - getCaloriesOut());
    }

    /*  Over riding toString is a technique advised in Effective Java 2nd edition
        by Joshua Bloch. The superclass returns a fairly unhelpful hashcode representing
        the class - this allows a more useful message. In this instance, the data.
     */
    @Override
    public String toString(){
        return (this.getTheDate() + " / " +
                this.getFoods() + " / " +
                this.getExercises());
    }
}
