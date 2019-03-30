package com.sellersync.jantz.sellersync;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 */
public class AddProductFragment extends Fragment {
    Button btnNew;
    Button btnRestock;


    public AddProductFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final FragmentManager fm = getActivity().getSupportFragmentManager();
        btnNew = (Button) view.findViewById(R.id.btnNewProduct);
        btnRestock = (Button) view.findViewById(R.id.btnRestock);

        btnNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new NewProductFragment(), "NEWPRODUCT").commit();
                MainActivity.tvPageTitle.setText(getString(R.string.new_product));

            }
        });
        btnRestock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fm.beginTransaction().replace(R.id.containerView, new RestockFragment(), "RESTOCKPRODUCT").commit();
                MainActivity.tvPageTitle.setText(getString(R.string.restock));
            }
        });
    }
}