package eu.lynxworks.balancingact;

/**
 * Exercise class
 * - Defines an exercise.
 * - Always created with a name, duration and calorie value.
 */

class Exercise {
    /*  Attributes */
    private String name;
    private float duration;
    private float calories;
    private String day;

    /*  Getters */
    public String getName()     { return this.name; }
    public float getDuration()  { return this.duration; }
    public float getCalories()  { return this.calories; }
    public String getDay()      { return this.day; }

    /*  Setters */
    private void setName(String name)        { this.name = name; }
    private void setDuration(float duration) { this.duration = duration; }
    private void setCalories(float calories) { this.calories = calories; }
    private void setDay(String day)          { this.day = day; }

    /*  Constructor
        There need not be a Day associated with an Exercise at this stage but it is needed
        to assure the tables are joined correctly and link Day and Exercise objects.
     */
    public Exercise(String name, float duration, float calories){
        setName(name);
        setDuration(duration);
        setCalories(calories);
        setDay(null);
    }

    public Exercise(String name, float duration, float calories, String day){
        setName(name);
        setDuration(duration);
        setCalories(calories);
        setDay(day);
    }
}