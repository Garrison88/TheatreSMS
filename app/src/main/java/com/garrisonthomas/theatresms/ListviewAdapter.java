package com.garrisonthomas.theatresms;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/********* Adapter class extends with BaseAdapter and implements with OnClickListener ************/
public class ListviewAdapter extends BaseAdapter{

    /*********** Declare Used Variables *********/
    private ArrayList<ListModel> data;
    private static LayoutInflater inflater=null;
    public Resources res;
    ListModel tempValues=null;
    int i=0;

    public ListviewAdapter(LayoutInflater _inflater, ArrayList<ListModel> d, Resources resLocal) {

        data=d;
        res = resLocal;

        inflater = _inflater;

    }

    /******** What is the size of Passed Arraylist Size ************/
    public int getCount() {

        if(data.size()<=0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    /********* Create a holder Class to contain inflated xml file elements *********/
    public static class ViewHolder{

        public TextView text;
        public TextView text_right;
    }

    /****** Depends upon data size called for each row , Create each ListView row *****/
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if(convertView==null){

            /****** Inflate tabitem.xml file for each row ( Defined below ) *******/
            vi = inflater.inflate(R.layout.message_layout, null);

            /****** View Holder Object to contain tabitem.xml file elements ******/

            holder = new ViewHolder();
            holder.text = (TextView) vi.findViewById(R.id.message_body);
            holder.text_right=(TextView)vi.findViewById(R.id.time_stamp);

            /************  Set holder with LayoutInflater ************/
            vi.setTag( holder );
        }
        else
            holder=(ViewHolder)vi.getTag();

        if(data.size()<=0)
        {
            holder.text.setText("Nothing to see here...");

        }
        else
        {
            /***** Get each Model object from Arraylist ********/
            tempValues=null;
            tempValues = ( ListModel ) data.get( position );

            /************  Set Model values in Holder elements ***********/

            holder.text.setText( tempValues.get_text() );
            holder.text_right.setText(tempValues.get_timestamp());


        }



        return vi;
    }

}
