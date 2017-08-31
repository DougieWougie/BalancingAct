package eu.lynxworks.balancingact;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;

import java.util.List;

public class UserActivity extends AppCompatActivity {
    /*  Database might be utilised, however closing a database is an intensive action. For
        that reason, the close is carried out only when the fragment is destroyed - so needs
        to be opened on creation.
    */
    private UserDatabaseManager dbManager;
    private User user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        dbManager = new UserDatabaseManager(getApplicationContext());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Button save = (Button) findViewById(R.id.userSaveButton);
        Button cancel = (Button) findViewById(R.id.userCancelButton);

        // dbManager.drop(); // Uncomment to drop the table - useful for diagnosing issues.

        user = getUser();

        if (user != null) {
            populateDisplay();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (populateFromDisplay()) {
                    try {
                        dbManager.updateUser(user);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Snackbar snackbar = Snackbar.make(view, "Entry updated", Snackbar.LENGTH_SHORT);
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

    /*  This function responds to the user clicking the back button on the
        toolbar to go back up to the parent activity.
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*  This method populates the display. */
    private void populateDisplay() {
        EditText name = (EditText) findViewById(R.id.editUserName);
        EditText age = (EditText) findViewById(R.id.editUserAge);
        EditText height = (EditText) findViewById(R.id.editUserHeight);
        EditText weight = (EditText) findViewById(R.id.editUserWeight);
        RatingBar activity = (RatingBar) findViewById(R.id.ratingUserActivity);
        name.setText(user.getName());
        age.setText(user.getAge());
        height.setText(Float.toString(user.getHeight()));
        weight.setText(Float.toString(user.getWeight()));
        activity.setNumStars(user.getActivityLevel());
    }

    /*  This method populates the display. */
    private boolean populateFromDisplay() {
        EditText name = (EditText) findViewById(R.id.editUserName);
        EditText age = (EditText) findViewById(R.id.editUserAge);
        EditText height = (EditText) findViewById(R.id.editUserHeight);
        EditText weight = (EditText) findViewById(R.id.editUserWeight);
        RadioGroup sexGroup = (RadioGroup) findViewById(R.id.radioUserSex);
        RadioButton sexRadio = (RadioButton) findViewById(sexGroup.getCheckedRadioButtonId());
        RatingBar activity = (RatingBar) findViewById(R.id.ratingUserActivity);
        String sex = (String) sexRadio.getText();

        if (name.getText().toString().isEmpty() ||
                age.getText().toString().isEmpty() ||
                height.getText().toString().isEmpty() ||
                weight.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    R.string.fill_all_notification, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }
        user = new User(name.getText().toString(),
                Integer.parseInt(age.getText().toString()),
                Float.valueOf(height.getText().toString()),
                Float.valueOf(weight.getText().toString()),
                sex,
                activity.getNumStars());
        return true;
    }

    /*  This method checks if there is user data in the database. */
    private User getUser() {
        List<User> data = dbManager.getAll();
        return data.get(0);
    }

    @Override
    public void onDestroy() {
        dbManager.close();
        super.onDestroy();
    }
}