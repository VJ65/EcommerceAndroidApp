package com.solodroid.ecommerce;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class SwipeTabFragment extends Fragment {

    private int tab;
    private int color;
    private int image;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        
        image = bundle.getInt("image");
        tab = bundle.getInt("tab");
        color = bundle.getInt("color");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.swipetab_fragment, null);
        
        ImageView tabImage = (ImageView) view.findViewById(R.id.imageView);
        tabImage.setImageResource(image);
        
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(tab);
        

        
        view.setBackgroundResource(color);
        return view;
    }
}