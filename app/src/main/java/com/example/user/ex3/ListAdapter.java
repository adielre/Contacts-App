package com.example.user.ex3;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends ArrayAdapter<Contact>
{
    //fields
    private List<Contact> contactList;
    private Context context;

    //constructor
    public ListAdapter(Context context, ArrayList<Contact> contactList)
    {
        super(context,0, contactList);
        this.context = context;
        this.contactList = contactList;
    }

    // adapter to the listView for the name phone and phone Image
    @Override
    public View getView(int position,  View convertView,  ViewGroup parent)
    {
        View tempContact = convertView;

        if(tempContact==null)
            tempContact = LayoutInflater.from(context).inflate(R.layout.one_line, parent, false);

        Contact c = contactList.get(position);

        ImageView imageView = tempContact.findViewById(R.id.phoneImg);

        if(c.getPhone().equals("")) //if there isn't a phone (gray img)
            imageView.setImageResource(R.drawable.phone_unavalible);
        else    //if there is a phone (green img)
            imageView.setImageResource(R.drawable.phone_avalible);

        //put the name in the list
        ((TextView)(tempContact.findViewById(R.id.nameLine))).setText(c.getName());

        //put the phone number in the list
        ((TextView)(tempContact.findViewById(R.id.phoneLine))).setText(c.getPhone());

        return tempContact;
    }

}// ListAdapter class