package com.asis.finalproject.nasaimageoftheday;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.asis.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ListOfImages extends AppCompatActivity {

    public static final String ITEM_TITLE = "TITLE";
    public static final String ITEM_DATE = "DATE";
    public static final String ITEM_EXPLANATION = "EXPLANATION";
    public static final String ITEM_PATH = "PATH";
    public static final String ITEM_URL = "URL";
    final Calendar myCalendar = Calendar.getInstance();
    SharedPreferences dateSharedPrefs = null;
    EditText editText;
    SQLiteDatabase db;
    private ArrayList<NasaImageItem> elements = new ArrayList<>();
    private MyListAdapter myAdapter;
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final String ITEM_TYPE = "TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_images);

        boolean isTablet = findViewById(R.id.frameLayout) != null;
        dateSharedPrefs = getSharedPreferences("Date", Context.MODE_PRIVATE);
        editText = findViewById(R.id.Date);
        String savedDate = dateSharedPrefs.getString("ReserveName", editText.getText().toString());
        editText.setText(savedDate);
        loadDataFromDatabase();


        Button goToImageOfTheDay = findViewById(R.id.getImage);
        Intent imageOfTheDay = new Intent(ListOfImages.this, NasaImageOfTheDay.class);
        goToImageOfTheDay.setOnClickListener((v) -> {
            imageOfTheDay.putExtra("DATE", editText.getText().toString());
//            startActivity(imageOfTheDay);
            startActivityForResult(imageOfTheDay, 56);
            });


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        editText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(ListOfImages.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                Snackbar.make(v, "Loading Calendar", Snackbar.LENGTH_LONG)
                        .show();

            }
        });

        ListView myList = findViewById(R.id.listViewOfNasaImages);
        myList.setAdapter(myAdapter = new MyListAdapter());

        myList.setOnItemClickListener((parent, view, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString(ITEM_TITLE, elements.get(position).getTitle());
            dataToPass.putString(ITEM_DATE, elements.get(position).getDate());
            dataToPass.putString(ITEM_EXPLANATION, elements.get(position).getExplanation());
            dataToPass.putString(ITEM_PATH, elements.get(position).getPath());
            dataToPass.putString(ITEM_URL, elements.get(position).getUrl());
            dataToPass.putInt(ITEM_POSITION, position);
            dataToPass.putLong(ITEM_ID, id);

            if(isTablet)
            {
                DetailsFragment dFragment = new DetailsFragment(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ListOfImages.this, EmptyActivity.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(ListOfImages.this)
                    .setTitle("Do you want to delete this item?")
                    .setMessage("Row: " + position +
                            "\nDatabase ID: " + id +
                            "\nTitle: " + elements.get(position).getTitle() +
                            "\nDate: " + elements.get(position).getDate())
                    .setPositiveButton("Yes",(dialog, item) -> {
                        deleteMessage(elements.get(position));
                        elements.remove(position);
                        List<Fragment> fragments = getSupportFragmentManager().getFragments();
                        if (!fragments.isEmpty())
                            getSupportFragmentManager().beginTransaction().remove(fragments.get(0)).commit();
                        myAdapter.notifyDataSetChanged();
                    })
                    .setNegativeButton("No", (dialog, item) -> {
                        dialog.cancel();
                    })
                    .show();
            return true;
        });
    }

    @Override
    protected void onPause() {
        saveSharedPrefs( editText.getText().toString());
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        myAdapter = new MyListAdapter();
        myAdapter.notifyDataSetChanged();
        super.onActivityResult(requestCode, resultCode, data);
    }


//    @Override
//    protected void onResume() {
//        Log.e("ListOfImages","In function:" + "onResume()");
//        myAdapter.notifyDataSetChanged();
//        super.onResume();
//    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = dateSharedPrefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();
    }

    private void loadDataFromDatabase()
    {
        //get a database connection:
        MyOpener dbOpener = new MyOpener(this);
        db = dbOpener.getWritableDatabase();


        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {MyOpener.COL_ID, MyOpener.COL_DATE, MyOpener.COL_EXPLANATION, MyOpener.COL_URL, MyOpener.COL_TITLE, MyOpener.COL_PATH};
        //query all the results from the database:
        Cursor results = db.query(false, MyOpener.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results, db.getVersion());

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int dateColIndex = results.getColumnIndex(MyOpener.COL_DATE);
        int explanationColIndex = results.getColumnIndex(MyOpener.COL_EXPLANATION);
        int urlColIndex = results.getColumnIndex(MyOpener.COL_URL);
        int titleColIndex = results.getColumnIndex(MyOpener.COL_TITLE);
        int pathColIndex = results.getColumnIndex(MyOpener.COL_PATH);
        int idColIndex = results.getColumnIndex(MyOpener.COL_ID);

        //iterate over the results, return true if there is a next item:
        while(results.moveToNext())
        {
            String date = results.getString(dateColIndex);
            String explanation = results.getString(explanationColIndex);
            String url = results.getString(urlColIndex);
            String title = results.getString(titleColIndex);
            String path = results.getString(pathColIndex);
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            elements.add(new NasaImageItem(title, explanation, date, url, path, id));
        }

        //At this point, the contactsList array has loaded every row from the cursor.
    }

    protected void deleteMessage(NasaImageItem c)
    {
        db.delete(MyOpener.TABLE_NAME, MyOpener.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }


    class MyListAdapter extends BaseAdapter {
        @Override
        public int getCount() {return elements.size();}

        @Override
        public NasaImageItem getItem(int position) {return elements.get(position);}

        @Override
        public long getItemId(int position) {return getItem(position).getId();}

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View newView = inflater.inflate(R.layout.row_layout, parent, false);
            TextView titleFromDb = newView.findViewById(R.id.textRowLayout);
            TextView dateFromDb = newView.findViewById(R.id.textViewDateOnListView);
            ImageView imageFromDb = newView.findViewById(R.id.nasaImageOnListView);
            NasaImageItem nasaImage = getItem(position);

            FileInputStream fis = null;
            try {    fis = openFileInput(nasaImage.getDate() + ".png");   }
            catch (FileNotFoundException e) {    e.printStackTrace();  }
            Bitmap bm = BitmapFactory.decodeStream(fis);

            titleFromDb.setText("Title: " + nasaImage.getTitle());
            dateFromDb.setText("Date: " + nasaImage.getDate());
            imageFromDb.setImageBitmap(bm);

            return newView;
        }
    }

    public void printCursor( Cursor c, int version){
        Log.d("DB_DATA", "The database version is: " + version);
        Log.d("DB_DATA", "The number of columns in the database is: " + c.getColumnCount());
        Log.d("DB_DATA", "The names of the columns are: " + Arrays.toString(c.getColumnNames()));
        Log.d("DB_DATA", "The total of rows in the table is: " + c.getCount());
        Log.d("DB_DATA", DatabaseUtils.dumpCursorToString(c));
    }
}