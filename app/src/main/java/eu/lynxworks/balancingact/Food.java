package eu.lynxworks.balancingact;

/**
 * This class defines a food.
 */

public class Food {
    // Attributes of a "food" item
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
        // Required parameters
        private final String productName;
        private final float quantity;
        private final float energy;

        // Optional parameters set to default values
        private long barcode            = 0;
        private String brand            = "";
        private float salt              = 0;
        private float carbohydrate      = 0;
        private float protein           = 0;
        private float fat               = 0;
        private float fibre             = 0;
        private float sugar             = 0;

        public Builder(String productName, float quantity, float energy){
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

    // Getter methods are required for populating the SQLite database.
    public long getBarcode()        { return barcode; }
    public String getProductName()  { return productName; }
    public float getQuantity()      { return quantity; }
    public String getBrand()        { return brand; }
    public float getSalt()          { return salt; }
    public float getEnergy()        { return energy; }
    public float getCarbohydrate()  { return carbohydrate; }
    public float getProtein()       { return protein; }
    public float getFat()           { return fat; }
    public float getFibre()         { return fibre; }
    public float getSugar()         { return sugar; }

    /*  Over riding toString is a technique advised in Effective Java 2nd edition
        by Joshua Bloch. The superclass returns a fairly unhelpful hashcode representing
        the class - this allows a more useful message. In this instance, the data.
     */
    @Override
    public String toString(){
        return getBarcode() + " / " +
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
