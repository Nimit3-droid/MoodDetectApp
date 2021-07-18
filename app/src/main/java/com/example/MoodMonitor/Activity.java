package com.example.MoodMonitor;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.graphics.Color;

@Entity(tableName = Constants.TABLE_NAME)
public class Activity {

    @PrimaryKey(autoGenerate = true)
    int uid;

    @ColumnInfo(name = Constants.COL1_NAME)
    long startTimestamp;

    @ColumnInfo(name = Constants.COL2_NAME)
    String activityType;

    @ColumnInfo(name = Constants.COL3_NAME)
    int duration;

    public Activity(long startTimestamp, String activityType, int duration) {
        this.startTimestamp = startTimestamp;
        this.activityType = activityType;
        this.duration = duration;
    }

    public int getUid() {
        return uid;
    }

//    public void setUid(int uid) {
//        this.uid = uid;
//    }

    public long getStartTimestamp() {
        return startTimestamp;
    }

//    public void setStartTimestamp(int startTimestamp) {
//        this.startTimestamp = startTimestamp;
//    }

    public String getActivityType() {
        return activityType;
    }

//    public void setActivityType(String activity) {
//        this.activityType = activity;
//    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public static class Constants {

        // MainActivity.java
        public static final int N_SAMPLES = 90;
        public static final float MEAN_X = 0.662868f;
        public static final float MEAN_Y = 7.255639f;
        public static final float MEAN_Z = 0.411062f;
        public static final float SD_X = 6.849058f;
        public static final float SD_Y = 6.746204f;
        public static final float SD_Z = 4.754109f;
        public static final String WALKING = "Walking";
        public static final String SITTING = "Sitting";
        public static final String JOGGING = "Jogging";
        public static final String STANDING = "Standing";
        public static final String UPSTAIRS = "Upstairs";
        public static final String DOWNSTAIRS = "Downstairs";

        // Activity.java
        public static final String TABLE_NAME = "activity";
        public static final String COL1_NAME = "start_timestamp";
        public static final String COL2_NAME = "activity_type";
        public static final String COL3_NAME = "duration";

        // AppDatabase.java
        public static final String DB_NAME = "activity_database";

        // DisplayStatsActivity.java
        public static final String EMPTY = "";
        public static final String OPTION_HOUR = "Past Hour";
        public static final String OPTION_DAY = "Past Day";
        public static final String OPTION_WEEK = "Past Week";
        public static final String OPTION_MONTH = "Past Month";
        public static final long MILLI_TO_SEC = 1000L;
        public static final int SEC_IN_HOUR = 3600;
        public static final int SEC_IN_DAY = 86400;
        public static final int SEC_IN_WEEK = 604800;
        public static final int SEC_IN_MONTH = 2592000;
        public static final int[] JOYFUL_COLORS = {
                Color.rgb(217, 80, 138), Color.rgb(254, 149, 7), Color.rgb(254, 247, 120),
                Color.rgb(106, 167, 134), Color.rgb(53, 194, 209), Color.rgb(42, 109, 130)};
        public static final int TO_PERCENT = 100;
        public static final int DP = 1;
        public static final float TEXT_SIZE = 10f;

        // RecognitionActivity.java
        public static final String LIB_NAME = "tensorflow_inference";
        public static final String MODEL_FILE = "file:///android_asset/har_classifier.pb";
        public static final String INPUT_NODE = "input";
        public static final String[] OUTPUT_NODES = {"output"};
        public static final String OUTPUT_NODE = "output";
        public static final long[] INPUT_SIZE = {1,1,90,3};
        public static final int OUTPUT_SIZE = 6;

        //moodsClass
        public static final String HAPPY="happy";
        public static final String SAD="sad";
        public static final String DEPRESSES="depressed";
        public static final String BOREDOM="boredom";
        public static final String EXHAUSTED="exhausted";

        //calories

        public static final double WALK = 0.0855;
        public static final double SIT= 0.0111;
        public static final double JOG = 0.1388;
        public static final double STAND = 0.0383;
        public static final double UPS = 0.1744;
        public static final double DOWNS = 0.1111;


    }
}