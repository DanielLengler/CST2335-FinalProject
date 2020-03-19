package com.asis.finalproject.nasaearthimage;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.asis.finalproject.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NasaImageFavoritesActivity extends AppCompatActivity {

    private ArrayList<NasaEarthImage> nasaEarthImages = new ArrayList<>();
    private FavoritesAdapter favoritesAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nasa_image_favorites);

        loadFavoritesList();

        favoritesAdapter = new FavoritesAdapter(this, R.layout.nasa_earth_image_row, nasaEarthImages);
        Log.e("onCreate", String.valueOf(nasaEarthImages.size()));

        ListView listView = findViewById(R.id.favouritesList);
        listView.setAdapter(favoritesAdapter);
        listView.setOnItemClickListener((adapterView, view, i, l) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putSerializable("NASA-IMAGE", nasaEarthImages.get(i));

            boolean isLandScape = findViewById(R.id.frameLayout) != null;
            if (isLandScape) {
                FavoriteDetails favoriteDetails = new FavoriteDetails();
                favoriteDetails.setArguments(dataToPass);
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.frameLayout, favoriteDetails)
                        .commit();
            } else {
                Intent intent = new Intent(NasaImageFavoritesActivity.this, FavoriteDetailsActivityHolder.class);
                intent.putExtras(dataToPass);
                startActivity(intent);
            }
        });
        listView.setOnItemLongClickListener((adapterView, view, i, l) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(NasaImageFavoritesActivity.this);
            builder.setTitle("Remove image from favorites?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> {
                        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
                        databaseHelper.deleteImage(nasaEarthImages.get(i));
                        databaseHelper.close();

                        deleteImage(nasaEarthImages.get(i));

                        nasaEarthImages.remove(i);
                        favoritesAdapter.notifyDataSetChanged();

                        Snackbar.make(view, i +" deleted", Snackbar.LENGTH_SHORT).show();

                        //TODO delete files on device for this image
                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            return true;
        });
    }

    private void deleteImage(NasaEarthImage nasaEarthImage) {
        System.err.println(Environment.getExternalStorageDirectory());


        File file = new File(Environment.getExternalStorageDirectory(), nasaEarthImage.getPath());
        boolean result = file.delete();
        Toast.makeText(this, "File delete result: "+result, Toast.LENGTH_LONG).show();
    }

    private void loadFavoritesList() {
        DatabaseHelper databaseHelper = new DatabaseHelper(this);

        Cursor cursor = databaseHelper.getAll();
        cursor.moveToFirst();
        for (int i = 0; i < cursor.getCount(); i++) {
            NasaEarthImage nasaEarthImage = new NasaEarthImage(
                    cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COL_ID)),
                    cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(DatabaseHelper.COL_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(DatabaseHelper.COL_IMAGE_PATH)));
            nasaEarthImages.add(nasaEarthImage);
            cursor.moveToNext();
        }
        cursor.close();

        databaseHelper.close();
    }

    public static Bitmap loadImageFromPath(Context context, NasaEarthImage nasaEarthImage) {
        if (nasaEarthImage.getPath() == null) {
            return null;
        }

        try (FileInputStream fis = context.openFileInput(nasaEarthImage.getPath())) {
            return BitmapFactory.decodeStream(fis);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private class FavoritesAdapter extends ArrayAdapter<NasaEarthImage> {

        FavoritesAdapter(@NonNull Context context, int resource, List<NasaEarthImage> list) {
            super(context, resource, list);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = convertView;

            if (rowView == null && layoutInflater != null) {
                rowView = layoutInflater.inflate(R.layout.nasa_earth_image_row, parent, false);
            }

            TextView longitudeTextView = rowView.findViewById(R.id.longitude);
            TextView latitudeTextView = rowView.findViewById(R.id.latitude);
            ImageView imageView = rowView.findViewById(R.id.imageView);

            NasaEarthImage nasaEarthImage = getItem(position);
            Log.e("FavoritesAdapter", String.valueOf(nasaEarthImage));
            if (nasaEarthImage != null) {
                longitudeTextView.setText(String.valueOf(nasaEarthImage.getLongitude()));
                latitudeTextView.setText(String.valueOf(nasaEarthImage.getLatitude()));
                imageView.setImageBitmap(loadImageFromPath(rowView.getContext(), nasaEarthImage));
            }

            return rowView;
        }
    }
}
