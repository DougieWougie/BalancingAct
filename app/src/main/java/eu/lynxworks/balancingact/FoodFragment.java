package eu.lynxworks.balancingact;


import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

import static android.app.Activity.RESULT_OK;
import static eu.lynxworks.balancingact.R.string.blank;


/**
 * This fragment is used to record food entries.
 */
public class FoodFragment extends Fragment {
    /*  Database might be utilised, however closing a database is an intensive action. For
        that reason, the close is carried out only when the fragment is destroyed - so needs
        to be opened on creation.
    */
    private DatabaseManager dbManager;

    private String queryBarcode = null;
    /* The number below is the weight in grams used to correct for portion size. */
    private static final int units = 100;

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
        dbManager = new DatabaseManager(getContext());

        /*  Event handlers for the various components */
        ImageButton searchButton = (ImageButton) fragmentView.findViewById(R.id.searchButton);
        ImageButton scanButton = (ImageButton) fragmentView.findViewById(R.id.scanButton);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search();
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
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
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
                txt_energy.setText(String.format(Locale.getDefault(), "%.1f", food.getEnergy()));
                txt_salt.setText(String.format(Locale.getDefault(), "%.1f", food.getSalt()));
                txt_sugar.setText(String.format(Locale.getDefault(), "%.1f", food.getSugar()));
                txt_carb.setText(String.format(Locale.getDefault(), "%.1f", food.getCarbohydrate()));
                txt_protein.setText(String.format(Locale.getDefault(), "%.1f", food.getProtein()));
                txt_fat.setText(String.format(Locale.getDefault(), "%.1f", food.getFat()));
                txt_fiber.setText(String.format(Locale.getDefault(), "%.1f", food.getFibre()));

            } catch (Exception e) {
                Log.d("EXCEPTION", "In FoodFragment->upDateDisplay()", e);
            }

            try {
                TextView txt_energy = (TextView) getView().findViewById(R.id.txt_energy);
                TextView txt_salt = (TextView) getView().findViewById(R.id.txt_salt);
                TextView txt_carb = (TextView) getView().findViewById(R.id.txt_carbohydrate);
                TextView txt_protein = (TextView) getView().findViewById(R.id.txt_protein);
                TextView txt_fat = (TextView) getView().findViewById(R.id.txt_fat);
                TextView txt_fiber = (TextView) getView().findViewById(R.id.txt_fiber);
                TextView txt_sugar = (TextView) getView().findViewById(R.id.txt_sugar);
                txt_energy.setText(String.format(Locale.getDefault(), "%.1f", food.getEnergy() * food.getQuantity() / units));
                txt_salt.setText(String.format(Locale.getDefault(), "%.1f", food.getSalt() * food.getQuantity() / units));
                txt_sugar.setText(String.format(Locale.getDefault(), "%.1f", food.getSugar() * food.getQuantity() / units));
                txt_carb.setText(String.format(Locale.getDefault(), "%.1f", food.getCarbohydrate() * food.getQuantity() / units));
                txt_protein.setText(String.format(Locale.getDefault(), "%.1f", food.getProtein() * food.getQuantity() / units));
                txt_fat.setText(String.format(Locale.getDefault(), "%.1f", food.getFat() * food.getQuantity() / units));
                txt_fiber.setText(String.format(Locale.getDefault(), "%.1f", food.getFibre() * food.getQuantity() / units));

            } catch (Exception e) {
                Log.d("EXCEPTION", "In FoodFragment->upDateDisplay()", e);
            }
        }
    }

    /*  This function clears the display
     */

    private void clearDisplay() {
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
            txt_product.setText(blank);
            txt_brand.setText(blank);
            txt_energy.setText(blank);
            txt_salt.setText(blank);
            txt_sugar.setText(blank);
            txt_carb.setText(blank);
            txt_protein.setText(blank);
            txt_fat.setText(blank);
            txt_fiber.setText(blank);
        } catch (Exception e) {
            Log.d("EXCEPTION", "In FoodFragment->clearDisplay()", e);
        }
        try {
            TextView txt_energy = (TextView) getView().findViewById(R.id.txt_energy);
            TextView txt_salt = (TextView) getView().findViewById(R.id.txt_salt);
            TextView txt_carb = (TextView) getView().findViewById(R.id.txt_carbohydrate);
            TextView txt_protein = (TextView) getView().findViewById(R.id.txt_protein);
            TextView txt_fat = (TextView) getView().findViewById(R.id.txt_fat);
            TextView txt_fiber = (TextView) getView().findViewById(R.id.txt_fiber);
            TextView txt_sugar = (TextView) getView().findViewById(R.id.txt_sugar);
            txt_energy.setText(R.string.blank);
            txt_salt.setText(R.string.blank);
            txt_sugar.setText(R.string.blank);
            txt_carb.setText(R.string.blank);
            txt_protein.setText(R.string.blank);
            txt_fat.setText(R.string.blank);
            txt_fiber.setText(R.string.blank);

        } catch (Exception e) {
            Log.d("EXCEPTION", "In FoodFragment->clearDisplay()", e);
        }
    }

    private void updateBarcode() {
        EditText editText = (EditText) getView().findViewById(R.id.editBarcode);
        if (editText != null) {
            queryBarcode = String.valueOf(editText.getText());
        }
    }

    private void search() {
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
            if(food==null){
                TextView notFound = (TextView) getView().findViewById(R.id.textNotFound);
                notFound.setVisibility(View.VISIBLE);
                notFound.setText(R.string.food_not_found);
                Button manualAdd = (Button) getView().findViewById(R.id.buttonManualAdd);
                manualAdd.setVisibility(View.VISIBLE);
                manualAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent editIntent = new Intent(getContext(), ManualFoodActivity.class);
                        startActivity(editIntent);
                    }
                });
            }
            else {
                updateDisplay(food);
                try {
                    final Button saveButton = (Button) getView().findViewById(R.id.foodSaveButton);
                    final Button cancelButton = (Button) getView().findViewById(R.id.foodCancelButton);
                    final TableLayout table = (TableLayout) getView().findViewById(R.id.tableLayout);
                    final EditText editQuantity = (EditText) getView().findViewById(R.id.editQuantity);
                    final TextView editLabel = (TextView) getView().findViewById(R.id.textLabelQuantity);
                    editQuantity.setText(String.valueOf(food.getQuantity()));
                    editQuantity.setVisibility(View.VISIBLE);
                    editLabel.setVisibility(View.VISIBLE);
                    table.setVisibility(View.VISIBLE);
                    saveButton.setVisibility(View.VISIBLE);
                    saveButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        /*  A database manager is used to save the entry, then a snackbar gives
                            the user feedback. Finally the buttons are hidden. The event handlers
                            are not configured at the same time as the other controls as they
                            are not visible until this method is called.
                         */
                            float quantity = Float.valueOf(String.valueOf(editQuantity.getText()));

                            if (quantity != food.getQuantity()) {
                                food.updateQuantities(quantity);
                            }

                            dbManager.addFood(food);
                            Snackbar saveSnackbar = Snackbar.make(view, R.string.snack_save_success, Snackbar.LENGTH_SHORT);
                            saveSnackbar.show();
                            saveButton.setVisibility(View.INVISIBLE);
                            cancelButton.setVisibility(View.INVISIBLE);
                            editQuantity.setVisibility(View.INVISIBLE);
                            editLabel.setVisibility(View.INVISIBLE);
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
                            table.setVisibility(View.INVISIBLE);
                            saveButton.setVisibility(View.INVISIBLE);
                            cancelButton.setVisibility(View.INVISIBLE);
                            Snackbar snackbar = Snackbar.make(view, R.string.cancelled, Snackbar.LENGTH_SHORT);
                            snackbar.show();
                        }
                    });
                } catch (Exception e) {
                    Log.d("EXCEPTION", "In FoodFragment->onPostExecute()", e);
                }
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
                /*  Create and open a connection using the HTTP GET method. If no connection,
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
                try {
                    InputStream inputStream = anURLConnection.getInputStream();
                    StringBuilder stringBuffer = new StringBuilder();
                    bufferedReader = new BufferedReader((new InputStreamReader(inputStream)));
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuffer.append(line).append("\n");
                    }
                    jsonResponse = stringBuffer.toString();
                }
                catch (Exception e){
                    jsonResponse = null;
                }

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

                        String barcode = product.getString("id");
                        String productName = product.getString("product_name");
                        /*  Bit of a fudge - the JSON returns a string where we really
                            need an integer so we'll remove the " g" - annoyingly some
                            products don't have the leading space.
                         */
                        String quantity = product.getString("quantity");
                        if (quantity.endsWith(" g")) {
                            quantity = product.getString("quantity").substring(0,
                                    product.getString("quantity").length() - 2);
                        }
                        if (quantity.endsWith("g")) {
                            quantity = product.getString("quantity").substring(0,
                                    product.getString("quantity").length() - 1);
                        }
                        String brand = product.getString("brands");

                        /*  Now we require a JSONobject to parse the contents of the nutrition
                            information...
                         */
                        JSONObject nutrition = product.getJSONObject("nutriments");

                        Date today = new Date();
                        SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
                        String todayString = yearMonthDay.format(today);

                        Food food = new Food.Builder(
                                todayString,
                                productName,
                                Float.valueOf(quantity),
                                Float.valueOf(nutrition.getString("energy_100g").trim()))
                                .barcode(barcode)
                                .brand(brand)
                                .salt(Float.valueOf(nutrition.getString("salt_100g")))
                                .carbohydrate(Float.valueOf(nutrition.getString("carbohydrates_100g")))
                                .protein(Float.valueOf((nutrition.getString("proteins_100g"))))
                                .fat(Float.valueOf((nutrition.getString("saturated-fat_100g"))))
                                .fibre(Float.valueOf((nutrition.getString("fiber_100g"))))
                                .sugar(Float.valueOf((nutrition.getString("sugars_100g"))))
                                .build();
                        return food;
                    } catch (JSONException e) {
                        Log.d("EXCEPTION", "Parsing the JSON response in searchBarcode()", e);
                    }
                }
                return null;
            } catch (IOException e) {
                Log.d("EXCEPTION", "IOException in searchBarcode()", e);
            } finally {
                if (anURLConnection != null) {
                    try {
                        bufferedReader.close();
                    } catch (IOException e) {
                        Log.d("EXCEPTION", "IOException in searchBarcode()", e);
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

    @Override
    public void onDestroy(){
        dbManager.close();
        super.onDestroy();
    }
}