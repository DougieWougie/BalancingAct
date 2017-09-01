package eu.lynxworks.balancingact;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.util.List;
import java.util.Date;

/**
 * Created by Dougie Richardson (dr4485) on 01/09/17.
 */

public class Day {
    /*  Attributes */
    private String theDate;
    private List<Exercise> exercises;
    private List<Food> foods;
    private int steps;

    /*  Getters */
    public String getTheDate()              { return this.theDate; }
    public List<Exercise> getExercises()    { return this.exercises; }
    public List<Food> getFoods()            { return this.foods; }
    public int getSteps()                   { return this.steps; }

    /*  Setters */
    public void setTheDate(Date date) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        try {
            this.theDate = simpleDateFormat.format(simpleDateFormat.parse(date.toString()));
        } catch (Exception e) {
            Log.d("Dougie", "Exception in ", e);
        }
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public void setFoods(List<Food> foods) {
        this.foods = foods;
    }

    /*  TODO: Setter for the daily step counter. */
    public void setSteps(){

    }

    public void addExercise(Exercise exercise) {
        this.exercises.add(exercise);
    }

    public void addFood(Food food) {
        this.foods.add(food);
    }

    public void removeExercise(Exercise exercise){
        this.exercises.remove(exercise);
    }

    public void removeFood(Food food){
        this.foods.remove(food);
    }

    /*  The next two methods iterate through the associated data and total the amount
        of calories consumed and expended.
     */
    public float caloriesConsumed(){
        float totalCalories = 0f;
        for(Food food:getFoods()){
            totalCalories += food.getEnergy();
        }
        return totalCalories;
    }

    public float caloriesExpended(){
        float totalCalories = 0f;
        for(Exercise exercise:getExercises()){
            totalCalories += exercise.getCalories();
        }
        return totalCalories;
    }

    /*  This method works out the calorie balance. */
    public float calorieBalance() {
        return caloriesConsumed() - caloriesExpended();
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
