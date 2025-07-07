package com.example.elementaryapp2.services;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.elementaryapp2.R;

public class SettingsDialogFragment extends DialogFragment {

    private EditText ipInput;
    private Button saveBtn;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.TransparentAlertDialog);
        View view = getLayoutInflater().inflate(R.layout.settings_dialog_layout, null);
        builder.setView(view)
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss());

        // Initialize UI components here
        ipInput = view.findViewById(R.id.ipInput);
        saveBtn = view.findViewById(R.id.save_btn);

        ipInput.setText(Services.ipAddress); // Set the current IP address

        saveBtn.setOnClickListener(v -> saveIp());

        return builder.create();
    }


    private void saveIp() {
        String newIp = ipInput.getText().toString();
        Services.updateIpKey(requireContext(), newIp);
        Toast.makeText(getContext(), "Saved", Toast.LENGTH_SHORT).show();
    }
}
