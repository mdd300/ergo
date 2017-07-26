package com.uniquesys.qrgo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.util.ArrayList;


public class ResFragment extends Fragment {
    ArrayList<Bitmap> splittedBitmaps;
    ArrayList<String> splittedid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            splittedBitmaps = getArguments().getParcelableArrayList("lista");
            splittedid = getArguments().getStringArrayList("id");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_grid, container,
                false);



        GridView gridView = (GridView) rootView.findViewById(R.id.gridProdutos);
        SplittedImageAdapterRes adapter = new SplittedImageAdapterRes(getActivity(), splittedBitmaps, splittedid);
        gridView.setAdapter(adapter);
        return rootView;
    }


}
