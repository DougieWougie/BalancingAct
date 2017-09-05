package eu.lynxworks.balancingact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private List<Day> dataSet;
    private User user;

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView date, caloriesIn, caloriesOut, balance;

        public ViewHolder(View view){
            super(view);
            date   = (TextView) view.findViewById(R.id.textCardDate);
            caloriesIn  = (TextView) view.findViewById(R.id.textCardFood);
            caloriesOut     = (TextView) view.findViewById(R.id.textCardExercise);
            balance   = (TextView) view.findViewById(R.id.textCardBalance);

            view.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){

                }
            });
        }
    }

    public DayAdapter(Context context){
        try{
            DatabaseManager dbManager = new DatabaseManager(context.getApplicationContext());
            dataSet = dbManager.getAllDays();
//            user = dbManager.ge
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create new view
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        /*  Make the date look a little better. */
        Day day = dataSet.get(position);

        try {
            SimpleDateFormat shortDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            SimpleDateFormat longDateFormat = new SimpleDateFormat("EEEE dd MMM yyyy", Locale.getDefault());
            Date aDate = shortDateFormat.parse(day.getTheDate());
            String dateText = longDateFormat.format(aDate.getTime());
            holder.date.setText(dateText);
        } catch (Exception e) {
            Log.d("EXCEPTION", "Trying to create the long date by parsing the String date", e);
        }
        holder.caloriesIn.setText(String.valueOf(day.getCaloriesIn()));
        holder.caloriesOut.setText(String.valueOf(day.getCaloriesOut()));
        holder.balance.setText(String.valueOf(day.calorieBalance(1000)));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}