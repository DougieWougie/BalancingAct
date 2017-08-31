package eu.lynxworks.balancingact;

/**
 * Created by Dougie Richardson on 18/06/17.
 * User is an Object that describes the Entity "User". It has attributes relating to physical
 * attributes of an individual used in the calculation of various exercise and diet related
 * methods.
 */

// TODO: this should be set as a singleton, there's only one user!

class User {
    /*  Attributes */
    private String name;
    private int age;            // Years
    private float height;       // cm
    private float weight;       // kg
    private String sex;         // male or female
    private int activityLevel;  // 1-4 relates to exercise and defined below

    /*  Activity levels (see above) */
    private static final int ACTIVE_SEDENTARY   = 1;
    private static final int ACTIVE_MODERATE    = 2;
    private static final int ACTIVE_ACTIVE      = 3;
    private static final int ACTIVE_EXTREME     = 4;

    /*  Getters */
    public String getName()         { return this.name; }
    public int getAge()             { return this.age; }
    public float getHeight()        { return this.height; }
    public float getWeight()        { return this.weight; }
    public String getSex()          { return this.sex; }
    public int getActivityLevel()   { return this.activityLevel; }

    /*  Setters - not everything changes but it's useful to be able to make alterations. */
    private void setName(String name)                { this.name = name; }
    private void setAge(int age)                     { this.age = age; }
    private void setHeight(float height)             { this.height = height; }
    private void setWeight(float weight)             { this.weight = weight; }
    private void setSex(String sex)                  { this.sex = sex; }
    private void setActivityLevel(int activityLevel) { this.activityLevel = activityLevel; }

    /*  Constructor */
    public User(String name, int age, float height, float weight, String sex, int activityLevel) {
        setName(name);
        setAge(age);
        setHeight(height);
        setWeight(weight);
        setSex(sex);
        setActivityLevel(activityLevel);
    }

    /*  Methods
        The first two are used to calculate Basal Metabolic Rate (BMR) used in calculating
        calories expended and Body Mass Index (BMI).
     */

    public float getBMR(){
        /*  Mifflin St-Jeor equation is used:

            P = (10 * mass in kg + 6.25 *height in cm - 5 * age in years + s) kcal/day
                where s is 5 for male, -161 for female.
         */
        return (10.0f * getWeight()) + (6.25f * getHeight()) - (5f * (float) getAge());
    }

    public float getBMI() {
        /*  BMI is weight in kg / height in metres * height in metres. Our height is in cm so
        *   needs to be divided by 100. */
        float conversion = 100f;

        return getWeight() / ((getHeight() / conversion) * (getHeight() / conversion));
    }
}
