package com.example.lab3_20190212;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class Acelerometro extends Fragment {

    private RecyclerView recyclerView;
    private Adaptador adaptador;
    private List<Lista> acele;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_acelerometro, container, false);

        recyclerView = view.findViewById(R.id.vistaa);
        acele = new ArrayList<>();
        adaptador = new Adaptador(acele, getContext());
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            recyclerView.setAdapter(adaptador);
        }

        return view;
    }

    public void recibirParametros(List<Lista> lista) {
        acele = lista;
    }


}