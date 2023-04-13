package com.example.databaseappex;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


        TextView nameEdit;
        Spinner citySpinner;
        Spinner spinner;
        Button addButton;
        Button getButton;
        Button Dbutton;
        TextView resultView;
        RecyclerView Rview;

        DatabaseControl control;


        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate (Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nameEdit = findViewById(R.id.nameEdit);
        citySpinner= findViewById(R.id.citySpinner);
        spinner = findViewById(R.id.spinner);
        addButton = findViewById(R.id.addButton);
        getButton = findViewById(R.id.getButton);
        Dbutton = findViewById(R.id.Dbutton);
        resultView = findViewById(R.id.resultView);
        Rview = findViewById(R.id.Rview);

        control = new DatabaseControl(this);

        //Get Button
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                if(name.equals("")){
                    //resultView.setText("Failed Enter Name");
                    Toast.makeText(getApplicationContext(), "Failed: Enter Name", Toast.LENGTH_SHORT).show();
                }else {
                    control.open();
                    String sResult = control.getState(name);
                    String cResult = control.getCity(name);
                    control.close();
                    resultView.setText(cResult + ", " + sResult);
                }

            }
        });

        //Add Button
        // In activity main asks for name, city, state then opens database to store then closes
        //Gives message if it was added or not
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                if(name.equals("")){
                    Toast.makeText(getApplicationContext(), "Failed: Enter Name", Toast.LENGTH_SHORT).show();
                }else {

                    String city = ((TextView) citySpinner.getSelectedView()).getText().toString();
                    String state = ((TextView) spinner.getSelectedView()).getText().toString();
                    control.open();
                    boolean itWorked = control.insert(name, city, state);
                    control.close();
                    if (itWorked) {
                        Toast.makeText(getApplicationContext(), "Added " + name + " " + city + " " + state, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), "FAILED " + name + " " + city + " " + state, Toast.LENGTH_SHORT).show();
                        onResume();
                    }
                }
            }
        });

        //Delete Button
        // Provide name in activity main and will delete all entries in database corresponding with name
        Dbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String teamName = nameEdit.getText().toString();
                if(teamName.equals("")){
                    Toast.makeText(getApplicationContext(), "Failed: Enter Name", Toast.LENGTH_SHORT).show();
                }else {
                    control.open();
                    control.delete(teamName);
                    control.close();
                    onResume();
                }
            }
        });

        //State Spinner Resource
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.states, android.R.layout.simple_spinner_item); // ^All part of same line
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        //City Spinner Resource
        ArrayAdapter<CharSequence> Cadapter = ArrayAdapter.createFromResource(this,
                R.array.city, android.R.layout.simple_spinner_item);
        Cadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        citySpinner.setAdapter(Cadapter);

        }



    @Override
    protected void onResume() {
        super.onResume();
        Rview.setLayoutManager(new LinearLayoutManager(this));
        control.open();
        String[] dataset = control.getAllNamesArray();
        control.close();
        CustomAdapter adapter = new CustomAdapter(dataset);
        adapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomAdapter.ViewHolder viewHolder = (CustomAdapter.ViewHolder) view.getTag();
                TextView textView = viewHolder.getTextView();
                resultView.setText(textView.getText().toString());
                control.open();
                String city = control.getCity(textView.getText().toString());
                control.close();
                resultView.setText(textView.getText().toString()+ " "+city);
            }
        });
        Rview.setAdapter(adapter);
    }
}
