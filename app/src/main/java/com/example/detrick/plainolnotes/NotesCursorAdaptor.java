package com.example.detrick.plainolnotes;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.detrick.plainolnotes.DBOpenHelper;
import com.example.detrick.plainolnotes.R;

/**
 * Created by Detrick on 10/19/2015.
 */
 public class NotesCursorAdaptor extends CursorAdapter {
    public NotesCursorAdaptor(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.note_list_item,parent,false);

    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        String noteText = cursor.getString(cursor.getColumnIndex(DBOpenHelper.NOTE_TEXT));
        int pos = noteText.indexOf(10);
        if (pos!=-1){
            noteText = noteText.substring(0,pos)+" ...";

        }
        TextView tv = (TextView) view.findViewById(R.id.tvNote);
        tv.setText(noteText);

    }
}
