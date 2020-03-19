package com.asis.finalproject.imageoftheday;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.asis.finalproject.R;

import java.io.FileInputStream;
import java.io.FileNotFoundException;


public class DetailsFragmentImageOfTheDay extends Fragment {

    private Bundle dataFromActivity;
    private long id;
    private String title;
    private String explanation;
    private String date;
    private String path;
    private String url;
    private AppCompatActivity parentActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        dataFromActivity = getArguments();
        id = dataFromActivity.getLong(ListOfImagesOfTheDay.ITEM_ID );
        title = dataFromActivity.getString(ListOfImagesOfTheDay.ITEM_TITLE);
        date = dataFromActivity.getString(ListOfImagesOfTheDay.ITEM_DATE);
        explanation = dataFromActivity.getString(ListOfImagesOfTheDay.ITEM_EXPLANATION);
        url = dataFromActivity.getString(ListOfImagesOfTheDay.ITEM_URL);
        path = dataFromActivity.getString(ListOfImagesOfTheDay.ITEM_PATH);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details_image_of_the_day, container, false);

        //show the title
        TextView titleView = (TextView)result.findViewById(R.id.textViewFragmentTitleImageOfTheDay);
        titleView.setText("Title: " + title);
        //show the date
        TextView dateView = (TextView)result.findViewById(R.id.textViewFragmentDateImageOfTheDay);
        dateView.setText("Date: " + date);
        //show the explanation
//        TextView explanationView = (TextView)result.findViewById(R.id.textViewFragmentExplanation);
//        explanationView.setText("Explanation: " + explanation);
        //show the url
        TextView urlView = (TextView)result.findViewById(R.id.textViewFragmentUrlImageOfTheDay);
        urlView.setText("URL: " + url);
        //show the message

        FileInputStream fis = null;
        try {    fis = getContext().openFileInput(date + ".png");   }
        catch (FileNotFoundException e) {    e.printStackTrace();  }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ImageView imageView = (ImageView)result.findViewById(R.id.imageViewFragmentImageImageOfTheDay);
        imageView.setImageBitmap(bm);

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.textViewFragmentIdImageOfTheDay);
        idView.setText("ID=" + id);

        // get the delete button, and add a click listener:
        Button finishButton = (Button)result.findViewById(R.id.buttonHideFragmentImageOfTheDay);
        finishButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
            getActivity().finish();
        });

        return result;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        //context will either be FragmentExample for a tablet, or EmptyActivity for phone
        parentActivity = (AppCompatActivity)context;
    }
}
