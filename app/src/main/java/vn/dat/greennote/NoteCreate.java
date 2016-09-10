package vn.dat.greennote;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import vn.dat.greennote.DatabaseHandler.FullscreenIMAGE;
import vn.dat.greennote.DatabaseHandler.NoteDatabase;
import vn.dat.greennote.DatabaseHandler.VerifyStoragePermissions;
import vn.dat.greennote.NoteWidget.MyWidgetProvider;

/**
 * Created by Alone on 04/12/2016.
 */
public class NoteCreate extends AppCompatActivity {
    NoteDatabase db = new NoteDatabase(this);
    EditText edtTitle, edtContent;
    TextView txtDate;
    ImageView imgContent;
    String title, content;
    int id;
    byte[] img;
    Bitmap imgBitmap;
    String imgPath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true); // ẩn/hiện tiêu đề
        actionBar.setTitle("Note Create");
        actionBar.setDisplayHomeAsUpEnabled(true);   // hiện button icon

        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);
        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.viewBody);
        txtDate = (TextView) findViewById(R.id.txtDateNote);
        imgContent = (ImageView) findViewById(R.id.imgContent);

        imgBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.img_null);
        imgContent.setImageBitmap(imgBitmap);

        Bundle bundle = getIntent().getExtras();
        String createNote = bundle.getString("createNote");
        if (createNote.equals("NewImage")) {
            VerifyStoragePermissions.VerifyStoragePermissions(this);

            Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(image, 8888);
        }
        if (createNote.equals("PickImage")) {
            Intent pickIMG = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(pickIMG, 9999);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("MMM dd, yyyy, hh:mm a");
        String date = sdf.format(new Date());

        //Calendar c = Calendar.getInstance();
        //String date = c.get(Calendar.DAY_OF_MONTH) + "-" + (c.get(Calendar.MONTH) + 1) + "-" + c.get(Calendar.YEAR) + " , " + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE);
        txtDate.setText(date);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 8888 && resultCode == RESULT_OK) {
            imgBitmap = (Bitmap) data.getExtras().get("data");
            imgContent.setImageBitmap(imgBitmap);
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 9999 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            String filePath = cursor.getString(columnIndex);

            File file = new File(filePath);
            try {
                InputStream inputStream = new FileInputStream(file);
                imgBitmap = BitmapFactory.decodeStream(inputStream);
                imgContent.setImageBitmap(imgBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_note_create, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        title = edtTitle.getText().toString();
        content = edtContent.getText().toString();
        switch (item.getItemId()) {
            case R.id.itmCreateDone:
            case R.id.itmCreateSave:
                insert();
                break;
            case R.id.itmCreateClear:
                edtTitle.setText("");
                edtContent.setText("");
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void insert() {

        saveImage();
        db.execute("INSERT INTO tblNote VALUES(null,'" + edtTitle.getText() + "','" + edtContent.getText() + "','" + txtDate.getText() + "','','" + imgPath + "')");
        Toast.makeText(this, "Note has been Created !", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(MyWidgetProvider.UPDATE_MEETING_ACTION));

        onBackPressed();
    }

    public void saveImage() {

        SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyyHHmmss");
        String date = sdf.format(new Date());

        String name = "green_note_" + date + ".png";

        File filePath = Environment.getExternalStorageDirectory();
        File dir = new File(filePath.getAbsolutePath().toString() + "/GreenNote");
        dir.mkdirs();

        File file = new File(dir, name);

        try {
            OutputStream outputStream = new FileOutputStream(file);
            imgBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.flush();
            outputStream.close();

            //MediaStore.Images.Media.insertImage(getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        imgPath=file.toString();
    }

    public byte[] processIMG(ImageView img) {
        //Bitmap bmp = BitmapFactory.decodeResource(getResources(), R.drawable.chomuc);
        BitmapDrawable drawable = (BitmapDrawable) img.getDrawable();
        Bitmap bmp = drawable.getBitmap();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    public void viewImage(View view) {
        Intent fullScreenIntent = new Intent(view.getContext(), FullscreenIMAGE.class);
        img = processIMG(imgContent);
        fullScreenIntent.putExtra("imgFullScreen", img);
        fullScreenIntent.putExtra("id", id);
        startActivity(fullScreenIntent);
    }
}
