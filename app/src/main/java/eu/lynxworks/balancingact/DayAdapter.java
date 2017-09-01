package eu.lynxworks.balancingact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class DayAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Day> dataSet;

    /*  The defined ViewHolder only requires the components that will be interacted
        with - this defines how the Object is adapted to each card displayed in the
        RecyclerView.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder{
        final TextView date;
        final TextView caloriesIn;
        final TextView caloriesOut;
        final TextView balance;

        public ViewHolder(View view) {
            super(view);
            date        = (TextView) view.findViewById(R.id.textCardDate);
            caloriesIn  = (TextView) view.findViewById(R.id.textCardFood);
            caloriesOut = (TextView) view.findViewById(R.id.textCardExercise);
            balance     = (TextView) view.findViewById(R.id.textCardBalance);
        }
    }

    public DayAdapter(Context context){
        /* TODO: Database connection for Day. */
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}