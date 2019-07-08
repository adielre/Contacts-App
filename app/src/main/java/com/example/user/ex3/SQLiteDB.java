package com.example.user.ex3;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class SQLiteDB
{
    //fields
    private SQLiteDatabase db=null;
    private Context context;
    private boolean firstTime=true;

    //constructor
    public SQLiteDB(Context context)
    {
        this.context = context;
        try
        {   //  create/open the sql db
            db = context.openOrCreateDatabase("query",MODE_PRIVATE,null);
            db.execSQL("CREATE TABLE IF NOT EXISTS contacts (id integer primary key, name VARCHAR, phone VARCHAR)");

        }
        catch (Exception e)
        {
            Log.d("debug","Error on openOrCreateDatabase");
        }
    }

    // add a contact to the db
    public ListAdapter add(String name, String phone)
    {
        if(name.equals("")) { // if there isn't a name
            Toast.makeText(context,"Please Enter a Name",Toast.LENGTH_SHORT).show();
            return null;
        }
        int id = nameAvailable(name); // check if the name is in the db
        if(id==-1) //if the name isn't un the db
            db.execSQL("INSERT INTO contacts (name, phone) VALUES ('" + name + "', '" + phone + "');");
        else //is the name is in the db its update his number
        {
            ContentValues CV = new ContentValues();
            CV.put("phone",phone);
            db.execSQL("UPDATE contacts SET phone = '"+phone+"' WHERE id = '"+ id+"';");
        }
        return updateList();
    }

    //function that check is the name is in the db
    private int nameAvailable(String name)
    {
        Cursor cr = db.rawQuery("SELECT * FROM contacts WHERE name ='"+name+"'",null);

        if(cr.getCount()==0)
            return -1;
        else
        {
            cr.moveToFirst();
            return (Integer.parseInt(cr.getString(0)));
        }
    }

    //function that search in the db by name/phone number
    public ListAdapter search(String name, String phone)
    {
        ArrayList<Contact> contactsArray = new ArrayList<>();
        Cursor c;
        String searchName,searchPhone;

        if(!name.equals("")) //search by name
        {
            if (!phone.equals("")) //searce by name and phone
                c=db.rawQuery("SELECT * FROM contacts WHERE name LIKE '%"+name+"%'AND phone LIKE '%"+phone+"%';",null);
            c=db.rawQuery("SELECT * FROM contacts WHERE name LIKE '%"+name+"%';",null);
        }
        else if(!phone.equals(""))
            c=db.rawQuery("SELECT * FROM contacts WHERE phone LIKE '%"+phone+"%';",null);
        else {
            Toast.makeText(context, "Name And Phone doen't provide", Toast.LENGTH_SHORT).show();
            return null;
        }
        return listHelper(c,contactsArray);
    }

    //function that update the view
    public ListAdapter updateList()
    {
        ArrayList<Contact> contactsArray = new ArrayList<>();

        Cursor c = db.rawQuery("SELECT * FROM contacts",null);
        return listHelper(c,contactsArray);
    }

    //function that help to the updateList and search function (because we dont want a code duplication)
    private ListAdapter listHelper(Cursor c, ArrayList<Contact> contactsArray)
    {
        int nameC = c.getColumnIndex("name");
        int phoneC = c.getColumnIndex("phone");
        c.moveToFirst();
        if(c.getCount()==0)
        {
            if(firstTime)
            {
                firstTime=false;
                return null;
            }
            Toast.makeText(context, "Name And Phone doesn't exist", Toast.LENGTH_SHORT).show();
        }
        else
        {
            String searchName = c.getString(nameC);
            String searchPhone=c.getString(phoneC);
            contactsArray.add(new Contact(searchName,searchPhone));
            while (c.moveToNext())
            {
                searchName = c.getString(nameC);
                searchPhone=c.getString(phoneC);
                contactsArray.add(new Contact(searchName,searchPhone));
            }
            ListAdapter list= new ListAdapter(context,contactsArray);
            return list;

        }
        ListAdapter list= new ListAdapter(context,contactsArray);
        return list;
    }

    //delete the db when we uninstall the application
    public void destroy()
    {
        db.close();
    }

}// end SQLiteDB class