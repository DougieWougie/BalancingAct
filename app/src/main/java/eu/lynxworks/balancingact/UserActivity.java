package eu.lynxworks.balancingact;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class UserActivity extends AppCompatActivity {
    /*  Database might be utilised, however closing a database is an intensive action. For
        that reason, the close is carried out only when the fragment is destroyed - so needs
        to be opened on creation.
    */
    private DatabaseManager dbManager;
    private User theUser;

    private void setTheUser(User user){
        theUser = user;
    }
    private User getTheUser(){
        return theUser;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        dbManager = new DatabaseManager(getApplicationContext());

        try {
            Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        catch (Exception e){
            Log.d("Dougie", "setDisplayHomeUpAsEnabled threw an exception", e);
        }

        Button save = (Button) findViewById(R.id.userSaveButton);
        Button cancel = (Button) findViewById(R.id.userCancelButton);

        //  Uncommenting the following will drop the database, useful for diagnostics.
        //  dbManager.drop(); // Uncomment to drop the table - useful for diagnosing issues.

        /*  With a single user, if they're in the database we want to load their details. */
        if (!(dbManager.isUserEmpty())) {
            getUserFromDatabase();
            populateDisplay();
        }

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (populateFromDisplay()) {
                    try {
                        dbManager.updateUser(getTheUser());
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
        RadioGroup activityGroup = (RadioGroup) findViewById(R.id.radioActivity);
        RadioGroup sex = (RadioGroup) findViewById(R.id.radioUserSex);
        /*  Try catch is used because there are so many type conversions going on that
            it is extremely easy to build in difficult to trace bugs.
         */
        try {
            name.setText(getTheUser().getName());
            age.setText(String.format(Locale.getDefault(), "%d", getTheUser().getAge()));
            height.setText(String.format(Locale.getDefault(), "%.1f", getTheUser().getHeight()));
            weight.setText(String.format(Locale.getDefault(), "%.1f", getTheUser().getWeight()));
            if(Objects.equals(getTheUser().getSex(), "Male")){
                sex.check(R.id.radioUserMale);
            }
            else {
                sex.check(R.id.radioUserFemale);
            }
            switch(getTheUser().getActivityLevel()){
                case(1):
                    activityGroup.check(R.id.radioActivity1);
                    break;
                case(2):
                    activityGroup.check(R.id.radioActivity2);
                    break;
                case(3):
                    activityGroup.check(R.id.radioActivity3);
                    break;
                case(4):
                    activityGroup.check(R.id.radioActivity4);
                    break;
                default:
                    activityGroup.check(R.id.radioActivity1);
            }
        }
        catch (Exception e){
            Log.d("Dougie", "Exception in populateDisplay", e);
        }
    }

    /*  This method populates the display. */
    private boolean populateFromDisplay() {
        EditText name = (EditText) findViewById(R.id.editUserName);
        EditText age = (EditText) findViewById(R.id.editUserAge);
        EditText height = (EditText) findViewById(R.id.editUserHeight);
        EditText weight = (EditText) findViewById(R.id.editUserWeight);
        RadioGroup sexGroup = (RadioGroup) findViewById(R.id.radioUserSex);
        RadioButton sexRadio = (RadioButton) findViewById(sexGroup.getCheckedRadioButtonId());
        RadioGroup activityGroup = (RadioGroup) findViewById(R.id.radioActivity);
        RadioButton activityRadio = (RadioButton) findViewById(activityGroup.getCheckedRadioButtonId());

        String sex = (String) sexRadio.getText();
        int activityLevel = 1;

        if (name.getText().toString().isEmpty() ||
                age.getText().toString().isEmpty() ||
                height.getText().toString().isEmpty() ||
                weight.getText().toString().isEmpty()) {
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content),
                    R.string.fill_all_notification, Snackbar.LENGTH_SHORT);
            snackbar.show();
            return false;
        }

        switch(activityGroup.getCheckedRadioButtonId()){
            case(R.id.radioActivity1):
                activityLevel=1;
                break;
            case(R.id.radioActivity2):
                activityLevel=2;
                break;
            case(R.id.radioActivity3):
                activityLevel=3;
                break;
            case(R.id.radioActivity4):
                activityLevel=4;
                break;
        }
        setTheUser(new User(name.getText().toString(),
                            Integer.parseInt(age.getText().toString()),
                            Float.valueOf(height.getText().toString()),
                            Float.valueOf(weight.getText().toString()),
                            sex,
                            activityLevel));
        return true;
    }

    /*  This method checks if there is user data in the database. Android doesn't handle checking
        empty tables very gracefully so try catch is used to return null if the table is empty.
     */
    private void getUserFromDatabase() {
        User aUser;
        try{
            List<User> userList = dbManager.getAllUser();
            aUser = userList.get(0);
        }
        catch (Exception e){
            e.printStackTrace();
            aUser = null;
        }
        setTheUser(aUser);
    }

    /*  We only close the Database Manager on the Activity being destroyed as it is an intensive
        operation. This prove to be an essential optimisation!
     */
    @Override
    public void onDestroy() {
        dbManager.close();
        super.onDestroy();
    }
}