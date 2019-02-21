package at.tacticaldevc.panictrigger.contactList;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import at.tacticaldevc.panictrigger.R;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactItem> {
    private List<Contact> list;

    public ContactAdapter(List<Contact> list)
    {
        this.list = list;
    }

    public ContactItem onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ContactItem(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.content_list_item, viewGroup, false));
    }

    public void onBindViewHolder(ContactItem contactItem, int i) {
        Contact ci = list.get(i);
        contactItem.name.setText(ci.name);
        contactItem.number.setText(ci.number);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ContactItem extends RecyclerView.ViewHolder
    {
        public TextView name, number;

        public ContactItem(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            number = itemView.findViewById(R.id.number);
        }
    }

}
