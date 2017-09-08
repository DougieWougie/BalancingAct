package eu.lynxworks.balancingact;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManualFoodActivity extends AppCompatActivity {

    private Food food;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_food);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        /*  A null pointer exception is possible when setting HomeAsUpEnabled
            this condition checks for it.
         */
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        final Button save = (Button) findViewById(R.id.editFoodSave);
        final Button cancel = (Button) findViewById(R.id.editFoodCancel);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (populateFromDisplay()) {
                    try {
                        DatabaseManager dbMananager = new DatabaseManager(getApplicationContext());
                        dbMananager.addFood(food);
                        Snackbar snackbar = Snackbar.make(view, R.string.snack_save_success, Snackbar.LENGTH_SHORT);
                        snackbar.show();
                        save.setVisibility(View.INVISIBLE);
                        cancel.setVisibility(View.INVISIBLE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Snackbar snackbar = Snackbar.make(view, R.string.edit_food_requires, Snackbar.LENGTH_SHORT);
                    snackbar.show();
                }
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private boolean populateFromDisplay() {
        EditText name = (EditText) findViewById(R.id.editFoodName);
        EditText quantity = (EditText) findViewById(R.id.editFoodQuantity);
        EditText energy = (EditText) findViewById(R.id.editFoodEnergy);

        if (name.getText().toString().equals("") ||
                quantity.getText().toString().equals("") ||
                energy.getText().toString().equals("")) {
            return false;
        } else {
            Date today = new Date();
            SimpleDateFormat yearMonthDay = new SimpleDateFormat("yyyy-MM-dd");
            String todayString = yearMonthDay.format(today);

            String productName = String.valueOf(name.getText());
            String calories = String.valueOf(energy.getText());
            String amount = String.valueOf(quantity.getText());

            Food food = new Food.Builder(
                    todayString,
                    productName,
                    Float.valueOf(amount),
                    Float.valueOf(calories))
                    .build();

            this.food = food;
            return true;
        }
    }
}
