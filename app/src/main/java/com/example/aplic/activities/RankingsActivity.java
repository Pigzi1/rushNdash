package com.example.aplic.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aplic.R;
import com.example.aplic.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class RankingsActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ValueEventListener {

    private DatabaseReference userRef;
    private List<User> userList;
    private TableRow scoreHeader, deathHeader, itemHeader;
    private List<String[]> deathRows, scoreRows, itemRows;
    private TableLayout tableLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        userRef = FirebaseDatabase.getInstance().getReference("Users");
        userRef.addValueEventListener(this);
        userList = new ArrayList<>();
        scoreRows = new ArrayList<>();
        deathRows = new ArrayList<>();
        itemRows = new ArrayList<>();
        scoreHeader = new TableRow(this);
        deathHeader = new TableRow(this);
        itemHeader = new TableRow(this);

        ImageButton ibBack = findViewById(R.id.ibBack);
        ibBack.setOnClickListener(view -> finish());
        setSpinner();
        tableLayout = findViewById(R.id.tableLayout);

        setHeader();
    }
    // creates the spinner
    public void setSpinner() {
        Spinner spinner = findViewById(R.id.spinner);
        // list for different types of ranking
        List<String> rankingTypes = new ArrayList<>();
        rankingTypes.add(" (none)");
        rankingTypes.add(" Total Score:");
        rankingTypes.add(" Total Deaths:");
        rankingTypes.add(" Total Items Owned:");

        ArrayAdapter<String> rankingTypesAdapter = new ArrayAdapter<>(this, R.layout.spinner_selected_item, rankingTypes);
        rankingTypesAdapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinner.setAdapter(rankingTypesAdapter);
        spinner.setOnItemSelectedListener(this);
    }
    // creates for each ranking type it's header
    public void setHeader() {
        scoreHeader.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        String[] scoreHeaderText = {"Rank", "Username", "Score"};
        for (String header: scoreHeaderText) {
            TextView headerView = new TextView(this);
            headerView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            headerView.setText(header);
            headerView.setTextColor(getColor(R.color.white));
            headerView.setTypeface(ResourcesCompat.getFont(this, R.font.berlin_sans_fb_demi_bold));
            headerView.setPadding(20, 20, 20, 50);
            scoreHeader.setBackgroundColor(getColor(R.color.black));
            scoreHeader.addView(headerView);
        }
        deathHeader.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        String[] deathHeaderText = {"Rank", "Username", "Deaths"};
        for (String header: deathHeaderText) {
            TextView headerView = new TextView(this);
            headerView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            headerView.setText(header);
            headerView.setTextColor(getColor(R.color.white));
            headerView.setTypeface(ResourcesCompat.getFont(this, R.font.berlin_sans_fb_demi_bold));
            headerView.setPadding(20, 20, 20, 50);
            deathHeader.setBackgroundColor(getColor(R.color.black));
            deathHeader.addView(headerView);
        }
        itemHeader.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.MATCH_PARENT));
        String[] itemHeaderText = {"Rank", "Username", "Items"};
        for (String header: itemHeaderText) {
            TextView headerView = new TextView(this);
            headerView.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT));
            headerView.setText(header);
            headerView.setTextColor(getColor(R.color.white));
            headerView.setTypeface(ResourcesCompat.getFont(this, R.font.berlin_sans_fb_demi_bold));
            headerView.setPadding(20, 20, 20, 50);
            itemHeader.setBackgroundColor(getColor(R.color.black));
            itemHeader.addView(headerView);
        }
    }
    // sums up the user's score
    public int scoreSum(User tempUser) {
        List<Integer> userScoreList = tempUser.hsl;
        int sum = 0;
        for (int number : userScoreList) {
            sum += number;
        }
        return sum;
    }
    // creates rankings table for each type
    public void setLists() {
        List<User> scoreList = userList;
        scoreList.sort(Comparator.comparingInt(this::scoreSum));
        for (int i = scoreList.size() - 1; i >= Math.max(scoreList.size() - 10, 0); i--) {
            scoreRows.add(new String[]{Integer.toString(scoreList.size() - i), scoreList.get(i).userName, String.valueOf(scoreSum(scoreList.get(i)))});
        }

        List<User> deathList = userList;
        deathList.sort(Comparator.comparingInt(u -> u.deaths));
        for (int i = deathList.size() - 1; i >= Math.max(deathList.size() - 10, 0); i--) {
            deathRows.add(new String[]{Integer.toString(deathList.size() - i), deathList.get(i).userName, String.valueOf(deathList.get(i).deaths)});
        }

        List<User> itemList = userList;
        itemList.sort(Comparator.comparingInt(u -> u.itemsOwned));
        for (int i = itemList.size() - 1; i >= Math.max(itemList.size() - 10, 0); i--) {
            itemRows.add(new String[]{Integer.toString(itemList.size() - i), itemList.get(i).userName, String.valueOf(itemList.get(i).itemsOwned)});
        }
    }
    // every time an option is chosen the table changes according to the option chosen
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        String item = adapterView.getItemAtPosition(i).toString();
        tableLayout.removeAllViews();
        switch (item) {
            case " Total Score:":
                tableLayout.addView(scoreHeader);

                for (String[] row : scoreRows) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    for (String value : row) {
                        TextView valueView = new TextView(this);
                        valueView.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        valueView.setText(value);
                        valueView.setTextColor(getColor(R.color.white));
                        valueView.setTypeface(ResourcesCompat.getFont(this, R.font.berlin_sans_fb_demi_bold));
                        valueView.setPadding(20, 10, 150, 20);
                        tableRow.setBackgroundColor(getColor(R.color.black));
                        tableRow.addView(valueView);
                    }
                    tableLayout.addView(tableRow);
                }
                break;
            case " Total Deaths:":
                tableLayout.addView(deathHeader);

                for (String[] row : deathRows) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    for (String value : row) {
                        TextView valueView = new TextView(this);
                        valueView.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        valueView.setText(value);
                        valueView.setTextColor(getColor(R.color.white));
                        valueView.setTypeface(ResourcesCompat.getFont(this, R.font.berlin_sans_fb_demi_bold));
                        valueView.setPadding(20, 10, 150, 20);
                        tableRow.setBackgroundColor(getColor(R.color.black));
                        tableRow.addView(valueView);
                    }
                    tableLayout.addView(tableRow);
                }
                break;
            case " Total Items Owned:":
                tableLayout.addView(itemHeader);

                for (String[] row : itemRows) {
                    TableRow tableRow = new TableRow(this);
                    tableRow.setLayoutParams(new TableRow.LayoutParams(
                            TableRow.LayoutParams.MATCH_PARENT,
                            TableRow.LayoutParams.WRAP_CONTENT));
                    for (String value : row) {
                        TextView valueView = new TextView(this);
                        valueView.setLayoutParams(new TableRow.LayoutParams(
                                TableRow.LayoutParams.WRAP_CONTENT,
                                TableRow.LayoutParams.WRAP_CONTENT));
                        valueView.setText(value);
                        valueView.setTextColor(getColor(R.color.white));
                        valueView.setTypeface(ResourcesCompat.getFont(this, R.font.berlin_sans_fb_demi_bold));
                        valueView.setPadding(20, 10, 150, 20);
                        tableRow.setBackgroundColor(getColor(R.color.black));
                        tableRow.addView(valueView);
                    }
                    tableLayout.addView(tableRow);
                }
                break;
        }
    }
    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
    }
    // creates a list of all users
    @Override
    public void onDataChange(@NonNull DataSnapshot snapshot) {
        Log.i("TAG", "onDataChange: ");
        userRef.removeEventListener(this);
        for (DataSnapshot data : snapshot.getChildren()) {
            User tempUser = data.getValue(User.class);
            userList.add(tempUser);
        }
        setLists();
        Log.i("TAG", "onDataChange: " + userList);
    }
    @Override
    public void onCancelled(@NonNull DatabaseError error) {
    }
}