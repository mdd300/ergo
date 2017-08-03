package com.uniquesys.qrgo.Chat;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.uniquesys.qrgo.R;

import java.util.ArrayList;


public class ConversaFragment extends Fragment {
    ArrayList<String> splittedMensagem;
    ArrayList<String> layout;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            splittedMensagem = getArguments().getStringArrayList("mensagem");
            layout = getArguments().getStringArrayList("layout");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_conversa, container,
                false);

        ListView listView = (ListView) rootView.findViewById(R.id.ListViewMensagem);
        SplittedListAdapter adapter = new SplittedListAdapter(getActivity(), splittedMensagem, layout);
        listView.setAdapter(adapter);
        return rootView;
    }

}
