package vn.dat.greennote.NoteWidget;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import vn.dat.greennote.DatabaseHandler.Note;
import vn.dat.greennote.DatabaseHandler.NoteDatabase;
import vn.dat.greennote.R;

/**
 * Created by Alone on 04/22/2016.
 */
public class ListViewWidgetService extends RemoteViewsService {


    public RemoteViewsService.RemoteViewsFactory onGetViewFactory(Intent intent) {

        return new ListViewRemoteViewsFactory(this.getApplicationContext(), intent);

    }

}

class ListViewRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context mContext;

    private ArrayList<Note> notes;


    public ListViewRemoteViewsFactory(Context context, Intent intent) {

        mContext = context;

    }

    // Initialize the data set.

    public void onCreate() {

        // In onCreate() you set up any connections / cursors to your data source. Heavy lifting,

        // for example downloading or creating content etc, should be deferred to onDataSetChanged()

        // or getViewAt(). Taking more than 20 seconds in this call will result in an ANR.
        NoteDatabase db = new NoteDatabase(mContext);
        notes = new ArrayList<Note>();
        Cursor c = db.rawQuery("SELECT * FROM tblNote");
//        int total=c.getCount();
        int id;
        String title, content, date, localion;
        String getImage;

        while (c.moveToNext()) {
            id = Integer.parseInt(c.getString(0).toString());
            title = c.getString(1).toString();
            content = c.getString(2).toString();
            date = c.getString(3).toString();
            localion = c.getString(4).toString();
            getImage = c.getString(5);
            Note tmp = new Note(id, title, content, date, localion, getImage);
            notes.add(0, tmp);
        }



    }

    // Given the position (index) of a WidgetItem in the array, use the item's text value in

    // combination with the app widget item XML file to construct a RemoteViews object.


    public RemoteViews getViewAt(int position) {

        // position will always range from 0 to getCount() - 1.

        // Construct a RemoteViews item based on the app widget item XML file, and set the

        // text based on the position.

        RemoteViews rv = new RemoteViews(mContext.getPackageName(), R.layout.custom_list_item_widget);

        // feed row


        int id = notes.get(position).getId();
        String title = notes.get(position).getTitle();
        rv.setTextViewText(R.id.txtTitleWidget, title);

        String date = notes.get(position).getDate();
        rv.setTextViewText(R.id.txtDateWidget, date);

        String content = notes.get(position).getContent();
        rv.setTextViewText(R.id.txtContentWidget, content);

        File file=new File(notes.get(position).getImgResource());
        try {
            InputStream inputStream = new FileInputStream(file);
            Bitmap img = BitmapFactory.decodeStream(inputStream);
            rv.setImageViewBitmap(R.id.imgContentWidget, img);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // end feed row

        // Next, set a fill-intent, which will be used to fill in the pending intent template

        // that is set on the collection view in ListViewWidgetProvider.

        Bundle extras = new Bundle();

        extras.putInt(MyWidgetProvider.EXTRA_ITEM, position);

        Intent fillInIntent = new Intent();

        fillInIntent.putExtra("id", id);

        fillInIntent.putExtras(extras);

        // Make it possible to distinguish the individual on-click

        // action of a given item

        rv.setOnClickFillInIntent(R.id.txtTitleWidget, fillInIntent);
        rv.setOnClickFillInIntent(R.id.txtDateWidget, fillInIntent);
        rv.setOnClickFillInIntent(R.id.txtContentWidget, fillInIntent);
        rv.setOnClickFillInIntent(R.id.imgContentWidget, fillInIntent);


        // Return the RemoteViews object.
        return rv;

    }

    public int getCount() {

        Log.e("size=", notes.size() + "");
        return notes.size();

    }

    public void onDataSetChanged() {
        // Fetching JSON data from server and add them to records arraylist
    }

    public int getViewTypeCount() {

        return 1;

    }

    public long getItemId(int position) {

        return position;

    }

    public void onDestroy() {

        notes.clear();

    }

    public boolean hasStableIds() {

        return true;

    }

    public RemoteViews getLoadingView() {

        return null;

    }

}