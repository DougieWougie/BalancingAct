package eu.lynxworks.balancingact;

import android.content.Context;
import android.graphics.Color;
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

            date  = (TextView) view.findViewById(R.id.textCardDate);
            caloriesIn  = (TextView) view.findViewById(R.id.textCardFood);
            caloriesOut = (TextView) view.findViewById(R.id.textCardExercise);
            balance = (TextView) view.findViewById(R.id.textCardBalance);

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
            Global global = Global.getGlobalInstance();
            if(global.getUser()!=null) {
                user = global.getUser();
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Create new view
        Global global = Global.getGlobalInstance();
        this.user = global.getUser();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Day day = dataSet.get(position);
        try {
            /*  Make the date look a little better. */
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
        if(user==null){
            holder.balance.setText(R.string.add_user_for_bmr);
        }
        else {
            int calorieBalance = day.calorieBalance(user.getBMR());
            holder.balance.setText("Balance " + String.valueOf(calorieBalance));
            if(calorieBalance>=0){
                holder.balance.setTextColor(Color.GREEN);
            }
            if(calorieBalance<0){
                holder.balance.setTextColor(Color.RED);
            }
        }
    }

    @Override
    public int getItemCount() {
        return dataSet.size();
    }
}