package eu.lynxworks.balancingact;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.util.Date;
import java.util.List;


/**
 * This fragment is used to record home entries.
 */
public class HomeFragment extends Fragment {
    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment instance() {
        return new HomeFragment();
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
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        /*  This fragment utilises a RecyclerView - an Android component that
            minimises resource usage by only allowing the number of components
            necessary to be drawn in the current ListView.
            1. Select a layout manager
            2. Select an adapter (the adapter pattern is used to bind
               different information).
         */
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_home);
        /*  If content doesn't get resized (which it doesn't in this app) then this
            optimisation improves performance.
         */
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        RecyclerView.Adapter adapter = new DayAdapter(getContext());
        recyclerView.setAdapter(adapter);

        /*  If this is the first time the application has been launched today then we create a
            new Day and save it to the database. Checking for today's entry confirms first use. As
            with any database activity, there is always a risk of an exception and we use the
            usual method of catching exceptions to the Log with a tag.
         */

//        TextView textView = (TextView) view.findViewById(R.id.homeText);
//        textView.setText(today.toString());

        final String testDate = "2017-09-03";

        EditText editText = (EditText) view.findViewById(R.id.editText);
        Button button = (Button) view.findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseManager dbManager = new DatabaseManager(getContext());
                List<Exercise> exercises = dbManager.getAllExercise();
                List<Exercise> exercisesOnDay = dbManager.getDayExercise(testDate);
                List<Food> foods = dbManager.getAllFood();
                List<Food> foodsOnDay = dbManager.getDayFood(testDate);
            }
        });

        return view;
    }

    private Day createDay() {
        Day day = null;
        try {
            DatabaseManager dbManager = new DatabaseManager(getContext());
            day = dbManager.getDay(new java.sql.Date((new Date()).getTime()));
            if(day==null){
                dbManager.saveDay(new Day(new java.sql.Date((new Date()).getTime())));
            }
        } catch (Exception e) {
            Log.d("Dougie", "Exception in ", e);
        }
        return day;
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