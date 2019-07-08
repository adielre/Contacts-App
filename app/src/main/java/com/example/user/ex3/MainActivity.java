package com.example.user.ex3;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    //fields
    private EditText insertedName,insertedPhone;
    private Button searchButton,insertButton;
    private ListView listView;
    private SQLiteDB db;

    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //connect the fields to the editTexts/buttons
        insertedName = findViewById(R.id.nameTxt);
        insertedPhone = findViewById(R.id.phoneTxt);
        searchButton = findViewById(R.id.searchBtn);
        searchButton.setOnClickListener(this);
        insertButton= findViewById(R.id.insertBtn);
        insertButton.setOnClickListener(this);
        listView = findViewById(R.id.listView);

        //initialize the sql db
        db = new SQLiteDB(this);

        //make the adapter and connect it to the view
        ListAdapter adapter = db.updateList();

        listView.setAdapter(adapter);

        //listener to the dial button
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                TextView phone =view.findViewById(R.id.phoneLine);
                if((phone.getText().toString()).equals("")) //check if there is a number
                {
                    Toast.makeText(getApplicationContext(),"The selected contact doesn't have a Phone Number",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //copy the number to the dial in the phone
                    Intent dial = new Intent(Intent.ACTION_DIAL);
                    dial.setData(Uri.parse("tel:"+phone.getText().toString()));
                    startActivity(dial);
                }
            }
        });
    }

    public void onClick(View v)
    {
        ListAdapter adapter = null;
        if(v.getId()==R.id.insertBtn)   //if we press the insert button
        {
            adapter=db.add(insertedName.getText().toString(),insertedPhone.getText().toString());
            //delete the Strings in the name and phone text
            insertedName.setText("");
            insertedPhone.setText("");
        }
        else if(v.getId()==R.id.searchBtn) //if we press the search button
            adapter=db.search(insertedName.getText().toString(),insertedPhone.getText().toString());
        if(adapter==null)
            adapter=db.updateList();;
        listView.setAdapter(adapter);
    }

    protected void onDestroy()
    {
        db.destroy();
        super.onDestroy();
    }

}// end MainActivity class
