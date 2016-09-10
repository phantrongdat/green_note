package vn.dat.greennote.NoteMenu;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.dat.greennote.DatabaseHandler.AdapterHelp;
import vn.dat.greennote.DatabaseHandler.NoteDatabase;
import vn.dat.greennote.R;

/**
 * Created by Alone on 04/16/2016.
 */
public class Help extends Fragment {
    NoteDatabase db;
    ListView lstHelp;
    TextView txtHelpResult;
    ArrayList<vn.dat.greennote.DatabaseHandler.Help> list;
    AdapterHelp adapterHelp;

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_help, null);
        txtHelpResult=(TextView)v.findViewById(R.id.txtHelpResult);
        lstHelp = (ListView) v.findViewById(R.id.lstHelp);
        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        db = new NoteDatabase(getActivity().getApplicationContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        getHelp("SELECT * FROM tblHelp");
    }

    public Cursor getHelp(String s) {
        Cursor c = db.rawQuery(s);
        list=new ArrayList<>();
        adapterHelp=new AdapterHelp(getActivity(),R.layout.custom_list_item_help,list);
        lstHelp.setAdapter(adapterHelp);
        String title,content;
        String getImage;
        while (c.moveToNext())
        {
            title = c.getString(1).toString();
            content = c.getString(2).toString();
            getImage = c.getString(0);
            vn.dat.greennote.DatabaseHandler.Help tmp = new vn.dat.greennote.DatabaseHandler.Help(getImage,title, content);
            list.add(tmp);
        }
        adapterHelp.notifyDataSetChanged();
        return c;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.mnu_note_help, menu);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.itmMainSearch));
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (getHelp("SELECT * FROM tblHelp WHERE title LIKE '%" + newText + "%' " +
                        "OR content LIKE '%" + newText + "%'").getCount() == 0) {
                    txtHelpResult.setText("No result for the search.");
                } else {
                    txtHelpResult.setText("");
                }
                db.closeDatabase();

                return false;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }
}
