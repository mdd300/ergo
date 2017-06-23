package com.uniquesys.qrgo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;

import java.util.ArrayList;


public class GridFragment extends Fragment {
    ArrayList<Bitmap> splittedBitmaps;
    ArrayList<String> splittedid;

    private String mParam1;
    private String mParam2;


    public GridFragment newInstance(String param1, String param2) {
        GridFragment fragment = new GridFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

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
            SplittedImageAdapter adapter = new SplittedImageAdapter(getActivity(),getActivity(), splittedBitmaps, splittedid);


            gridView.setAdapter(adapter);

            return rootView;
    }


}
