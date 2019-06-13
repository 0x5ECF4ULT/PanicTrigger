package at.tacticaldevc.panictrigger.contactList;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.Duration;

import at.tacticaldevc.panictrigger.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactItem> {
    private List<Contact> list;

    public ContactAdapter(List<Contact> list)
    {
        this.list = list;
    }

    public ContactItem onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list_item, viewGroup, false);
        return new ContactItem(view);
    }

    public void onBindViewHolder(ContactItem contactItem, int i) {
        Contact ci = list.get(i);
        contactItem.name.setText(ci.name);
        contactItem.number.setText(ci.number);
        contactItem.group.setText(ci.groupID);
        contactItem.c = ci;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Set<String> getGroups()
    {
        Set<String> groups = new HashSet<>();

        groups.add("General");

        for(Contact c : list)
        {
            groups.add(c.groupID);
        }

        return groups;
    }

    public class ContactItem extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener
    {
        public TextView name, number, group;
        public Contact c;
        public View v;

        public ContactItem(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
            group = itemView.findViewById(R.id.group);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(v.getContext(), "Clicked!", Toast.LENGTH_LONG).show();
        }

        @Override
        public boolean onLongClick(View v) {
            new AlertDialog.Builder(v.getContext())
                .setTitle("Delete?")
                .setMessage("Would you really like to delete this entry?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int i = list.indexOf(c);
                        list.remove(c);
                        notifyItemRemoved(i);
                    }
                })
                .setNegativeButton("No", null)
                .show();
            return true;
        }
    }

}
