package at.tacticaldevc.panictrigger;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.tacticaldevc.panictrigger.contactList.Contact;
import at.tacticaldevc.panictrigger.contactList.ContactAdapter;

public class ContactPickerActivity extends AppCompatActivity {

    private RecyclerView rv;
    private SharedPreferences prefs;
    private String mode;

    private List<Contact> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Pick contacts");

        prefs = getSharedPreferences("conf", MODE_PRIVATE);
        mode = getIntent().getDataString();

        list = new ArrayList<>();
        rv = findViewById(R.id.contactList);
        rv.setAdapter(new ContactAdapter(list));
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setItemAnimator(new DefaultItemAnimator());

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent contacts = new Intent(Intent.ACTION_PICK) //Something doesn't seem to work... Disabling this for now
                        .setType(ContactsContract.Contacts.CONTENT_TYPE);
                startActivityForResult(contacts, 0);*/

                final EditText input = new EditText(ContactPickerActivity.this);
                AlertDialog.Builder builder = new AlertDialog.Builder(ContactPickerActivity.this);
                input.setInputType(InputType.TYPE_CLASS_PHONE);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        aa.add(input.getText().toString());
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }
        });

        //Import data from shared prefs
        for(String number : prefs.getStringSet(mode, new HashSet<String>()))
        {
            aa.add(number);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_contactpicker, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Set<String> newValues = new HashSet<>();
        for(int i = 0; i < rv.getAdapter().getItemCount(); i++)
        {
            newValues.add((String) lv.getAdapter().getItem(i));
        }
        prefs.edit().putStringSet(getString(R.string.var_numbers_trigger), newValues).putStringSet(getString(R.string.var_numbers_notify), newValues).apply();
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        Set<String> newValues = new HashSet<>();
        for(int i = 0; i < rv.getAdapter().getItemCount(); i++)
        {
            newValues.add((String) lv.getAdapter().getItem(i));
        }
        prefs.edit().putStringSet(mode, newValues).apply();
    }

    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 0 && resultCode == RESULT_OK)
        {
            Uri contact = data.getData();
            Cursor c = getContentResolver().query(contact, null, null, null, null);
            c.moveToFirst();
            Toast.makeText(this, c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE)), Toast.LENGTH_LONG).show();
        }
    }*/
}
