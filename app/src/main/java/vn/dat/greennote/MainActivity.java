package vn.dat.greennote;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import vn.dat.greennote.DatabaseHandler.NoteDatabase;

public class MainActivity extends AppCompatActivity {
    NoteDatabase db = new NoteDatabase(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        dbConfig();
    }


    public void dbConfig() {
        // image BLOB
        db.execute("CREATE TABLE IF NOT EXISTS Note (id INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL , title NVARCHAR(50) NOT NULL , content NVARCHAR(1000) NOT NULL , date NVARCHAR(30), location NVARCHAR(50))");
//        db.execute("INSERT INTO Note VALUES(null,'TEST TITLE','Database','','')");
        db.execute("CREATE TABLE IF NOT EXISTS Account (id VARCHAR(15) PRIMARY KEY   NOT NULL , password VARCHAR(50) NOT NULL)");
        if (db.rawQuery("SELECT*FROM Account WHERE id='admin'").getCount() == 0) {
            db.execute("INSERT INTO Account VALUES('admin','123')");
        }
        db.closeDatabase();
    }

    public void skip(View view) {
        Intent edit = new Intent(this, NoteMain.class);
        startActivity(edit);
    }

    public void login(View view) {
        if (checkAccount()) {
            Toast.makeText(this, "Success !", Toast.LENGTH_SHORT).show();
            Intent edit = new Intent(this, NoteMain.class);
            startActivity(edit);
        } else {
            AlertDialog.Builder al = new AlertDialog.Builder(MainActivity.this);
            al.setTitle("Error !");
            al.setMessage("User or password Incorrect !");
            al.create().show();
        }
    }

    public boolean checkAccount() {
        EditText edtUser = (EditText) findViewById(R.id.edtUser);
        EditText edtPassword = (EditText) findViewById(R.id.edtPassword);
        String user = null;
        String password = null;
        Cursor account = db.rawQuery("SELECT*FROM Account");
        while (account.moveToNext()) {
            user = account.getString(0);
            password = account.getString(1);
            if (user.equalsIgnoreCase(edtUser.getText().toString()) && password.equals(edtPassword.getText().toString())) {
                return true;
            }
        }
        db.closeDatabase();
        return false;
    }
}
