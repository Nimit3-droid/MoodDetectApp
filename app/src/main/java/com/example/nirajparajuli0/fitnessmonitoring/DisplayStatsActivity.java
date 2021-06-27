package com.example.nirajparajuli0.fitnessmonitoring;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.lang.ref.WeakReference;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayStatsActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    private AppDatabase activityDB;
    private String[] items = new String[] {Constants.OPTION_HOUR, Constants.OPTION_DAY, Constants.OPTION_WEEK, Constants.OPTION_MONTH};
    private String [] xVal = new String[] {Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY, Constants.EMPTY};
    private int [] TimeAndActivity = new int[6];
    private String [] ActivityName = new String[6];
    private TextView totalTime;
    private TextView jogging;
    private TextView walking;
    private TextView upstairs;
    private TextView downstairs;
    private TextView standing;
    private TextView sitting;
    private TextView moodPredicted;
    public float totalTimeTakenInDb=0.0f;
    public Map<String,Integer> activityTypeWithTime=new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
//        final Spinner dropdown = findViewById(R.id.spinner1);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
//        dropdown.setAdapter(adapter);
//        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
//                String choice = dropdown.getSelectedItem().toString();
                long initialTimestamp = getInitialTimestamp(Constants.OPTION_WEEK);
                getStats(initialTimestamp);

