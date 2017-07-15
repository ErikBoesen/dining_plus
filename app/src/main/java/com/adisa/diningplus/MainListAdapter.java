package com.adisa.diningplus;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 * Created by Adisa on 4/27/2017.
 */

class MainListAdapter extends BaseAdapter {
    private Context context;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 0;
    private HashMap<String, Integer> crestMap = new HashMap<>();
    private ArrayList<MainActivity.HallItem> hallList = new ArrayList<>();
    private ArrayList<MainActivity.HallItem> openList = new ArrayList<>();
    private ArrayList<MainActivity.HallItem> closedList = new ArrayList<>();
    private Comparator<MainActivity.HallItem> hallSort = new Comparator<MainActivity.HallItem>() {
        @Override
        public int compare(MainActivity.HallItem o1, MainActivity.HallItem o2) {
            return Double.compare(o1.distance, o2.distance);
        }
    };

    class ViewHolder {
        ImageView crest;
        TextView name;
        TextView distance;
        TextView occupancy;
    }

    MainListAdapter(Context context) {
        this.context = context;
        crestMap.put("Berkeley", R.drawable.berkeley);
        crestMap.put("Branford", R.drawable.branford);
        crestMap.put("Commons", R.drawable.commons);
        crestMap.put("Calhoun", R.drawable.calhoun);
        crestMap.put("Grace Hopper", R.drawable.calhoun);
        crestMap.put("Stiles", R.drawable.stiles);
        crestMap.put("Davenport", R.drawable.davenport);
        crestMap.put("Franklin", R.drawable.franklin);
        crestMap.put("Murray", R.drawable.murray);
        crestMap.put("Hall of Graduate Studies", R.drawable.grad);
        crestMap.put("Jonathan Edwards", R.drawable.je);
        crestMap.put("Morse", R.drawable.morse);
        crestMap.put("Pierson", R.drawable.pierson);
        crestMap.put("Saybrook", R.drawable.saybrook);
        crestMap.put("Silliman", R.drawable.silliman);
        crestMap.put("Timothy Dwight", R.drawable.td);
        crestMap.put("Trumbull", R.drawable.trumbull);
    }

    @Override
    public void notifyDataSetChanged() {
        hallList = new ArrayList<>();
        Collections.sort(openList, hallSort);
        Collections.sort(closedList, hallSort);
        if (openList.size() > 0){
            MainActivity.HallItem openHeader = new MainActivity.HallItem("Open now", -1, -1 , -1, -1);
            hallList.add(openHeader);
            hallList.addAll(openList);
        }
        if (closedList.size() > 0){
            MainActivity.HallItem closedHeader = new MainActivity.HallItem("Closed now", -1, -1 , -1, -1);
            hallList.add(closedHeader);
            hallList.addAll(closedList);
        }
        super.notifyDataSetChanged();
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder viewHolder;
        MainActivity.HallItem item = getItem(position);
        if (convertView == null) {
            viewHolder = new ViewHolder();
            if (getItemViewType(position) == TYPE_ITEM){
                convertView = inflater.inflate(R.layout.dininghall_list, null);
                viewHolder.crest = (ImageView) convertView.findViewById(R.id.crest);
                viewHolder.name = (TextView) convertView.findViewById(R.id.dhall_name);
                viewHolder.distance = (TextView) convertView.findViewById(R.id.dhall_dist);
                viewHolder.occupancy = (TextView) convertView.findViewById(R.id.dhall_occupancy);
            } else {
                convertView = inflater.inflate(R.layout.dininghall_header, null);
                viewHolder.name = (TextView) convertView.findViewById(R.id.header_name);
                convertView.setEnabled(false);
                convertView.setOnClickListener(null);
            }

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        if (getItemViewType(position) == TYPE_ITEM){
            Integer crestId = crestMap.get(item.name);
            if (crestId != null){
                viewHolder.crest.setImageDrawable(context.getResources().getDrawable(crestId));
            } else {
                viewHolder.crest.setImageDrawable(context.getResources().getDrawable(R.drawable.commons));
            }
            viewHolder.name.setText(item.name);

            DecimalFormat numberFormat = new DecimalFormat("0.00");
            if (item.distance > 50){
                viewHolder.distance.setText("> 50 mi");
            } else {
                viewHolder.distance.setText("" + numberFormat.format(item.distance) + " mi");
            }
            int capacity = item.occupancy * 10;

            if (capacity <= 30) {
                viewHolder.occupancy.setTextColor(Color.parseColor("#64dd17"));
            }
            if (capacity >= 40) {
                viewHolder.occupancy.setTextColor(Color.parseColor("#eb9438"));
            }
            if (capacity >= 80){
                viewHolder.occupancy.setTextColor(Color.parseColor("#d62b2b"));
            }
            viewHolder.occupancy.setText(capacity + "%");
        } else {
            viewHolder.name.setText(item.name);
        }

        return convertView;
    }

    public void setLists(ArrayList<MainActivity.HallItem> openList, ArrayList<MainActivity.HallItem> closedList){
        this.openList = openList;
        this.closedList = closedList;
    }

    @Override
    public int getCount() {
        return hallList.size();
    }

    @Override
    public MainActivity.HallItem getItem(int position) {
        return hallList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        return hallList.get(position).occupancy == -1 ? TYPE_HEADER : TYPE_ITEM;
    }
}