package vn.dat.greennote.NoteMenu;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.ShareActionProvider;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;

import vn.dat.greennote.DatabaseHandler.AdapterNote;
import vn.dat.greennote.DatabaseHandler.Note;
import vn.dat.greennote.DatabaseHandler.NoteDatabase;
import vn.dat.greennote.NoteCreate;
import vn.dat.greennote.NoteEdit;
import vn.dat.greennote.R;

public class NoteList extends Fragment {

    protected NoteDatabase db;
    protected ListView lstNote;
    protected TextView txtResult;
    protected ArrayList<Note> arrayNote;
    protected AdapterNote adapterNote;
    protected View layoutList;
    int id;
    FloatingActionsMenu floatingActionsMenu;
    AlertDialog actions;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View note = inflater.inflate(R.layout.fragment_note_list, null);

        txtResult = (TextView) note.findViewById(R.id.txtResult);
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
                floatingActionsMenu.collapse();
            }
        });

        registerForContextMenu(lstNote);

        floatingActionsMenu = (FloatingActionsMenu) note.findViewById(R.id.floatActionMenu);
        floatingActionsMenu.startLayoutAnimation();
        final FloatingActionButton fabText = (FloatingActionButton) note.findViewById(R.id.fabCreateText);
        fabText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionsMenu.collapse();
                Intent create = new Intent(getContext(), NoteCreate.class);
                create.putExtra("createNote", "text");
                getActivity().startActivity(create);
            }
        });


        FloatingActionButton fabImage = (FloatingActionButton) note.findViewById(R.id.fabCreateCamera);
        fabImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                floatingActionsMenu.collapse();
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("Choose an Option");
                String[] options = {"Take a new Image", "Pick image from Galery"};
                builder.setItems(options, actionListener);
//                builder.setNegativeButton("Cancel", null);
                builder.create().show();
//
            }
        });
        layoutList = note.findViewById(R.id.layoutNoteList);

        FloatingActionButton fabRecord = (FloatingActionButton) note.findViewById(R.id.fabCreateRecord);
        fabRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(layoutList, "Note with record UnSupported", Snackbar.LENGTH_LONG).show();
            }
        });
        return note;
    }

    DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent crtimg = new Intent(getContext(), NoteCreate.class);

            switch (which) {
                case 0:
                    crtimg.putExtra("createNote", "NewImage");
                    getActivity().startActivity(crtimg);
                    break;
                case 1:
                    crtimg.putExtra("createNote", "PickImage");
                    getActivity().startActivity(crtimg);
                    break;
            }
        }
    };


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        Context context = getActivity().getApplicationContext();
        db = new NoteDatabase(context);
    }

    @Override
    public void onResume() {

        if (getData("SELECT * FROM tblNote").getCount() == 0) {
            arrayNote.clear();
            adapterNote.notifyDataSetChanged();
            txtResult.setText("It all begins with notes .!");
        } else {
            txtResult.setText("");
            //getData("SELECT * FROM tblNote");
        }
        db.closeDatabase();
        super.onResume();
    }

    public Cursor getData(String s) {

        arrayNote.clear();
        Cursor c = null;
        try {
            c = db.rawQuery(s);
            if (c != null) {
                int id;
                String title, content, date, localion;
                String getImageResource;
                while (c.moveToNext()) {
                    id = Integer.parseInt(c.getString(0).toString());
                    title = c.getString(1);
                    content = c.getString(2);
                    date = c.getString(3);
                    localion = c.getString(4);
                    getImageResource = c.getString(5);
                    Note tmp = new Note(id, title, content, date, localion, getImageResource);
                    arrayNote.add(0, tmp);
                }
                adapterNote.notifyDataSetChanged();
            }
        } catch (SQLiteException e) {
            e.printStackTrace();
            c.close();
            db.closeDatabase();
        }
        return c;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.mnu_note_main, menu);

        MenuItem shareItem = menu.findItem(R.id.itmMainShare);
        ShareActionProvider myShareActionProvider = (ShareActionProvider) MenuItemCompat
                .getActionProvider(shareItem);

        Intent myIntent = new Intent();
        myIntent.setAction(Intent.ACTION_SEND);
        myIntent.putExtra(Intent.EXTRA_TEXT, "Green Note là ứng dụng hoàn toàn miễn phí, ứng dụng nhằm phục vụ nhu cầu hàng ngày giúp ghi lại những khoẳng khắc đáng nhớ hay có thể ghi lại và sắp xếp công việc cho người sử dụng.");
        myIntent.setType("text/plain");
        myShareActionProvider.setShareIntent(myIntent);

        SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.itmMainSearch));
        searchView.setIconifiedByDefault(true);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (getData("SELECT * FROM tblNote WHERE title LIKE '%" + newText + "%' " +
                        "OR content LIKE '%" + newText + "%' OR date LIKE '%" + newText + "%'").getCount() == 0) {
                    txtResult.setText("No Help for the search.");
                } else {
                    txtResult.setText("");
                }
                db.closeDatabase();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.itmSortName:
                getData("SELECT * FROM tblNote ORDER BY title DESC");
                db.closeDatabase();
                Snackbar.make(layoutList, "Note list selected Sort by Name", Snackbar.LENGTH_LONG).show();
                break;
            case R.id.itmSortDate:
                getData("SELECT * FROM tblNote");
                Snackbar.make(layoutList, "Note list selected Sort by Date", Snackbar.LENGTH_LONG).show();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.context_list_action, menu);

        id = arrayNote.get(((AdapterView.AdapterContextMenuInfo) menuInfo).position).getId();
        String title = adapterNote.getItem(((AdapterView.AdapterContextMenuInfo) menuInfo).position).getTitle();

        menu.setHeaderTitle(title);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.itmItemEdit:
                Intent note = new Intent(getContext(), NoteEdit.class);
                note.putExtra("id", id);
                startActivity(note);
                break;
            case R.id.itmItemDelete:
                AlertDialog.Builder delete = new AlertDialog.Builder(getContext());
                delete.setMessage("Delete this item ?");
                delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        db.execute("DELETE FROM tblNote WHERE id=" + id);
                        onResume();
                        Toast.makeText(getContext(), "Note has been Deteted !", Toast.LENGTH_SHORT).show();

                    }
                });
                delete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                delete.create().show();
                break;
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public Animation onCreateAnimation(int transit, boolean enter, int nextAnim) {
        return super.onCreateAnimation(transit, enter, nextAnim);
    }
}
