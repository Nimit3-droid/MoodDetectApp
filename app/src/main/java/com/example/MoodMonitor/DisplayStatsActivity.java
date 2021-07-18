package com.example.MoodMonitor;

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
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
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
    private TextView totalCalories;
    public float totalTimeTakenInDb=0.0f;
    public Map<String,Integer> activityTypeWithTime=new HashMap<>();
    private  double totalCal=0;
    DecimalFormat df = new DecimalFormat("#.##");



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


        if(time<3600){
            totalTime.setText("Total Time Doing These Activities : "+Float.toString(time) + " seconds");
        }else{
            totalTime.setText("Total Time Doing These Activities : "+df.format(time/3600) + " Hrs");
        }


    }
    public void totalCaloriesBurnt(Map<String,Integer> map){

        totalCalories= findViewById(R.id.totalCaloriesBurnt);
        if(map.containsKey("Jogging")){
            totalCal+=map.get("Jogging")*Constants.JOG;
        }
        if(map.containsKey("Walking")){
            totalCal+=map.get("Walking")*Constants.WALK;
        }
        if(map.containsKey("Upstairs")){
            totalCal+=map.get("Upstairs")*Constants.UPS;
        }
        if(map.containsKey("Downstairs")){
            totalCal+=map.get("Downstairs")*Constants.DOWNS;
        }
        if(map.containsKey("Standing")){
            totalCal+=map.get("Standing")*Constants.STAND;
        }
        if(map.containsKey("Sitting")){
            totalCal+=map.get("Sitting")*Constants.SIT;
        }
        if(totalCal>0){
            totalCalories.setText("Total Calories Burnt : "+df.format(totalCal));
        }else{
            totalCalories.setText("No Calories Burnt Till Now");
        }

    }

    public void displayTimeAndActivity(Map<String,Integer> map){
        jogging=findViewById(R.id.Jogging);
        walking=findViewById(R.id.Walking);
        upstairs=findViewById(R.id.Upstairs);
        downstairs=findViewById(R.id.Downstairs);
        standing=findViewById(R.id.Standing);
        sitting=findViewById(R.id.Sitting);
        if(map.containsKey("Jogging")){
            jogging.setText("Jogging  :  "+df.format(map.get("Jogging")*Constants.JOG) + " Kcal");
        }else{
            jogging.setText("Jogging  :  "+0 + " Kcal");
        }
        if(map.containsKey("Walking")){
            walking.setText("Walking  :  "+df.format(map.get("Walking")*Constants.WALK) + " Kcal");
        }else{
            walking.setText("walking  :  "+0 + " Kcal");
        }
        if(map.containsKey("Upstairs")){
            upstairs.setText("Upstairs  :  "+df.format(map.get("Upstairs")*Constants.UPS) + " Kcal");
        }else{
            upstairs.setText("upstairs  :  "+0 + " Kcal");
        }
        if(map.containsKey("Downstairs")){
            downstairs.setText("Downstairs  :  "+df.format(map.get("Downstairs")*Constants.DOWNS) + " Kcal");
        }else{
            downstairs.setText("downstairs  :  "+0 + " Kcal");
        }
        if(map.containsKey("Standing")){
            standing.setText("Standing  :  "+df.format(map.get("Standing")*Constants.STAND )+ " Kcal");
        }else{
            standing.setText("standing  :  "+0 + " Kcal");
        }
        if(map.containsKey("Sitting")){
            sitting.setText("Sitting  :  "+df.format(map.get("Sitting")*Constants.SIT) + " Kcal");
        }else{
            sitting.setText("Sitting  :  "+0 + " Kcal");
        }
    }
    void sortByValue(Map<String,Integer> map) {
        Map<Integer,String> sortedmap =new HashMap<>();
        List<String> mapKeys = new ArrayList<>(map.keySet());
        List<Integer> mapValues = new ArrayList<>(map.values());
        Collections.sort(mapValues);
        Collections.sort(mapKeys);
//        for(int i=0;i<map.size();i++){
//            mapValues.get(i)
//        }
    }
    public void displayMood(){
        //happy
//        Map<Integer, String> SortedCalo=new HashMap<>();
////        float max=Integer.MIN_VALUE;
////        float min=Integer.MAX_VALUE;
//        List<String> mapKeys = new ArrayList<>(map.keySet());
//        List<Integer> mapValues = new ArrayList<>(map.values());
//        Collections.sort(mapValues);
//        Collections.sort(mapKeys);
//        for(int i=0;i<map.size();i++){
////            SortedCalo.put();
//        }


//        float jog=map.get("Jogging");
//        float walk=map.get("Walking");
//        float ups=map.get("Upstairs");
//        float downs=map.get("Downstairs");
//        float stand=map.get("Standing");
//        float sit=map.get("Sitting");
        moodPredicted=findViewById(R.id.CurrentMood);
        String currMood="Feels Good";
        if(totalCal>2500){
            currMood="Exausted";

        }else if(2100<totalCal && totalCal <=2500){
            currMood ="Happy";
        }else if(1800<totalCal && totalCal <=2100){
            currMood="Sad";
        }else if(totalCal <=1800){
            currMood="Boredom";
        }else{
            currMood="Depressed";
        }
        if(totalCal>100){
            moodPredicted.setText("Mood Predicted Today : "+currMood);
        }else{
            moodPredicted.setText("Do some Physical Work to get your current mood");
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
            displayMood();
            totalCaloriesBurnt(activityTypeWithTime);
        }
    }
}
