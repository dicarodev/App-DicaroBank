package com.dicarodev.dicarobank.view;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.dicarodev.dicarobank.R;

public class WelcomeDialog extends DialogFragment {

    private TextView tvWelcome;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomDialogTheme);
        builder.setTitle(getString(R.string.tittle_welcome_dialog));

        tvWelcome = new TextView(requireActivity());
        tvWelcome.setText(getString(R.string.tv_welcome_dialog));
        tvWelcome.setTextSize(16f);
        tvWelcome.setTextColor(Color.WHITE);
        tvWelcome.setPadding(70, 40, 16, 16);
        builder.setView(tvWelcome);

        builder.setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> {
            Intent loginIntent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(loginIntent);
            requireActivity().finish();
        });

        return builder.create();
    }
}