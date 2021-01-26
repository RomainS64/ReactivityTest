package tempdereaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tempderaction.R;

public class Menu extends AppCompatActivity {

    Button start;
    TextView score;
    Intent k,p;
    ImageView param;
    SharedPreferences myPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        getSupportActionBar().hide();

        start =(Button)findViewById(R.id.start);
        score = findViewById(R.id.score);
        param = findViewById(R.id.param);

        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        k = new Intent(this,MainActivity.class);
        p = new Intent(this,Param.class);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(k);
            }
        });
        param.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(p);
            }
        });

    }
    protected void onStart(){
        super.onStart();
        int valScore= myPreferences.getInt("score",1000);
        if(valScore==1234){
            score.setText("no score...");
        }else{
            score.setText(valScore+"ms");
        }
    }
}