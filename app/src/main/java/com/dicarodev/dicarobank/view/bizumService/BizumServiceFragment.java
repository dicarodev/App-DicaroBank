package com.dicarodev.dicarobank.view.bizumService;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.databinding.FragmentBizumServiceBinding;
import com.dicarodev.dicarobank.model.bizum.Bizum;

import java.util.Objects;

public class BizumServiceFragment extends Fragment {

    private FragmentBizumServiceBinding binding;
    private NavController navController;
    private EditText etBizumAmount, etBizumMessage;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentBizumServiceBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onStart() {
        super.onStart();

        navController = Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_content_main);

        etBizumAmount = binding.bizumAmountEt;
        etBizumMessage = binding.bizumMessageEt;
        Button btnSearchContact = binding.searchContactBtn;
        btnSearchContact.setOnClickListener(v -> createBizum());

        clearFields();
    }

    private void createBizum() {
        String bizumAmount = etBizumAmount.getText().toString().trim();
        String bizumMessage = etBizumMessage.getText().toString().trim();

        double amount = !bizumAmount.isEmpty() ? Double.parseDouble(bizumAmount) : 0;

        if (bizumAmount.isEmpty()) {
            etBizumAmount.setError(getString(R.string.required_field));
            etBizumAmount.requestFocus();
        } else if (amount < 0.5 || amount > 300) {
            etBizumAmount.setError(getString(R.string.bizumService_incorrect_amount));
            etBizumAmount.setText("");
            etBizumAmount.requestFocus();
        } else {
            Bizum bizum = new Bizum(amount, bizumMessage, null);
            Bundle bundleContacts = new Bundle();
            bundleContacts.putSerializable("bizum", bizum);
            navController.navigate(R.id.nav_contacts, bundleContacts);
        }
    }

    private void clearFields() {
        binding.bizumAmountEt.setText("");
        binding.bizumMessageEt.setText("");
    }
}