package com.example.loginpage;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class AccountPage extends Fragment {

    private Button logout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_account, container, false);

        final Intent intent = new Intent(getActivity(), MainActivity.class);
        logout = (Button) view.findViewById(R.id.logoutButton);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
//                Toast.makeText(getActivity(), "It worked!", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
