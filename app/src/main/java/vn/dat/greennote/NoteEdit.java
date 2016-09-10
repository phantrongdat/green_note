package vn.dat.greennote;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
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
import vn.dat.greennote.NoteWidget.MyWidgetProvider;

/**
 * Created by Alone on 04/11/2016.
 */
public class NoteEdit extends AppCompatActivity {
    NoteDatabase db = new NoteDatabase(this);
    EditText edtTitle, edtContent;
    TextView txtDate;
    ImageView imgContent;
    String title, content;
    int id;
    String imgPath;
    Typeface fontDate, fontContent;
    Bitmap imgBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        android.support.v7.app.ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(true); // ẩn/hiện tiêu đề
        actionBar.setTitle("Note Edit");
        actionBar.setDisplayHomeAsUpEnabled(true);   // hiện button icon
        actionBar.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_mtrl_am_alpha);

        fontDate = Typeface.createFromAsset(getAssets(), "font/VTIMESN0.TTF");
        fontContent = Typeface.createFromAsset(getAssets(), "font/VTIMESN0.TTF");

        edtTitle = (EditText) findViewById(R.id.edtTitle);
        edtContent = (EditText) findViewById(R.id.viewBody);
//        edtContent.setTypeface(fontContent);

        txtDate = (TextView) findViewById(R.id.txtDateNote);
//        txtDate.setTypeface(fontDate);

        imgContent = (ImageView) findViewById(R.id.imgContent);

        Bundle bd = getIntent().getExtras();
        id = bd.getInt("id");
        loadData(id);
    }

    public void loadData(int id) {
        Cursor c = db.rawQuery("SELECT * FROM tblNote WHERE id=" + id);
        c.moveToNext();
        edtTitle.setText(c.getString(1));
        edtContent.setText(c.getString(2));

        txtDate.setText(c.getString(3));

//        img=c.getBlob(5);
        imgPath = c.getString(5);

        File file = new File(imgPath);
        try {
            InputStream inputStream = new FileInputStream(file);
            imgBitmap = BitmapFactory.decodeStream(inputStream);
            imgContent.setImageBitmap(imgBitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        db.closeDatabase();
    }

    @Override
    public void onBackPressed() {
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_in_left);
        super.onBackPressed();
    }

    public void viewImage(View view) {
        Intent fullScreenIntent = new Intent(view.getContext(), FullscreenIMAGE.class);
        fullScreenIntent.putExtra("imgFullScreen", processIMG(imgContent));
        startActivity(fullScreenIntent);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.mnu_note_edit, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        title = edtTitle.getText().toString();
        content = edtContent.getText().toString();
        switch (item.getItemId()) {
            case R.id.itmEditDone:
            case R.id.itmEditSave:
                update();
                break;
            case R.id.itmEditDelete:
                delete();
                break;
            case R.id.itmAddImage:
                addImage();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void addImage() {
        android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(this);
        builder.setTitle("Choose an Option");
        String[] options = {"Take a new Image", "Pick image from Galery", "Remove image"};
        builder.setItems(options, actionListener);
//                builder.setNegativeButton("Cancel", null);
        builder.create().show();
    }

    DialogInterface.OnClickListener actionListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            Intent crtimg = new Intent(getApplicationContext(), NoteCreate.class);

            switch (which) {
                case 0:
                    Intent image = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(image, 8888);
                    break;
                case 1:
                    Intent pickIMG = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickIMG, 9999);
                    break;
                case 2:
                    imgBitmap = BitmapFactory.decodeResource(getApplication().getResources(),
                            R.drawable.img_null);
                    imgContent.setImageBitmap(imgBitmap);
                    saveImage();
                    break;
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 8888 && resultCode == RESULT_OK) {
            imgBitmap = (Bitmap) data.getExtras().get("data");
            imgContent.setImageBitmap(imgBitmap);
            saveImage();
            super.onActivityResult(requestCode, resultCode, data);
        }
        if (requestCode == 9999 && resultCode == RESULT_OK) {
            Uri uri = data.getData();
            String[] projection = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(projection[0]);
            imgPath = cursor.getString(columnIndex);

            File file = new File(imgPath);
            try {
                InputStream inputStream = new FileInputStream(file);
                imgBitmap = BitmapFactory.decodeStream(inputStream);
                imgContent.setImageBitmap(imgBitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
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
        imgPath = file.toString();
    }

    public void update() {

        db.execute("UPDATE tblNote SET title='" + title + "',content='" + content + "',imgResource='" + imgPath + "'  WHERE id=" + id);
        Toast.makeText(this, "Note has been Updated !", Toast.LENGTH_SHORT).show();
        sendBroadcast(new Intent(MyWidgetProvider.UPDATE_MEETING_ACTION));
        onBackPressed();
    }

    public void delete() {
        AlertDialog.Builder delete = new AlertDialog.Builder(this);
        delete.setMessage("Delete this item ?");
        delete.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.execute("DELETE FROM tblNote WHERE id=" + id);
                Toast.makeText(NoteEdit.this, "Note has been Deteted !", Toast.LENGTH_SHORT).show();
                sendBroadcast(new Intent(MyWidgetProvider.UPDATE_MEETING_ACTION));
                onBackPressed();
            }
        });
        delete.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        delete.create().show();
    }


    public static class LineEditText extends EditText {
        // we need this constructor for LayoutInflater
        public LineEditText(Context context, AttributeSet attrs) {
            super(context, attrs);
            mRect = new Rect();
            mPaint = new Paint();
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setColor(Color.GRAY);
        }

        private Rect mRect;
        private Paint mPaint;

        @Override
        protected void onDraw(Canvas canvas) {

            int height = getHeight();
            int line_height = getLineHeight();

            int count = height / line_height;

            if (getLineCount() > count)
                count = getLineCount();

            Rect r = mRect;
            Paint paint = mPaint;
            int baseline = getLineBounds(0, r);

            for (int i = 0; i < count; i++) {

                canvas.drawLine(r.left, baseline + 1, r.right, baseline + 1, paint);
                baseline += getLineHeight();

                super.onDraw(canvas);
            }

        }
    }
}
