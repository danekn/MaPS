package com.example.android.handyshop;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StreamDownloadTask;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class ExpandableRequestsOffersListAdapter extends BaseExpandableListAdapter {

    private final ArrayList<Group> groups;
    public Context context;

    public ExpandableRequestsOffersListAdapter(Context context, ArrayList<Group> groups) {
        this.context = context;
        this.groups = groups;

    }
    private static class ViewHolder
    {
        CheckedTextView title;
        TextView description;
        ImageView image;
        ImageButton addItem;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return groups.get(groupPosition).children.get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public int getChildType(int groupPosition, int childPosition){
        if(childPosition==4)
            return 1;
        if(childPosition==3)
            return 2;
        return 0;
    }

    @Override
    public int getChildTypeCount(){
        return 3;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final String children = (String) getChild(groupPosition, childPosition);

        if (convertView==null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if(getChildType(groupPosition,childPosition)==0)
                convertView = inflater.inflate(R.layout.listrow_details_content, null);
            if(getChildType(groupPosition,childPosition)==1)
                convertView = inflater.inflate(R.layout.listrow_details_content_button, null);
            if(getChildType(groupPosition,childPosition)==2)
                convertView = inflater.inflate(R.layout.listrow_details_ratingbar, null);

        }
        if(getChildType(groupPosition,childPosition)==0) {

            TextView description = (TextView) convertView.findViewById(R.id.detail);
            description.setText(children);
        }
        if(getChildType(groupPosition,childPosition)==1) {
            TextView username = (TextView) convertView.findViewById(R.id.username);
            username.setText(children);
        }
        if(getChildType(groupPosition,childPosition)==2) {
            RatingBar rbar = (RatingBar) convertView.findViewById(R.id.ratingBarList);
            double feedback =Double.parseDouble(children.toString());
            rbar.setRating((float)(feedback));
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return groups.get(groupPosition).children.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groups.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return groups.size();
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        super.onGroupCollapsed(groupPosition);
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listrow_group_req_off, null);

        }
        Group group = (Group) getGroup(groupPosition);
        final Bitmap bmp;
        String[] keyAndfeedback = group.string.split(Pattern.quote("^"));
        CheckedTextView title = (CheckedTextView)convertView.findViewById(R.id.textView1);
        int i = keyAndfeedback[0].indexOf("?");
        final String t = keyAndfeedback[0].substring(0,i);
        final ImageView imageView= (ImageView) convertView.findViewById(R.id.imageReqOff);
        title.setText(t+keyAndfeedback[0].substring(i+1,keyAndfeedback[0].length()));
        final StorageReference imagesRef = (MainActivity.storageRef).child("images/"+keyAndfeedback[1]+t+".jpeg");
        imagesRef.getBytes(Long.MAX_VALUE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {

                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
                System.out.println("sss");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                System.out.println("ERRORE!!!! "+imagesRef);
                Bitmap bmp = BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.noimageicon);
                imageView.setImageBitmap(bmp);
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}