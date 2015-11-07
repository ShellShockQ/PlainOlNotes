package com.example.detrick.plainolnotes;

import android.app.AlertDialog;
import android.app.LoaderManager;
import android.content.ContentValues;
import android.content.CursorLoader;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>
{
    private static final String TAG = "MainActivity";
    private static final int EDITOR_REQUEST_CODE =1001 ;
    private CursorAdapter cursorAdapter;
    //@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        try {
//            Cursor cursor = getContentResolver().query(NotesProvider.CONTENT_URI,
//                    DBOpenHelper.ALL_COLUMNS,
//                    null,null,null,null);
//            String[] from ={DBOpenHelper.NOTE_TEXT};
//            int[] to ={R.id.tvNote};

            cursorAdapter = new NotesCursorAdaptor(this,null,0);
            ListView list = (ListView) findViewById(android.R.id.list);
            list.setAdapter(cursorAdapter);
            list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                   Intent intent = new Intent(MainActivity.this,EditorAcvity.class);
                    Uri uri = Uri.parse(NotesProvider.CONTENT_URI+"/"+id);
                    intent.putExtra(NotesProvider.CONTENT_ITEM_TYPE,uri);
                    startActivityForResult(intent,EDITOR_REQUEST_CODE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        getLoaderManager().initLoader(0,null, this);
    }

    private void insertNote(String noteText) {
        ContentValues values = new ContentValues();
        values.put(DBOpenHelper.NOTE_TEXT, noteText);
        Uri noteUri = getContentResolver().insert(NotesProvider.CONTENT_URI, values);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id){
            case R.id.action_create_sample:
                insertSampleData();
                break;
            case R.id.action_delete_all:
                deleteSampleData();
                break;

        }

        return super.onOptionsItemSelected(item);
    }

    private void deleteSampleData() {
        DialogInterface.OnClickListener dialogClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int button) {
                        if (button == DialogInterface.BUTTON_POSITIVE) {
                            //Insert Data management code here
                            getContentResolver().delete(NotesProvider.CONTENT_URI,null,null);
                            RestartLoader();
                            Toast.makeText(MainActivity.this,
                                    getString(R.string.all_deleted),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(android.R.string.yes), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener)
                .show();

    }

    private void insertSampleData() {
        insertNote("Simple Note");
        insertNote("Multi-line\nnote");
        insertNote("This is a very long note of text that should exceed the width of the screen");
        RestartLoader();


    }

    private void RestartLoader() {
        getLoaderManager().restartLoader(0,null,this);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      return new CursorLoader(this,NotesProvider.CONTENT_URI,
              null,null,null,null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        cursorAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        cursorAdapter.swapCursor(null);
    }

    public void OpenEditorForNewNote(View view) {
        Intent intent = new Intent(this,EditorAcvity.class);
        startActivityForResult(intent, EDITOR_REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==EDITOR_REQUEST_CODE &&  resultCode==RESULT_OK){
        RestartLoader();

        }
    }
}
