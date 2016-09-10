package vn.dat.greennote.DatabaseHandler;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import vn.dat.greennote.R;

/**
 * Created by Alone on 04/11/2016.
 */
public class AdapterNote extends ArrayAdapter<Note> {
    ArrayList<Note> arrayNote=new ArrayList<Note>();
    Activity context;
    int layout;
    public AdapterNote(Context context, int resource, ArrayList<Note> objects) {
        super(context, resource, objects);
        this.context= (Activity) context;
        arrayNote=objects;
        layout=resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View noteView=inflater.inflate(layout, null);
        TextView titleView =(TextView)noteView.findViewById(R.id.txtTitleView);
        TextView contentView =(TextView)noteView.findViewById(R.id.txtContentView);
        TextView dateView =(TextView)noteView.findViewById(R.id.txtDateView);
        ImageView imgContent=(ImageView)noteView.findViewById(R.id.imgContent);

        titleView.setText(arrayNote.get(position).getTitle());
        contentView.setText(arrayNote.get(position).getContent());
        dateView.setText(arrayNote.get(position).getDate());

        File file=new File(arrayNote.get(position).getImgResource());
        try {
            InputStream inputStream = new FileInputStream(file);
            imgContent.setImageBitmap(BitmapFactory.decodeStream(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return noteView;
    }
}
