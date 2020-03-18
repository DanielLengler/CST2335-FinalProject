package com.asis.finalproject.nasaimageoftheday;

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


public class DetailsFragment extends Fragment {

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
        id = dataFromActivity.getLong(ListOfImages.ITEM_ID );
        title = dataFromActivity.getString(ListOfImages.ITEM_TITLE);
        date = dataFromActivity.getString(ListOfImages.ITEM_DATE);
        explanation = dataFromActivity.getString(ListOfImages.ITEM_EXPLANATION);
        url = dataFromActivity.getString(ListOfImages.ITEM_URL);
        path = dataFromActivity.getString(ListOfImages.ITEM_PATH);

        // Inflate the layout for this fragment
        View result =  inflater.inflate(R.layout.fragment_details, container, false);

        //show the title
        TextView titleView = (TextView)result.findViewById(R.id.textViewFragmentTitle);
        titleView.setText("Title: " + title);
        //show the date
        TextView dateView = (TextView)result.findViewById(R.id.textViewFragmentDate);
        dateView.setText("Date: " + date);
        //show the explanation
//        TextView explanationView = (TextView)result.findViewById(R.id.textViewFragmentExplanation);
//        explanationView.setText("Explanation: " + explanation);
        //show the url
        TextView urlView = (TextView)result.findViewById(R.id.textViewFragmentUrl);
        urlView.setText("URL: " + url);
        //show the message

        FileInputStream fis = null;
//        try {    fis = openFileInput(date + ".png");   }
//        catch (FileNotFoundException e) {    e.printStackTrace();  }
        Bitmap bm = BitmapFactory.decodeStream(fis);
        ImageView imageView = (ImageView)result.findViewById(R.id.imageViewFragmentImage);
        imageView.setImageBitmap(bm);

        //show the id:
        TextView idView = (TextView)result.findViewById(R.id.textViewFragmentId);
        idView.setText("ID=" + id);

        // get the delete button, and add a click listener:
        Button finishButton = (Button)result.findViewById(R.id.buttonHideFragment);
        finishButton.setOnClickListener( clk -> {

            //Tell the parent activity to remove
            parentActivity.getSupportFragmentManager().beginTransaction().remove(this).commit();
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
