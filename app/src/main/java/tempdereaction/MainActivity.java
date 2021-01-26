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

import java.util.concurrent.Semaphore;

public class MainActivity extends AppCompatActivity {
    Button click;
    Thread THtempAttente,THtempReaction;
    int tempReaction=99999;
    boolean momentDeClicker,fin,estClicke;
    int timerStart,timerClick;
    Intent k;
    SharedPreferences.Editor editor;
    SharedPreferences manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        manager = PreferenceManager.getDefaultSharedPreferences(this);
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
                        tempsAttente();
                    }
                });
        THtempReaction = new Thread(
                new Runnable() {
                    public void run() {
                        tempsReaction();
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
    //click s'active à chaques fois que l'utilisater tap sur l'écran
    private void click(){

        if (momentDeClicker) {

            estClicke=true;
            momentDeClicker = false;
            click.setBackgroundColor(Color.BLACK);

            //Vérifier que le thread a bien calculé le temp de reaction
            try {
                wait();
            }catch (Throwable e){
                e.printStackTrace();
            }
            click.setText("Reactivity time\n " + tempReaction + "ms");
            fin = true;

        } else if (fin) {
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
    //tempAttente permet de determiner le temps qu'il faudra patienter et d'attendre durant celui-ci
    private void tempsAttente() {
        click.setBackgroundColor(Color.BLACK);
        click.setText("Ready ?");
        click.setTextColor(Color.WHITE);

        //initialisation du moment auquel il faudra cliquer
        timerStart =(int)System.currentTimeMillis();
        timerStart+=(int)(4+Math.random()*7)*1000;

        //tant que l'utilisateur n'a pas cliqué
        while(!fin){

            if(timerStart<(int)System.currentTimeMillis()){

                click.setBackgroundColor(Color.RED);
                click.setText("TAP !");
                momentDeClicker=true;
                break;
            }
        }
    }
   //tempReaction permet de déterminer le temp de réaction de l'utilisateur
    private void tempsReaction(){
        boolean premierMomentClick=true;
        while(!fin){
            //s'il s'agit du debut du calcul du temp de reaction...
            if(momentDeClicker && !estClicke && premierMomentClick){
                timerClick = (int) System.currentTimeMillis();
                premierMomentClick=false;
            }
            if(estClicke){
                tempReaction = (int) System.currentTimeMillis() - timerClick;
                break;
            }
        }
        synchronized (this){
            notifyAll();
        }

    }
}