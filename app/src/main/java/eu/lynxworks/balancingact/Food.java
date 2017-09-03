package eu.lynxworks.balancingact;

import android.icu.text.SimpleDateFormat;

import java.util.Date;

/**
 * This class defines a food.
 */

public class Food {
    /*  Attributes of a "food" item */
    private final String date;
    private final long barcode;
    private final String productName;
    private final float quantity;
    private final String brand;
    private final float salt;
    private final float energy;
    private final float carbohydrate;
    private final float protein;
    private final float fat;
    private final float fibre;
    private final float sugar;

    /*  Because not all of these are always known, in order to avoid the telescoping
        constructor problem, a "builder" design pattern is used.
     */
    public static class Builder {
        /*  Required parameters */
        private final String date;
        private final String productName;
        private final float quantity;
        private final float energy;

        /*  Optional parameters set to default values */
        private long barcode            = 0;
        private String brand            = "";
        private float salt              = 0;
        private float carbohydrate      = 0;
        private float protein           = 0;
        private float fat               = 0;
        private float fibre             = 0;
        private float sugar             = 0;

        public Builder(Date date, String productName, float quantity, float energy){
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            simpleDateFormat.format(date);
            this.date                   = simpleDateFormat.toString();
            this.productName            = productName;
            this.quantity               = quantity;
            this.energy                 = energy;
        }

        public Builder barcode(long value)          { barcode = value;      return this;}
        public Builder brand(String value)          { brand = value;        return this;}
        public Builder salt(float value)            { salt = value;         return this;}
        public Builder carbohydrate(float value)    { carbohydrate = value; return this;}
        public Builder protein(float value)         { protein = value;      return this;}
        public Builder fat(float value)             { fat = value;          return this;}
        public Builder fibre(float value)           { fibre = value;        return this;}
        public Builder sugar(float value)           { sugar = value;        return this;}

        public Food build() {
            return new Food(this);
        }
    }

    private Food(Builder builder) {
        this.date            = builder.date;
        this.barcode         = builder.barcode;
        this.productName     = builder.productName;
        this.quantity        = builder.quantity;
        this.brand           = builder.brand;
        this.salt            = builder.salt;
        this.energy          = builder.energy;
        this.carbohydrate    = builder.carbohydrate;
        this.protein         = builder.protein;
        this.fat             = builder.fat;
        this.fibre           = builder.fibre;
        this.sugar           = builder.sugar;
    }

    /*  Getter methods are required for populating the SQLite database. */
    public String getDate()         { return this.date; }
    public long getBarcode()        { return this.barcode; }
    public String getProductName()  { return this.productName; }
    public float getQuantity()      { return this.quantity; }
    public String getBrand()        { return this.brand; }
    public float getSalt()          { return this.salt; }
    public float getEnergy()        { return this.energy; }
    public float getCarbohydrate()  { return this.carbohydrate; }
    public float getProtein()       { return this.protein; }
    public float getFat()           { return this.fat; }
    public float getFibre()         { return this.fibre; }
    public float getSugar()         { return this.sugar; }

    /*  Over riding toString is a technique advised in Effective Java 2nd edition
        by Joshua Bloch. The superclass returns a fairly unhelpful hashcode representing
        the class - this allows a more useful message. In this instance, the data.
     */
    @Override
    public String toString(){
        return  getDate() + " / " +
                getBarcode() + " / " +
                getProductName() + " / " +
                getQuantity() + " / " +
                getBrand() + " / " +
                getSalt() + " / " +
                getEnergy() + " / " +
                getCarbohydrate() + " / " +
                getProtein() + " / " +
                getFat() + " / " +
                getFibre() + " / " +
                getSugar();
    }
}
