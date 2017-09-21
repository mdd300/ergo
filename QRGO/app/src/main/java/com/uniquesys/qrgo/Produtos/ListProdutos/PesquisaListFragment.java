package com.uniquesys.qrgo.Produtos.ListProdutos;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import com.uniquesys.qrgo.Produtos.GridProdutos.PesquisaImageAdapter;
import com.uniquesys.qrgo.R;

import java.util.ArrayList;


public class PesquisaListFragment extends Fragment {

    ArrayList<Bitmap> splittedBitmaps;
    ArrayList<String> splittedid;
    ArrayList<String> Contatos;
    String user;
    String hash;
    String res;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            splittedBitmaps = getArguments().getParcelableArrayList("lista");
            splittedid = getArguments().getStringArrayList("id");
            Contatos = getArguments().getStringArrayList("Contatos");
            user = getArguments().getString("user");
            hash = getArguments().getString("hash");
            res = getArguments().getString("resultado");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_list_prod, container,
                false);

        rootView.clearFocus();

        ListView gridView = (ListView) rootView.findViewById(R.id.ListView_Prods);
        PesquisaImageListProd adapter = new PesquisaImageListProd(getActivity(),user,hash,Contatos, splittedBitmaps, splittedid,res);

        gridView.setAdapter(adapter);
        gridView.setSelection(adapter.getCount() - 1);

        return rootView;
    }

}
