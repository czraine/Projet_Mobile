package com.example.agencedevoyage.Activities;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.agencedevoyage.Database.RetrofitClient;
import com.example.agencedevoyage.Entity.ApiService;
import com.example.agencedevoyage.R;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnalyticsActivity extends AppCompatActivity {
    private PieChart totalActivitiesPieChart;
    private PieChart uniqueUsersDoughnutChart;
    private HorizontalBarChart commonActionsBarChart;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);
        SharedPreferences preferences = getSharedPreferences("user_session", MODE_PRIVATE);
        String userName = preferences.getString("user_name", "Guest"); // Default to "Guest" if no name is found

        apiService = RetrofitClient.getClient().create(ApiService.class);
        TextView welcomeTextView = findViewById(R.id.userWelcomeText); // The TextView with "Welcome Back"
        welcomeTextView.setText( "Welcome Back, "+userName);
        totalActivitiesPieChart = findViewById(R.id.totalActivitiesPieChart);
        uniqueUsersDoughnutChart = findViewById(R.id.uniqueUsersDoughnutChart);
        commonActionsBarChart = findViewById(R.id.commonActionsBarChart);

        fetchAnalyticsData();  // Call to fetch data
    }

    private void displayAnalytics(Map<String, Object> data) {
        // Total Activities Pie Chart
        int totalActivities = ((Number) data.get("total_activities")).intValue();
        populateTotalActivitiesPieChart(totalActivities);

        // Unique Users Doughnut Chart
        int uniqueUsers = ((Number) data.get("unique_users")).intValue();
        populateUniqueUsersDoughnutChart(uniqueUsers);

        // Most Common Actions Bar Chart
        List<?> commonActions = (List<?>) data.get("most_common_actions");
        populateCommonActionsBarChart(commonActions);
    }
    private void fetchAnalyticsData() {
        apiService.getAnalyticsSummary().enqueue(new Callback<Map<String, Object>>() {
            @Override
            public void onResponse(Call<Map<String, Object>> call, Response<Map<String, Object>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Map<String, Object> analyticsData = response.body();
                    displayAnalytics(analyticsData);  // Call to display the analytics data in charts
                } else {
                    Toast.makeText(AnalyticsActivity.this, "Failed to fetch analytics", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Map<String, Object>> call, Throwable t) {
                Log.e("NetworkError", "Error occurred", t);
                Toast.makeText(AnalyticsActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void populateTotalActivitiesPieChart(int totalActivities) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(totalActivities, "Total Activities"));

        PieDataSet dataSet = new PieDataSet(entries, "Activities");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        PieData pieData = new PieData(dataSet);

        totalActivitiesPieChart.setData(pieData);
        totalActivitiesPieChart.getDescription().setEnabled(false);
        totalActivitiesPieChart.invalidate(); // Refresh chart
    }

    private void populateUniqueUsersDoughnutChart(int uniqueUsers) {
        List<PieEntry> entries = new ArrayList<>();
        entries.add(new PieEntry(uniqueUsers, "Unique Users"));

        PieDataSet dataSet = new PieDataSet(entries, "Users");
        dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        dataSet.setSliceSpace(3f); // Space between slices
        dataSet.setSelectionShift(5f); // Shift when selected
        PieData pieData = new PieData(dataSet);

        uniqueUsersDoughnutChart.setData(pieData);
        uniqueUsersDoughnutChart.setDrawHoleEnabled(true);
        uniqueUsersDoughnutChart.setHoleRadius(40f); // Make it a doughnut chart
        uniqueUsersDoughnutChart.getDescription().setEnabled(false);
        uniqueUsersDoughnutChart.invalidate();
    }

    private void populateCommonActionsBarChart(List<?> commonActions) {
        List<BarEntry> entries = new ArrayList<>();
        List<String> labels = new ArrayList<>();

        for (int i = 0; i < commonActions.size(); i++) {
            List<?> action = (List<?>) commonActions.get(i);
            String actionName = (String) action.get(0);
            int actionCount = ((Number) action.get(1)).intValue();
            entries.add(new BarEntry(i, actionCount));
            labels.add(actionName);
        }

        BarDataSet dataSet = new BarDataSet(entries, "Actions");
        dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        BarData barData = new BarData(dataSet);

        commonActionsBarChart.setData(barData);
        commonActionsBarChart.getDescription().setEnabled(false);
        commonActionsBarChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        commonActionsBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        commonActionsBarChart.getXAxis().setGranularity(1f);
        commonActionsBarChart.getXAxis().setLabelCount(labels.size());
        commonActionsBarChart.invalidate();
    }
}