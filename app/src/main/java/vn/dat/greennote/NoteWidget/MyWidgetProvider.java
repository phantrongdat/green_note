package vn.dat.greennote.NoteWidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.RemoteViews;

import vn.dat.greennote.DatabaseHandler.NoteDatabase;
import vn.dat.greennote.NoteCreate;
import vn.dat.greennote.NoteEdit;
import vn.dat.greennote.NoteMain;
import vn.dat.greennote.R;

/**
 * Created by Alone on 04/22/2016.
 */
public class MyWidgetProvider extends AppWidgetProvider {

    public static final String UPDATE_MEETING_ACTION = "android.appwidget.action.APPWIDGET_UPDATE";

    public static final String EXTRA_ITEM = "vn.dat.greennote.EXTRA_ITEM";


    public void onReceive(Context context, Intent intent) {

        AppWidgetManager mgr = AppWidgetManager.getInstance(context);

        if (intent.getAction().equals(UPDATE_MEETING_ACTION)) {

            int appWidgetIds[] = mgr.getAppWidgetIds(new ComponentName(context, MyWidgetProvider.class));

            Log.e("received", intent.getAction());

            for (int id : appWidgetIds) {
                mgr.notifyAppWidgetViewDataChanged(id, R.id.lstNoteWidget);
            }
        }

        super.onReceive(context, intent);
    }

    public void onUpdate(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds)
    {
        NoteDatabase db = new NoteDatabase(context);

        // update each of the app widgets with the remote adapter

        for (int i = 0; i < appWidgetIds.length; ++i) {


            // Set up the intent that starts the ListViewService, which will

            // provide the views for this collection.

            Intent intent = new Intent(context, ListViewWidgetService.class);

            // Add the app widget ID to the intent extras.

            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetIds[i]);

            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

            // Instantiate the RemoteViews object for the app widget layout.

            RemoteViews rv = new RemoteViews(context.getPackageName(), R.layout.widget_main);

            Cursor c = db.rawQuery("SELECT * FROM tblNote");
            rv.setTextViewText(R.id.txtNotes, c.getCount() + "");

            // Set up the RemoteViews object to use a RemoteViews adapter.

            // This adapter connects

            // to a RemoteViewsService  through the specified intent.

            // This is how you populate the data.

            rv.setRemoteAdapter(appWidgetIds[i], R.id.lstNoteWidget, intent);

            // Trigger listview item click

            Intent startActivityIntent = new Intent(context, NoteEdit.class);

            PendingIntent startActivityPendingIntent = PendingIntent.getActivity(context, 0, startActivityIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            rv.setPendingIntentTemplate(R.id.lstNoteWidget, startActivityPendingIntent);

            // The empty view is displayed when the collection has no items.

            // It should be in the same layout used to instantiate the RemoteViews  object above.

            rv.setEmptyView(R.id.lstNoteWidget, R.id.empty_view);

            //

            // Do additional processing specific to this app widget...



            Intent createNote = new Intent(context, NoteCreate.class);
//            createNote.putExtra("createNote", "text");
//            PendingIntent penTEXT = PendingIntent.getActivity(context, 0, createNote, PendingIntent.FLAG_UPDATE_CURRENT);
//            rv.setOnClickPendingIntent(R.id.imbText, penTEXT);

            createNote.putExtra("createNote", "text");
            PendingIntent penIMG = PendingIntent.getActivity(context, 0, createNote, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.imbCamera, penIMG);

            Intent HOME = new Intent(context, NoteMain.class);
            PendingIntent penHOME = PendingIntent.getActivity(context, 0, HOME, PendingIntent.FLAG_UPDATE_CURRENT);
            rv.setOnClickPendingIntent(R.id.imbHome, penHOME);
//            rv.setOnClickPendingIntent(R.id.imbSearch, penHOME);

            appWidgetManager.updateAppWidget(appWidgetIds[i], rv);

        }


        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }


}