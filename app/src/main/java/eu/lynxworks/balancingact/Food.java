package eu.lynxworks.balancingact;


/**
 * This class defines a food.
 */

public class Food {
    /*  Attributes of a "food" item */
    private final String date;
    private final String barcode;
    private final String productName;
    private float quantity;
    private final String brand;
    private float salt;
    private float energy;
    private float carbohydrate;
    private float protein;
    private float fat;
    private float fibre;
    private float sugar;

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
        private String barcode          = "";
        private String brand            = "";
        private float salt              = 0;
        private float carbohydrate      = 0;
        private float protein           = 0;
        private float fat               = 0;
        private float fibre             = 0;
        private float sugar             = 0;

        public Builder(String date, String productName, float quantity, float energy){
            this.date                   = date;
            this.productName            = productName;
            this.quantity               = quantity;
            this.energy                 = energy;
        }

        public Builder barcode(String value)        { barcode = value;      return this;}
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

    public void updateQuantities(float quantity){
        float factor = quantity / this.quantity;
        this.salt = this.salt * factor;
        this.energy = this.energy * factor;
        this.carbohydrate = this.carbohydrate * factor;
        this.protein = this.protein * factor;
        this.fat = this.fat * factor;
        this.fibre = this.fibre * factor;
        this.sugar = this.sugar * factor;
        this.quantity = quantity;
    }

    /*  Getter methods are required for populating the SQLite database. */
    public String getDate()         { return this.date; }
    public String getBarcode()        { return this.barcode; }
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
