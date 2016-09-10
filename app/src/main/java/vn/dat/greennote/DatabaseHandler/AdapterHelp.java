package vn.dat.greennote.DatabaseHandler;

import android.app.Activity;
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
 * Created by Alone on 04/25/2016.
 */
public class AdapterHelp extends ArrayAdapter<Help> {
    ArrayList<Help> listHelp;
    Activity context;
    int layout;

    public AdapterHelp(Activity context, int resource, ArrayList<Help> objects) {
        super(context, resource, objects);
        this.context = context;
        listHelp = objects;
        layout = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=context.getLayoutInflater();
        View v=inflater.inflate(R.layout.custom_list_item_help, null);

        ImageView imgHelp=(ImageView)v.findViewById(R.id.imgHelp);
        TextView txtTitle=(TextView)v.findViewById(R.id.txtTitleHelp);
        txtTitle.setText(listHelp.get(position).getTitle());

        TextView txtContent=(TextView)v.findViewById(R.id.txtContentHelp);
        txtContent.setText(listHelp.get(position).getContent());

//        Bitmap img = BitmapFactory.decodeByteArray(listHelp.get(position).getImage(), 0, listHelp.get(position).getImage().length);
        File file=new File(listHelp.get(position).getImgResource());
        try {
            InputStream inputStream = new FileInputStream(file);
            imgHelp.setImageBitmap(BitmapFactory.decodeStream(inputStream));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        imgHelp.setImageBitmap(BitmapFactory.decodeFile(listHelp.get(position).getImgResource()));
        return v;
    }
}
