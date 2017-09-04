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
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;


/**
 * This fragment is used to record home entries.
 */
public class HomeFragment extends Fragment {
    private Day today;

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
            usual method of catching exceptions to the Log with a tag. Clearly we don't want the
            database checking overhead every time the fragment, hence the extra condition.
         */
        if (today==null) {
            try {
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                String theDate = simpleDateFormat.format(new Date());
                DatabaseManager dbManager = new DatabaseManager(getContext());
                today = dbManager.getDayIfExists(theDate);
                if (today == null) {
                    today = new Day(new Date());
                    dbManager.saveDay(today);
                }
            } catch (Exception e) {
                Log.d("Dougie", "Exception in ", e);
            }
        }

        /*  The date is updated only when we know it! */
        SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        SimpleDateFormat longDateFormat = new SimpleDateFormat("EEEE dd MMMM yyyy", Locale.getDefault());
        try {
            Date aDate = shortDateFormat.parse(today.getTheDate());

            TextView dateText = (TextView) view.findViewById(R.id.textHomeDate);
            dateText.setText(longDateFormat.format(aDate.getTime()));
        } catch (Exception e) {
            Log.d("EXCEPTION", "Trying to create the long date by parsing the String date", e);
        }

        /*  When activating this fragment, we may have added food or exercise so need to
            update the lost of them in Day today.
         */
        today.update(getContext());

        return view;
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