//                Log.d("Spinner", choice);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parentView) {
//
//            }
//        });
    }
    public void getTotalTime(float time){
        totalTime= findViewById(R.id.totalActivityTime);
        moodPredicted=findViewById(R.id.CurrentMood);
        if(time>180*60){
            moodPredicted.setText("Mood Predicted - Happy :) ");
        }else if(time<=180*60 && time >=120*60){
            moodPredicted.setText("Mood Predicted - Normal :| ");
        }else if(time<120*60){
            moodPredicted.setText("Mood Predicted - Sad :( ");
        }else{
            moodPredicted.setText("");
        }

        totalTime.setText("Total Time : "+Float.toString(time) + " seconds | " + Float.toString(time/3600) +" hrs");

    }

    public void displayTimeAndActivity(Map<String,Integer> map){
        jogging=findViewById(R.id.Jogging);
        walking=findViewById(R.id.Walking);
        upstairs=findViewById(R.id.Upstairs);
        downstairs=findViewById(R.id.Downstairs);
        standing=findViewById(R.id.Standing);
        sitting=findViewById(R.id.Sitting);
        if(map.containsKey("Jogging")){
            jogging.setText("Jogging  :  "+map.get("Jogging") + " Seconds");
        }else{
            jogging.setText("Jogging  :  "+0 + " Seconds");
        }
        if(map.containsKey("Walking")){
            walking.setText("Walking  :  "+map.get("Walking") + " Seconds");
        }else{
            walking.setText("walking  :  "+0 + " Seconds");
        }
        if(map.containsKey("Upstairs")){
            upstairs.setText("Upstairs  :  "+map.get("Upstairs") + " Seconds");
        }else{
            upstairs.setText("upstairs  :  "+0 + " Seconds");
        }
        if(map.containsKey("Downstairs")){
            downstairs.setText("Downstairs  :  "+map.get("Downstairs") + " Seconds");
        }else{
            downstairs.setText("downstairs  :  "+0 + " Seconds");
        }
        if(map.containsKey("Standing")){
            standing.setText("Standing  :  "+map.get("Standing") + " Seconds");
        }else{
            standing.setText("standing  :  "+0 + " Seconds");
        }
        if(map.containsKey("Sitting")){
            sitting.setText("Sitting  :  "+map.get("Sitting") + " Seconds");
        }else{
            sitting.setText("Sitting  :  "+0 + " Seconds");
        }
    }
    private long getInitialTimestamp(String option) {
        long currentTime = System.currentTimeMillis() / Constants.MILLI_TO_SEC;
        long initialTime;

        switch (option) {
            case Constants.OPTION_HOUR:
                initialTime = currentTime - Constants.SEC_IN_HOUR;
                break;
            case Constants.OPTION_DAY:
                initialTime = currentTime - Constants.SEC_IN_DAY;
                break;
            case Constants.OPTION_WEEK:
                initialTime = currentTime - Constants.SEC_IN_WEEK;
                break;
            case Constants.OPTION_MONTH:
                initialTime = currentTime - Constants.SEC_IN_MONTH;
                break;
            default:
                initialTime = currentTime - Constants.SEC_IN_DAY;
                break;
        }
        return initialTime;
    }

    private void getStats(long time) {
        activityDB = AppDatabase.getAppDatabase(DisplayStatsActivity.this);
        new retrieveStats(this, time).execute();
    }

    private void drawPieChart(List<Activity> activities) {
//        PieChart pieChart = (PieChart) findViewById(R.id.piechart);
//        pieChart.setUsePercentValues(true);
        ArrayList<Entry> yvalues = new ArrayList<Entry>();
        float total = 0.0f;

        for (int i=0; i<activities.size(); i++) {
            Log.d("size", String.valueOf(activities.size()));
            total += activities.get(i).duration;
            activityTypeWithTime.put(activities.get(i).activityType,activities.get(i).duration);
            Log.d("activityTypeWithTime", "Pulled from DB " + activities.get(i).activityType +"    "+ activityTypeWithTime.get(activities.get(i).activityType));
        }
        totalTimeTakenInDb=total;




        Log.d("Total", String.valueOf(total));
        for (int i=0; i < activities.size(); i++) {
            Activity activity = activities.get(i);
            float percentage = round(activity.duration/total*Constants.TO_PERCENT, Constants.DP);
            yvalues.add(new Entry(percentage, i));
            xVal[i] = activity.activityType;
        }

//        for (int i=0; i < 3; i++) {
//            Activity activity = activities.get(i);
//            TimeAndActivity[i]=activity.duration;
//            ActivityName[i]=activity.activityType;
//            Log.d("TAG", TimeAndActivity[i] +  ActivityName[i]);
//        }

//        PieDataSet dataSet = new PieDataSet(yvalues, Constants.EMPTY);
//
//        PieData data = new PieData(xVal, dataSet);
//        data.setValueTextSize(Constants.TEXT_SIZE);
//        data.setValueFormatter(new PercentFormatter());
//        dataSet.setColors(Constants.JOYFUL_COLORS);
//        pieChart.setDescription(Constants.EMPTY);
//
//        Legend legend = pieChart.getLegend();
//        legend.setPosition(Legend.LegendPosition.ABOVE_CHART_CENTER);
//
//        pieChart.setData(data);
//        pieChart.invalidate();
//        pieChart.setOnChartValueSelectedListener(this);

    }

    public static float round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    @Override
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;

        Toast.makeText(DisplayStatsActivity.this,
                xVal[e.getXIndex()] + ": " + e.getVal() + "%", Toast.LENGTH_SHORT).show();
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    private class retrieveStats extends AsyncTask<Void,Void,List<Activity>> {
        private WeakReference<DisplayStatsActivity> activityReference;
        long startTime;

        retrieveStats(DisplayStatsActivity context, long time) {
            activityReference = new WeakReference<>(context);
            this.startTime = time;
        }

        @Override protected List<Activity> doInBackground(Void... voids) {
            return activityReference.get().activityDB.activityDao().getProportion(startTime);
        }

        @Override protected void onPostExecute(List<Activity> activities) {
            Activity first = activities.get(0);
            String time = Integer.toString(first.getDuration());
            String activity_type = first.getActivityType();
            Log.d("actttt", "Pulled from DB " + activity_type +"    "+ time);
            drawPieChart(activities);
            getTotalTime(totalTimeTakenInDb);
            displayTimeAndActivity(activityTypeWithTime);
        }
    }
}
