package com.example.levelapp_assignment;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

public class Adapter extends ArrayAdapter<TimeXY> {
    private Activity context;
    List<TimeXY> details;


    public Adapter(Activity context, List<TimeXY> details) {
        super(context, R.layout.list_content,details);

        this.context = context;
        this.details = details;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        //this method will instantiate our layout file from the layout_artist_list.xml into
        //our view objects


        View view = convertView;
        if(view==null){
            LayoutInflater inflater = context.getLayoutInflater();
            view = inflater.inflate(R.layout.list_content
                    , null
                    , true);
        }
        TimeXY detail = details.get(position);
        if(detail != null) {

            TextView textViewX = view.findViewById(R.id.textViewX);
            TextView textViewY = view.findViewById(R.id.textViewY);
            TextView textViewTimestamp = view.findViewById(R.id.textViewTimeStamp);

            textViewX.setText(detail.getXvalue());
            textViewY.setText(detail.getYvalue());
            textViewTimestamp.setText(detail.getTimestamp());


        }
        return view;
    }
}
