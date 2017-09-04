package eu.lynxworks.balancingact;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class DayAdapter extends RecyclerView.Adapter<DayAdapter.ViewHolder> {
    private List<Day> dataSet;

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
        Day day = dataSet.get(position);
        holder.date.setText(day.getTheDate());
        holder.caloriesIn.setText(String.valueOf(day.getCaloriesIn()));
        holder.caloriesOut.setText(String.valueOf(day.getCaloriesOut()));
        holder.balance.setText(String.valueOf(day.calorieBalance()));
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}