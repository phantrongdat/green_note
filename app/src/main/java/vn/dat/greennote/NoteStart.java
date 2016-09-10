package vn.dat.greennote;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class NoteStart extends AppCompatActivity {

    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_start);

    }

    @Override
    protected void onStart() {
        countDownTimer = new CountDownTimer(2000, 100) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                startActivity(new Intent(getApplicationContext(), NoteMain.class));
            }
        };
        countDownTimer.start();
        super.onStart();
    }
}
