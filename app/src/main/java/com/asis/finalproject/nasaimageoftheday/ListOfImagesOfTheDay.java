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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.asis.finalproject.R;
import com.asis.finalproject.MainActivity;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

/**
 * Class handle list view that display all the items in the database.
 * This class also has date information to get more images and button that directs the application to single image view class.
 */
public class ListOfImagesOfTheDay extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    public static final String ITEM_TITLE = "TITLE";
    public static final String ITEM_DATE = "DATE";
    public static final String ITEM_EXPLANATION = "EXPLANATION";
    public static final String ITEM_PATH = "PATH";
    public static final String ITEM_URL = "URL";
    final Calendar myCalendar = Calendar.getInstance();
    SharedPreferences dateSharedPrefs = null;
    EditText editText;
    SQLiteDatabase db;
    private ArrayList<NasaImageItem> elements;
    private MyListAdapter myAdapter;
//    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
//    public static final String ITEM_TYPE = "TYPE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_of_images_of_the_day);

        boolean isTablet = findViewById(R.id.frameLayout) != null;
        // Set data from previous execution in to date field.
        dateSharedPrefs = getSharedPreferences("Date", Context.MODE_PRIVATE);
        editText = findViewById(R.id.DateImageOfTheDay);
        String savedDate = dateSharedPrefs.getString("ReserveName", editText.getText().toString());
        editText.setText(savedDate);
        loadDataFromDatabase();

        Button goToImageOfTheDay = findViewById(R.id.getImageImageOfTheDay);
        Intent imageOfTheDay = new Intent(ListOfImagesOfTheDay.this, NasaImageOfTheDay.class);
        goToImageOfTheDay.setOnClickListener((v) -> {
            imageOfTheDay.putExtra("DATE", editText.getText().toString());
            startActivityForResult(imageOfTheDay, 56);
            });

        // Create calendar for the user pick a date.
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
                new DatePickerDialog(ListOfImagesOfTheDay.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
//                Snackbar.make(v, "Loading Calendar", Snackbar.LENGTH_LONG)
//                        .show();

            }
        });

        ListView myList = findViewById(R.id.listViewOfNasaImagesOfTheDay);
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
                DetailsFragmentImageOfTheDay dFragment = new DetailsFragmentImageOfTheDay(); //add a DetailFragment
                dFragment.setArguments( dataToPass ); //pass it a bundle for information
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, dFragment) //Add the fragment in FrameLayout
                        .commit(); //actually load the fragment.
            }
            else //isPhone
            {
                Intent nextActivity = new Intent(ListOfImagesOfTheDay.this, EmptyActivityImageOfTheDay.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivity(nextActivity); //make the transition
            }
        });

        // Create alert dialog to delete items from database
        myList.setOnItemLongClickListener((parent, view, position, id) -> {
            new AlertDialog.Builder(ListOfImagesOfTheDay.this)
                    .setTitle(R.string.alertDialogImageOfTheDay)
                    .setMessage(getString(R.string.rowMessageImageOfTheDay) + position +
                            getString(R.string.databaseIDImageOfTheDay) + id +
                            getString(R.string.titleAlertDialogImageOfTheDay) + elements.get(position).getTitle() +
                            getString(R.string.dateAlerDialogImageOfTheDay) + elements.get(position).getDate())
                    .setPositiveButton(R.string.yesAlerDialogImageOfTheDay,(dialog, item) -> {
                        deleteMessage(elements.get(position));
                        elements.remove(position);
                        List<Fragment> fragments = getSupportFragmentManager().getFragments();
                        if (!fragments.isEmpty())
                            getSupportFragmentManager().beginTransaction().remove(fragments.get(0)).commit();
                        myAdapter.notifyDataSetChanged();
                        Snackbar.make(view, R.string.snackBarImageDeletedImageOfTheDay, Snackbar.LENGTH_LONG)
                                .show();
                    })
                    .setNegativeButton(R.string.noAlertDialogImageOfTheDay, (dialog, item) -> {
                        dialog.cancel();
                    })
                    .show();
            return true;
        });

        //This gets the toolbar from the layout:
        Toolbar tBar = findViewById(R.id.toolbarImageOfTheDay);

        //This loads the toolbar, which calls onCreateOptionsMenu below:
        setSupportActionBar(tBar);

        //For NavigationDrawer:
        DrawerLayout drawer = findViewById(R.id.drawer_layout_imageOfTheDay);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawer, tBar, 0, 0);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.navDrawerMenuImageOfTheDay);
        navigationView.setNavigationItemSelectedListener(this);
        getSupportActionBar().setTitle(R.string.actionBarDescriptionImageOfTheDay);
        getSupportActionBar().setSubtitle(getString(R.string.actionBarSubTitleImageOfTheDay));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_nav_bar_image_of_the_day, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        String message = null;
        Intent goToMain = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);
        Intent goToBbc = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);
        Intent goToGuardian = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);
        Intent goToNasaEarth = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);

        //Look at your menu XML file. Put a case for every id in that file:
        switch(item.getItemId())
        {
            //what to do when the menu item is selected:
            case R.id.navBarBBC:
                startActivity(goToBbc);
                break;
            case R.id.navBarTheGuardian:
                startActivity(goToGuardian);
                break;
            case R.id.navBarNasa:
                startActivity(goToNasaEarth);
                break;
            case R.id.navBarMain:
                startActivity(goToMain);
                break;
            case R.id.help_item_imageOfTheDay:
                new AlertDialog.Builder(ListOfImagesOfTheDay.this)
                        .setTitle("Help")
                        .setMessage(R.string.helpImageOfTheDay)
                        .setPositiveButton(R.string.okImageOfTheDay, ((dialog, which) -> {
                            dialog.cancel();
                        }))
                        .show();
                break;
        }
        return true;
    }

    @Override
    public boolean onNavigationItemSelected( MenuItem item) {

        String message = null;
        Intent goToMain = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);
        Intent goToBbc = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);
        Intent goToGuardian = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);
        Intent goToNasaEarth = new Intent(ListOfImagesOfTheDay.this, MainActivity.class);

        switch(item.getItemId())
        {
            case R.id.navDrawerBBC:
                startActivity(goToBbc);
                break;
            case R.id.navDrawerTheGuardian:
                startActivity(goToGuardian);
                break;
            case R.id.navDrawerNasaEarth:
                startActivity(goToNasaEarth);
                break;
            case R.id.navDrawerMain:
                startActivity(goToMain);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout_imageOfTheDay);
        drawerLayout.closeDrawer(GravityCompat.START);
        return false;
    }

    @Override
    protected void onPause() {
        saveSharedPrefs( editText.getText().toString());
        super.onPause();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        loadDataFromDatabase();
        myAdapter.notifyDataSetChanged();

    }

    /**
     * Method set selected date on date field with the proper format.
     */
    private void updateLabel() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editText.setText(sdf.format(myCalendar.getTime()));
    }

    /**
     * Used to retrive data from previous execution.
     * @param stringToSave holds information that will be stored for future execution.
     */
    private void saveSharedPrefs(String stringToSave)
    {
        SharedPreferences.Editor editor = dateSharedPrefs.edit();
        editor.putString("ReserveName", stringToSave);
        editor.commit();
    }

    // Method executed when list view needs to be refreshed. This happens on resume of an execution and when items are deleted/inserted.
    private void loadDataFromDatabase()
    {
        //get a database connection:
        DbOpenerImageOfTheDay dbOpener = new DbOpenerImageOfTheDay(this);
        db = dbOpener.getWritableDatabase();
        elements = new ArrayList<>();

        // We want to get all of the columns. Look at MyOpener.java for the definitions:
        String [] columns = {DbOpenerImageOfTheDay.COL_ID, DbOpenerImageOfTheDay.COL_DATE, DbOpenerImageOfTheDay.COL_EXPLANATION, DbOpenerImageOfTheDay.COL_URL, DbOpenerImageOfTheDay.COL_TITLE, DbOpenerImageOfTheDay.COL_PATH};
        //query all the results from the database:
        Cursor results = db.query(false, DbOpenerImageOfTheDay.TABLE_NAME, columns, null, null, null, null, null, null);

        printCursor(results, db.getVersion());

        //Now the results object has rows of results that match the query.
        //find the column indices:
        int dateColIndex = results.getColumnIndex(DbOpenerImageOfTheDay.COL_DATE);
        int explanationColIndex = results.getColumnIndex(DbOpenerImageOfTheDay.COL_EXPLANATION);
        int urlColIndex = results.getColumnIndex(DbOpenerImageOfTheDay.COL_URL);
        int titleColIndex = results.getColumnIndex(DbOpenerImageOfTheDay.COL_TITLE);
        int pathColIndex = results.getColumnIndex(DbOpenerImageOfTheDay.COL_PATH);
        int idColIndex = results.getColumnIndex(DbOpenerImageOfTheDay.COL_ID);

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

    /**
     * Method simple deletes data from db.
     * @param c Image item object used to retrieve ID.
     */
    protected void deleteMessage(NasaImageItem c)
    {
        db.delete(DbOpenerImageOfTheDay.TABLE_NAME, DbOpenerImageOfTheDay.COL_ID + "= ?", new String[] {Long.toString(c.getId())});
    }

    /**
     * Class used to inflates information from db into list view.
     */
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
            View newView = inflater.inflate(R.layout.row_layout_image_of_the_day, parent, false);
            TextView titleFromDb = newView.findViewById(R.id.textRowLayoutImageOfTheDayListView);
            TextView dateFromDb = newView.findViewById(R.id.textViewDateOnListViewImageOfTheDay);
            ImageView imageFromDb = newView.findViewById(R.id.nasaImageOfTheDayOnListView);
            NasaImageItem nasaImage = getItem(position);

            FileInputStream fis = null;
            try {    fis = openFileInput(nasaImage.getDate() + ".png");   }
            catch (FileNotFoundException e) {    e.printStackTrace();  }
            Bitmap bm = BitmapFactory.decodeStream(fis);

            titleFromDb.setText(String.format("%s%s", getString(R.string.titleFromDbImageOfTheDay), nasaImage.getTitle()));
            dateFromDb.setText(String.format("%s%s", getString(R.string.dateFromDbImageOfTheDay), nasaImage.getDate()));
            imageFromDb.setImageBitmap(bm);

            return newView;
        }
    }

    /**
     * Used for debugging purposes.
     * @param c is cursor in database
     * @param version is the version of the database
     */
    public void printCursor( Cursor c, int version){
        Log.d("DB_DATA", "The database version is: " + version);
        Log.d("DB_DATA", "The number of columns in the database is: " + c.getColumnCount());
        Log.d("DB_DATA", "The names of the columns are: " + Arrays.toString(c.getColumnNames()));
        Log.d("DB_DATA", "The total of rows in the table is: " + c.getCount());
        Log.d("DB_DATA", DatabaseUtils.dumpCursorToString(c));
    }
}