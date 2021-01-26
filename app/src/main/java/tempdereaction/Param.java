package tempdereaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import com.example.tempderaction.R;

public class Param extends AppCompatActivity {

    Button reboot;
    SharedPreferences.Editor editor;
    SharedPreferences manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.param);

        manager= PreferenceManager.getDefaultSharedPreferences(this);
        editor = manager.edit();
        reboot = findViewById(R.id.reboot);

        getSupportActionBar().hide();
        reboot.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                editor.putInt("score",1234);
                editor.commit();
            }
        });
    }
}