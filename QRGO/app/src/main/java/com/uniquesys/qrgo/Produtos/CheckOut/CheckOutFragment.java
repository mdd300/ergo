package com.uniquesys.qrgo.Produtos.CheckOut;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.uniquesys.qrgo.R;

import java.util.ArrayList;


public class CheckOutFragment extends Fragment {
    ArrayList<Bitmap> splittedBit;
    String user;
    String hash;
    String data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            splittedBit = getArguments().getParcelableArrayList("lista");
            data = getArguments().getString("data");
            user = getArguments().getString("user_id");
            hash = getArguments().getString("hash");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_check_out, container,
                false);

        ListView gridView = (ListView) rootView.findViewById(R.id.CheckOutProd);
        SplittedListCheckOut adapter = new SplittedListCheckOut(getActivity(),splittedBit, data,user,hash);
        gridView.setAdapter(adapter);

        return rootView;
    }


}
