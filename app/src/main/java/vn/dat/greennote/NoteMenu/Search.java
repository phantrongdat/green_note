package vn.dat.greennote.NoteMenu;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import vn.dat.greennote.DatabaseHandler.AdapterNote;
import vn.dat.greennote.DatabaseHandler.Note;
import vn.dat.greennote.NoteEdit;
import vn.dat.greennote.R;

/**
 * Created by Alone on 04/16/2016.
 */
public class Search extends NoteList{
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View note = inflater.inflate(R.layout.fragment_search, null);
        txtResult = (TextView) note.findViewById(R.id.txtResult);
        layoutList = note.findViewById(R.id.layoutNoteList);
        arrayNote = new ArrayList<Note>();
        adapterNote = new AdapterNote(getContext(), R.layout.custom_list_item_note, arrayNote);

        lstNote = (ListView) note.findViewById(R.id.lstNote);
        lstNote.setAdapter(adapterNote);

        lstNote.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent note = new Intent(getContext(), NoteEdit.class);
                note.putExtra("id", arrayNote.get(position).getId());
                startActivity(note);
            }
        });
        registerForContextMenu(lstNote);

        return note;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        Snackbar.make(layoutList, "Welcome to Note Search", Snackbar.LENGTH_LONG).show();
        inflater.inflate(R.menu.mnu_note_main, menu);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.itmMainSearch));
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (getData("SELECT * FROM tblNote WHERE title LIKE '%" + newText + "%' OR content LIKE '%" + newText + "%'").getCount() == 0) {
                    txtResult.setText("No result for search");
                } else {
                    txtResult.setText("");
                }
                db.closeDatabase();
                return false;
            }
        });
        //super.onCreateOptionsMenu(menu, inflater);
    }
}
