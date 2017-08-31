package eu.lynxworks.balancingact;

/**
 * Exercise class
 * - Defines an exercise.
 * - Always created with a name, duration and calorie value.
 */

class Exercise {
    // Attributes
    private String name;
    private float duration;
    private float calories;

    // Getters
    public String getName()     { return this.name; }
    public float getDuration()  { return this.duration; }
    public float getCalories()  { return this.calories; }

    // Setters
    private void setName(String name)        { this.name = name; }
    private void setDuration(float duration) { this.duration = duration; }
    private void setCalories(float calories) { this.calories = calories; }

    // Constructor
    public Exercise(String name, float duration, float calories){
        setName(name);
        setDuration(duration);
        setCalories(calories);
    }
}