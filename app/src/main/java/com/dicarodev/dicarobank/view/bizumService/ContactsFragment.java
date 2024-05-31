package com.dicarodev.dicarobank.view.bizumService;

import android.content.ContentResolver;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dicarodev.dicarobank.R;
import com.dicarodev.dicarobank.adapter.ContactsRecyclerViewAdapter;
import com.dicarodev.dicarobank.adapter.TransactionsRecyclerViewAdapter;
import com.dicarodev.dicarobank.databinding.FragmentContactsBinding;
import com.dicarodev.dicarobank.model.account.AccountUserDto;
import com.dicarodev.dicarobank.model.bizum.Bizum;
import com.dicarodev.dicarobank.model.contact.Contact;
import com.dicarodev.dicarobank.model.transaction.TransactionDto;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class ContactsFragment extends Fragment {

    private FragmentContactsBinding binding;
    private NavController navController;
    private Bizum bizum;
    private static ArrayList<Contact> contactsList;
    private RecyclerView rvContacts;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentContactsBinding.inflate(inflater, container, false);
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

        if (getArguments() != null) {
            bizum = (Bizum) getArguments().getSerializable("bizum");
        }

        rvContacts = binding.contactsRv;
        contactsList = findContacts();
        configureContactsRV(contactsList, bizum, navController);
    }

    // Metodo para recuperar los contactos
    private ArrayList<Contact> findContacts() {
        contactsList = new ArrayList<>();
        // Define las columnas que se desean recuperar de la tabla de contactos
        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER
        };

        // Define la condición de selección para obtener solo los contactos con direcciones de correo electrónico
        String filter = ContactsContract.Data.MIMETYPE + " = ?";
        String[] filterArgs = {ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE};

        String sortOrder = ContactsContract.Data.DISPLAY_NAME + " ASC"; // Define el orden de clasificación de los resultados

        // Obtiene el ContentResolver para realizar la consulta
        ContentResolver contentResolver = requireContext().getContentResolver();
        // Realizar la consulta a la tabla de datos de contactos
        Cursor cursor = contentResolver.query(
                ContactsContract.Data.CONTENT_URI,
                projection,
                filter,
                filterArgs,
                sortOrder
        );

        // Procesar los resultados de la consulta
        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                // Obtener las columnas específicas de cada contacto
                //String contactId = cursor.getString(0);
                String name = cursor.getString(1);
                String email = cursor.getString(2);

                // Inicializa y crea un contacto nuevo y lo añade a la lista de contactos para usar luego en el RecyclerView
                Contact contact = new Contact(name, email);
                contactsList.add(contact);

            }
            cursor.close();
        }

        return contactsList;
    }

    private void configureContactsRV(List<Contact> contactList, Bizum bizum, NavController navController) {
        //transactionDtoList.sort(Comparator.comparing(TransactionDto::getTransactionDate).reversed());
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rvContacts.setLayoutManager(layoutManager);
        ContactsRecyclerViewAdapter contactsRecyclerViewAdapter = new ContactsRecyclerViewAdapter(contactList, bizum, navController);
        rvContacts.setAdapter(contactsRecyclerViewAdapter);
    }
}