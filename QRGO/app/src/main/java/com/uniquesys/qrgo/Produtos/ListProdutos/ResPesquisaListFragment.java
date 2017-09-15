package com.uniquesys.qrgo.Produtos.ListProdutos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.uniquesys.qrgo.Produtos.GridProdutos.ResPesquisaImageAdapter;
import com.uniquesys.qrgo.R;

import java.util.ArrayList;


public class ResPesquisaListFragment extends Fragment {

    ArrayList<Bitmap> splittedBitmaps;
    ArrayList<String> splittedid;
    ArrayList<String> Contatos;
    String user;
    String hash;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            splittedBitmaps = getArguments().getParcelableArrayList("lista");
            splittedid = getArguments().getStringArrayList("id");
            Contatos = getArguments().getStringArrayList("Contatos");
            user = getArguments().getString("user");
            hash = getArguments().getString("hash");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_prod, container,
                false);

        rootView.clearFocus();

        ListView gridView = (ListView) rootView.findViewById(R.id.ListView_Prods);
        PesquisaImageListProdRes adapter = new PesquisaImageListProdRes(getActivity(),user,hash,Contatos, splittedBitmaps, splittedid);

        gridView.setAdapter(adapter);

        return rootView;
    }

}
