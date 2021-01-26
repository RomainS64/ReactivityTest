package tempdereaction;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.tempderaction.R;

public class MainActivity extends AppCompatActivity {

    Thread THtempAttente,THtempReaction;
    Button click;
    boolean momentDeClicker,fin,estClicke;
    int timerStart,timerClick;
    int tempReaction=99999;
    Intent k;
    SharedPreferences.Editor editor;
    SharedPreferences manager;
    @Override

    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        manager=PreferenceManager.getDefaultSharedPreferences(this);
        editor = manager.edit();

        setContentView(com.example.tempderaction.R.layout.activity_main);
        getSupportActionBar().hide();
    }
    protected void onStart() {
        super.onStart();

        momentDeClicker=false;
        fin=false;
        estClicke=false;
        click = (Button) findViewById(R.id.click);

        k = new Intent(MainActivity.this, Menu.class);

        THtempAttente = new Thread(
                new Runnable() {
                    public void run() {
                        tempAttente();
                    }
                });
        THtempReaction = new Thread(
                new Runnable() {
                    public void run() {
                        tempReaction();
                    }
                });

        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click();
            }
        });

        THtempAttente.start();
        THtempReaction.start();
    }
    private void click(){
        if (momentDeClicker) {
            estClicke=true;
            momentDeClicker = false;
            click.setBackgroundColor(Color.BLACK);
            Log.e("MAIN", "affichage temps reaction reaction");
            while(tempReaction==0);
            click.setText("Reactivity time\n " + tempReaction + "ms");
            fin = true;
        } else if (fin) {
            Log.e("MAIN", "[RELANCE]");
            if(manager.getInt("score",10000)>tempReaction){
                editor.putInt("score",tempReaction);
                editor.commit();
            }
            startActivity(k);
        } else {
            click.setBackgroundColor(Color.RED);
            click.setText("TOO SOON !");
            fin = true;


        }
    }
    private void tempAttente() {
        click.setBackgroundColor(Color.BLACK);
        click.setText("Ready ?");
        click.setTextColor(Color.WHITE);
        timerStart =(int)System.currentTimeMillis();
        timerStart+=(int)(4+Math.random()*7)*1000;

        while(!fin){

            if(timerStart<(int)System.currentTimeMillis()){

                click.setBackgroundColor(Color.RED);
                click.setText("TAP !");
                Log.e("MAIN","tempAttente: moment de clicker = true");

                momentDeClicker=true;

                break;
            }
        }
    }
    private void tempReaction() {
        Log.e("MAIN", "tempReaction: Start");
        boolean exMomentClicke=false;
        while(!fin){
            if(momentDeClicker && !estClicke && !exMomentClicke){
                Log.e("MAIN", "tempReaction: Start du tier reaction");
                timerClick = (int) System.currentTimeMillis();
                exMomentClicke=true;

            }
            if(estClicke){
                Log.e("MAIN", "tempReaction: Click");
                tempReaction = (int) System.currentTimeMillis() - timerClick;
                break;
            }
        }



    }
}