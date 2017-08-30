package eu.lynxworks.balancingact;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;


/**
 * This fragment is used to record food entries.
 */
public class FoodFragment extends Fragment{
    private String queryBarcode = null;
    private float queryWeight = 0;
    /* The number below is the weight in grams used to correct for portion size. */
    static final int units = 100;

    public FoodFragment() {
        // Required empty public constructor
    }

    public static FoodFragment instance() {
        return new FoodFragment();
    }

    @Override
    /*  The onCreateView callback is required to run a fragment - this one allows
        the fragment to inflate it's own XML layout. This is useful in this application
        as fragments can be switched in and out programatically, allowing the bottom
        navigation bar on the main activity to function. It is important to note that
        as this application doesn't target API below 11, there is no need to associate
        fragments with a parent!
     */
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View fragmentView = inflater.inflate(R.layout.fragment_food, container, false);
        /*  Event handlers for the various components */
        ImageButton searchButton = (ImageButton) fragmentView.findViewById(R.id.searchButton);
        ImageButton scanButton = (ImageButton) fragmentView.findViewById(R.id.scanButton);
        Button cancelButton = (Button) fragmentView.findViewById(R.id.cancelButton);
        Button saveButton = (Button) fragmentView.findViewById(R.id.saveButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search(view);
            }
        });
        scanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ScanActivity.class);
                startActivity(intent);
            }
        });
        return fragmentView;
    }

    @Override
    public void onStart(){
        super.onStart();
    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
    }

    @Override
    public void onDetach(){
        super.onDetach();
    }

    /*  The following functions conduct the bulk of the fragment's work.
        This function updates the display with the Food object passed to it.
     */
    private void updateDisplay(Food food) {
        if (food != null) {
            try {
                TextView txt_product = (TextView) getView().findViewById(R.id.txt_food_product);
                TextView txt_brand = (TextView) getView().findViewById(R.id.txt_food_brand);
                TextView txt_energy = (TextView) getView().findViewById(R.id.txt_100g_energy);
                TextView txt_salt = (TextView) getView().findViewById(R.id.txt_100g_salt);
                TextView txt_carb = (TextView) getView().findViewById(R.id.txt_100g_carbohydrate);
                TextView txt_protein = (TextView) getView().findViewById(R.id.txt_100g_protein);
                TextView txt_fat = (TextView) getView().findViewById(R.id.txt_100g_fat);
                TextView txt_fiber = (TextView) getView().findViewById(R.id.txt_100g_fiber);
                TextView txt_sugar = (TextView) getView().findViewById(R.id.txt_100g_sugar);
                txt_product.setText(food.getProductName());
                txt_brand.setText(food.getBrand());
                txt_energy.setText(Float.toString(food.getEnergy()));
                txt_salt.setText(Float.toString(food.getSalt()));
                txt_sugar.setText(Float.toString(food.getSugar()));
                txt_carb.setText(Float.toString(food.getCarbohydrate()));
                txt_protein.setText(Float.toString(food.getProtein()));
                txt_fat.setText(Float.toString(food.getFat()));
                txt_fiber.setText(Float.toString(food.getFibre()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (queryWeight != 0) {
                TextView txt_energy = (TextView) getView().findViewById(R.id.txt_energy);
                TextView txt_salt = (TextView) getView().findViewById(R.id.txt_salt);
                TextView txt_carb = (TextView) getView().findViewById(R.id.txt_carbohydrate);
                TextView txt_protein = (TextView) getView().findViewById(R.id.txt_protein);
                TextView txt_fat = (TextView) getView().findViewById(R.id.txt_fat);
                TextView txt_fiber = (TextView) getView().findViewById(R.id.txt_fiber);
                TextView txt_sugar = (TextView) getView().findViewById(R.id.txt_sugar);
                txt_energy.setText(Float.toString(food.getEnergy()*queryWeight/units));
                txt_salt.setText(Float.toString(food.getSalt()*queryWeight/units));
                txt_sugar.setText(Float.toString(food.getSugar()*queryWeight/units));
                txt_carb.setText(Float.toString(food.getCarbohydrate()*queryWeight/units));
                txt_protein.setText(Float.toString(food.getProtein()*queryWeight/units));
                txt_fat.setText(Float.toString(food.getFat()*queryWeight/units));
                txt_fiber.setText(Float.toString(food.getFibre()*queryWeight/units));
            }
        }
    }

    /*  This function clears the display
     */
    private void clearDisplay(){
        try{
            TextView txt_product = (TextView) getView().findViewById(R.id.txt_food_product);
            TextView txt_brand = (TextView) getView().findViewById(R.id.txt_food_brand);
            TextView txt_energy = (TextView) getView().findViewById(R.id.txt_100g_energy);
            TextView txt_salt = (TextView) getView().findViewById(R.id.txt_100g_salt);
            TextView txt_carb = (TextView) getView().findViewById(R.id.txt_100g_carbohydrate);
            TextView txt_protein = (TextView) getView().findViewById(R.id.txt_100g_protein);
            TextView txt_fat = (TextView) getView().findViewById(R.id.txt_100g_fat);
            TextView txt_fiber = (TextView) getView().findViewById(R.id.txt_100g_fiber);
            TextView txt_sugar = (TextView) getView().findViewById(R.id.txt_100g_sugar);
            txt_product.setText(R.string.blank);
            txt_brand.setText(R.string.blank);
            txt_energy.setText(R.string.blank);
            txt_salt.setText(R.string.blank);
            txt_sugar.setText(R.string.blank);
            txt_carb.setText(R.string.blank);
            txt_protein.setText(R.string.blank);
            txt_fat.setText(R.string.blank);
            txt_fiber.setText(R.string.blank);
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void updateBarcode() {
        EditText editText = (EditText) getView().findViewById(R.id.editBarcode);
        EditText weightText = (EditText) getView().findViewById(R.id.editWeight);
        if (editText != null) {
            queryBarcode = String.valueOf(editText.getText());
            if (weightText != null) {
                queryWeight = Float.valueOf(String.valueOf(weightText.getText()));
            }
        }
    }

    private void search(View view) {
        updateBarcode();
        new SearchBarcode().execute("");
    }

    /*  Android SDK does not permit running network tasks from the main thread. An internal class
        is used that extends AsyncTask - this has two methods (doInBackGround) which executes
        our JSON enquiry and (onPostExecute) which updates the display when the response is
        received.
     */
    private class SearchBarcode extends AsyncTask<String, Void, Food> {
        @Override
        protected Food doInBackground(String... params) {
            return searchBarcode();
        }

        @Override
        protected void onPostExecute(final Food food) {
            super.onPostExecute(food);
            updateDisplay(food);
            try {
                final Button saveButton = (Button) getView().findViewById(R.id.saveButton);
                final Button cancelButton = (Button) getView().findViewById(R.id.cancelButton);
                final EditText editWeight = (EditText) getView().findViewById(R.id.editWeight);
                saveButton.setVisibility(View.VISIBLE);
                saveButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*  A database manager is used to save the entry, then a snackbar gives
                            the user feedback. Finally the buttons are hidden. The event handlers
                            are not configured at the same time as the other controls as they
                            are not visible until this method is called.
                         */
                        FoodDatabaseManager dbManager = new FoodDatabaseManager(getActivity());
                        dbManager.addFood(food);
                        Snackbar saveSnackbar = Snackbar.make(view, R.string.snack_save_success, Snackbar.LENGTH_SHORT);
                        saveSnackbar.show();
                        saveButton.setVisibility(View.INVISIBLE);
                        cancelButton.setVisibility(View.INVISIBLE);
                        editWeight.setVisibility(View.INVISIBLE);
                    }
                });
                cancelButton.setVisibility(View.VISIBLE);
                cancelButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*  All that needs to happen on cancelling is to reset the display
                            and hide the buttons. A snackbar is used to give the user feedback.
                        */
                        clearDisplay();
                        saveButton.setVisibility(View.INVISIBLE);
                        cancelButton.setVisibility(View.INVISIBLE);
                        editWeight.setVisibility(View.INVISIBLE);
                        Snackbar snackbar = Snackbar.make(view, R.string.cancel, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }

        @Nullable
        private Food searchBarcode() {
            /*  We need to create connection and reader objects in the root of this method
                or they will be inaccessible to the "finally" block.
             */
            HttpsURLConnection anURLConnection = null;
            BufferedReader bufferedReader = null;

            /*  The JSON query response is returned as a String, which will require conversion
                to a Food object. We also need to describe the URL for the query - as a
                 RESTFUL API part of the URL changes when the method is called.
              */
            String jsonResponse;
            String queryPrefix = "https://world.openfoodfacts.org/api/v0/product/";
            String querySuffix = ".json";
            String jsonQuery = queryPrefix + queryBarcode + querySuffix;

            /*  The search requires us to:
                    (1) create a connection to the RESTFUL API
                    (2) read the response
                    (3) parse the response.
              */
            try {
                /*  (1) Create and open a connection using the HTTP GET method. If no connection,
                    we return null - design by contract.
                 */
                URL anUrl = new URL(jsonQuery);
                try {
                    anURLConnection = (HttpsURLConnection) anUrl.openConnection();
                    anURLConnection.setRequestMethod("GET");
                    anURLConnection.connect();
                } catch (ConnectException e) {
                    return (null);
                } catch (Exception e) {
                    return (null);
                }

                /*  (2) Read the response
                 */
                InputStream inputStream = anURLConnection.getInputStream();
                StringBuilder stringBuffer = new StringBuilder();
                bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    stringBuffer.append(line).append("\n");
                }
                jsonResponse = stringBuffer.toString();

                /* (3) Parse the response
                 */
                if (jsonResponse != null) {
                    try {
                        /*  The complete JSON response contains four JSON objects - we're
                            interested in one - "product". So we create a new JSONobject for all
                            the data then another for the "product" tag.
                         */
                        JSONObject everything = new JSONObject(jsonResponse);
                        JSONObject product = everything.getJSONObject("product");

                        long barcode = Long.parseLong(product.getString("id"));
                        String productName = product.getString("product_name");
                        /*  Bit of a fudge - the JSON returns a string where we really
                            need an iteger so we'll remove the " g".
                         */
                        String quantity = product.getString("quantity");
                        if (quantity.endsWith(" g")) {
                            quantity = product.getString("quantity").substring(0,
                                    product.getString("quantity").length() - 2);
                        }
                        String brand = product.getString("brands");

                        /*  Now we require a JSONobject to parse the contents of the nutrition
                            information...
                         */
                        JSONObject nutrition = product.getJSONObject("nutriments");

                        return new Food.Builder(
                                productName,
                                Float.valueOf(quantity),
                                Float.valueOf(nutrition.getString("energy_100g")))
                                .barcode(barcode)
                                .brand(brand)
                                .salt(Float.valueOf(nutrition.getString("salt_100g")))
                                .carbohydrate(Float.valueOf(nutrition.getString("carbohydrates_100g")))
                                .protein(Float.valueOf((nutrition.getString("proteins_100g"))))
                                .fat(Float.valueOf((nutrition.getString("saturated-fat_100g"))))
                                .fibre(Float.valueOf((nutrition.getString("fiber_100g"))))
                                .sugar(Float.valueOf((nutrition.getString("sugars_100g"))))
                                .build();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                return null;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (anURLConnection != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                EditText editBarcode = (EditText) getView().findViewById(R.id.editBarcode);
                editBarcode.setText(data.getStringExtra("barcodeKey"));
            }
        }
    }
}