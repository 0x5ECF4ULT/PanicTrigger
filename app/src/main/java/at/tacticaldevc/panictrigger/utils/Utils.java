package at.tacticaldevc.panictrigger.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import at.tacticaldevc.panictrigger.R;
import at.tacticaldevc.panictrigger.contactList.Contact;

public class Utils {

    //contact related stuff
    public static String[] getContactGroups(Context c)
    {
        Set<String> groups = new HashSet<>();

        groups.add("General");

        for(String contactString : c.getSharedPreferences("conf", Context.MODE_PRIVATE).getStringSet(c.getString(R.string.var_numbers_notify), new HashSet<String>()))
        {
            groups.add(getContact(contactString).groupID);
        }

        return groups.toArray(new String[groups.size()]);
    }

    public static Contact getContact(String contactString)
    {
        String[] parts = contactString.split(";");
        Contact c = new Contact(
                (parts.length >= 2 ? parts[1] : ""),
                parts[0],
                (parts.length >= 3 ? parts[2] : "General")
        );
        return c;
    }

    public static Contact[] getContactsByGroup(String group, Context c)
    {
        Set<Contact> contacts = new HashSet<>();

        for(String contactString : c.getSharedPreferences("conf", Context.MODE_PRIVATE).getStringSet(c.getString(R.string.var_numbers_notify), new HashSet<String>()))
        {
            if(group.equals("General") || contactString.split(";")[2].equals(group))
                contacts.add(getContact(contactString));
        }

        return contacts.toArray(new Contact[contacts.size()]);
    }


    //permission related stuff
    public static String[] checkPermissions(Context c)
    {
        List<String> permissions = new ArrayList<>();
        permissions.add(Manifest.permission.RECEIVE_SMS);
        permissions.add(Manifest.permission.SEND_SMS);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.CALL_PHONE);
        permissions.add(Manifest.permission.INTERNET);
        permissions.add(Manifest.permission.ACCESS_NETWORK_STATE);
        permissions.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> permissionsToRequest = new ArrayList<>();
        for(String perm : permissions)
        {
            if(c.checkSelfPermission(perm) != PackageManager.PERMISSION_GRANTED)
                permissionsToRequest.add(perm);
        }

        return permissionsToRequest.toArray(new String[permissionsToRequest.size()]);
    }

    public static boolean onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {
        if(requestCode == 255 && grantResults.length > 0)
        {
            for (int grantResult : grantResults)
            {
                if(grantResult != PackageManager.PERMISSION_GRANTED)
                    return false;
            }
        }
        return true;
    }
}
