package eu.lynxworks.balancingact;

/**
 * Created by Dougie Richardson (dr4485) on 18/06/17.
 */

public class Exercise {
    // Attributes
    private String name;
    private float duration;
    private float calories;

    // Getters
    public String getName()     { return this.name; }
    public float getDuration()  { return this.duration; }
    public float getCalories()  { return this.calories; }

    // Setters
    public void setName(String name)        { this.name = name; }
    public void setDuration(float duration) { this.duration = duration; }
    public void setCalories(float calories) { this.calories = calories; }

    // Constructor
    public Exercise(String name, float duration, float calories){
        setName(name);
        setDuration(duration);
        setCalories(calories);
    }
}
