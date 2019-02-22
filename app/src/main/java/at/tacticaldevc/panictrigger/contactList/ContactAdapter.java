package at.tacticaldevc.panictrigger.contactList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.tacticaldevc.panictrigger.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactItem> {
    private List<Contact> list;

    private View.OnClickListener ocl = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //to be implemented: edit functionality
        }
    };
    private View.OnLongClickListener olcl = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(final View v) {
            AlertDialog.Builder adb = new AlertDialog.Builder(v.getContext());
            adb.setTitle("Delete?");
            adb.setMessage("Would you really like to delete this entry?");
            adb.setPositiveButton("", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    for (Contact c : list)
                    {
                        if(c.number.equals(((TextView) v.findViewById(R.id.contact_number)).getText().toString()))
                        {
                            list.remove(c);
                            notifyDataSetChanged();
                            break;
                        }
                    }
                }
            });
            return true;
        }
    };

    public ContactAdapter(List<Contact> list)
    {
        this.list = list;
    }

    public ContactItem onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list_item, viewGroup, false);
        view.setOnClickListener(ocl);
        view.setOnLongClickListener(olcl);
        return new ContactItem(view);
    }

    public void onBindViewHolder(ContactItem contactItem, int i) {
        Contact ci = list.get(i);
        contactItem.name.setText(ci.name);
        contactItem.number.setText(ci.number);
        contactItem.c = ci;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContactItem extends RecyclerView.ViewHolder
    {
        public TextView name, number;
        public Contact c;
        public View v;

        public ContactItem(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
        }
    }

}
