package eu.lynxworks.balancingact;

/**
 * Created by Dougie Richardson on 18/06/17.
 * User is an Object that describes the Entity "User". It has attributes relating to physical
 * attributes of an individual used in the calculation of various exercise and diet related
 * methods.
 */

// TODO: this should be set as a singleton, there's only one user!

public class User {
    // Attributes
    private String name;
    private int age;            // Years
    private float height;       // cm
    private float weight;       // cm
    private int activityLevel;  // 1-4 relates to exercise and defined below

    // Activity levels (see above)
    private static final int ACTIVE_SEDENTARY   = 1;
    private static final int ACTIVE_MODERATE    = 2;
    private static final int ACTIVE_ACTIVE      = 3;
    private static final int ACTIVE_EXTREME     = 4;

    // Getters
    public String getName()         { return this.name; }
    public int getAge()             { return this.age; }
    public float getHeight()        { return this.height; }
    public float getWeight()        { return this.weight; }
    public int getActivityLevel()   { return this.activityLevel; }

    // Setters - not everything changes but it's usefull to be able to make alterations.
    public void setName(String name)                { this.name = name; }
    public void setAge(int age)                     { this.age = age; }
    public void setHeight(float height)             { this.height = height; }
    public void setWeight(float weight)             { this.weight = weight; }
    public void setActivityLevel(int activityLevel) { this.activityLevel = activityLevel; }

    // Constructor
    public User(String name, int age, float height, float weight, int activityLevel) {
        setName(name);
        setAge(age);
        setHeight(height);
        setWeight(weight);
        setActivityLevel(activityLevel);
    }
}
