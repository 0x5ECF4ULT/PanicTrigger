package at.tacticaldevc.panictrigger;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

public class ContactPickerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_picker);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final ListView lv = findViewById(R.id.contactsList);
        final ArrayAdapter<String> aa = new ArrayAdapter<>(this, R.layout.activity_listview);
        lv.setAdapter(aa);

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
