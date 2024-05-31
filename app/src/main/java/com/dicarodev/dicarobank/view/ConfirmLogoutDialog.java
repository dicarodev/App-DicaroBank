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

public class ConfirmLogoutDialog extends DialogFragment {

    private TextView tvConfirmLogout;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.CustomDialogTheme);
        builder.setTitle(getString(R.string.tittle_logout_dialog));

        tvConfirmLogout = new TextView(requireActivity());
        tvConfirmLogout.setText(getString(R.string.tv_logout_dialog));
        tvConfirmLogout.setTextSize(16f);
        tvConfirmLogout.setTextColor(Color.WHITE);
        tvConfirmLogout.setPadding(70, 40, 16, 16);
        builder.setView(tvConfirmLogout);

        builder.setPositiveButton(R.string.dialog_positive_button, (dialog, which) -> {
            SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("myPreferences", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(requireActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().finish();
        });

        builder.setNegativeButton(R.string.dialog_negative_button, (dialog, which) -> dialog.dismiss());

        return builder.create();
    }
}
