package com.uniquesys.qrgo.Clientes;

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


public class ClientesFragment extends Fragment {
    ArrayList<Bitmap> splittedBitmaps;
    ArrayList<String> splittedid;
    ArrayList<String> dados;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            splittedBitmaps = getArguments().getParcelableArrayList("lista");
            splittedid = getArguments().getStringArrayList("id");
            dados = getArguments().getStringArrayList("dados");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_clientes, container,
                false);



        ListView gridView = (ListView) rootView.findViewById(R.id.gridClientes);
        SplittedClientesImage adapter = new SplittedClientesImage(getActivity(), splittedBitmaps, splittedid,dados);
        gridView.setAdapter(adapter);
        return rootView;
    }


}
