package com.example.elementaryapp2.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.elementaryapp2.R;
import com.example.elementaryapp2.services.Services;

public class SettingsFragment extends Fragment {

    EditText ipInput;
    Button saveBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_settings, container, false);

        ipInput = v.findViewById(R.id.ipInput);
        saveBtn = v.findViewById(R.id.save_btn);
        ipInput.setText(Services.ipAddress);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveIp();
            }
        });

        return v;
    }

    public void saveIp() {
        Services.updateIpKey(requireContext(), ipInput.getText().toString());
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
    }
}