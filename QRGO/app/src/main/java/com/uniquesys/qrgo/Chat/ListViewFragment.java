package com.uniquesys.qrgo.Chat;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uniquesys.qrgo.Chat.splittedImageAdapterList;
import com.uniquesys.qrgo.R;

import java.util.ArrayList;


public class ListViewFragment extends Fragment {
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

        View rootView = inflater.inflate(R.layout.fragment_list_view, container,
                false);

        ListView listView = (ListView) rootView.findViewById(R.id.ListViewChat);
        splittedImageAdapterList adapter = new splittedImageAdapterList(getActivity(), splittedBitmaps, splittedid);
        listView.setAdapter(adapter);
        return rootView;
    }


}
