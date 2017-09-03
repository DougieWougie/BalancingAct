package eu.lynxworks.balancingact;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;


/**
 * This fragment is used to record exercise entries.
 */
public class ExerciseFragment extends Fragment {
    private Exercise exercise;

    public ExerciseFragment() {
        // Required empty public constructor
    }

    public static ExerciseFragment instance() {
        return new ExerciseFragment();
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
        View fragmentView = inflater.inflate(R.layout.fragment_exercise, container, false);
        final Button cancel = (Button) fragmentView.findViewById(R.id.exerciseCancelButton);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearDisplay();
            }
        });
        final Button save = (Button) fragmentView.findViewById(R.id.exerciseSaveButton);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getExercise()) {
                    saveExercise();
                    save.setVisibility(View.INVISIBLE);
                    cancel.setVisibility(View.INVISIBLE);
                }
            }
        });
        return fragmentView;
    }

    private void clearDisplay() {
        try {
            EditText editExercise = (EditText) getView().findViewById(R.id.editExercise);
            EditText editDuration = (EditText) getView().findViewById(R.id.editDuration);
            EditText editCalories = (EditText) getView().findViewById(R.id.editCalories);
            editExercise.setText(R.string.blank);
            editDuration.setText(R.string.blank);
            editCalories.setText(R.string.blank);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    private boolean getExercise() {
        try {
            EditText editExercise = (EditText) getView().findViewById(R.id.editExercise);
            EditText editDuration = (EditText) getView().findViewById(R.id.editDuration);
            EditText editCalories = (EditText) getView().findViewById(R.id.editCalories);
            if (!editExercise.getText().toString().isEmpty() &&
                !editDuration.getText().toString().isEmpty() &&
                !editCalories.getText().toString().isEmpty() )
            {
                exercise = new Exercise(editExercise.getText().toString(),
                        Float.parseFloat(editDuration.getText().toString()),
                        Float.parseFloat(editCalories.getText().toString()));
                return true;
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void saveExercise() {
        /*  A database manager is used to save the entry, then a snackbar gives
            the user feedback.
         */
        try {
            DatabaseManager dbManager = new DatabaseManager(getActivity());
            dbManager.addExercise(exercise);
            Snackbar saveSnackbar = Snackbar.make(getView(), R.string.snack_save_success, Snackbar.LENGTH_SHORT);
            saveSnackbar.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}