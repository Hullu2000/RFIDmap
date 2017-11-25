package paikannus.hackjunction.com.rfid_p;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.view.WindowManager;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class mainActivity extends Activity {
    private List<persons> personsList = new ArrayList<>();
    private RecyclerView recyclerView;
    private adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

       // Intent i = new Intent(this, bitMap.class);
        //startActivity(i);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new adapter(personsList, this);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        preparepersonsData();
    }
    private void preparepersonsData() {
        persons persons = new persons("Matti ", "Koivula ky", "200m");
        personsList.add(persons);

        persons = new persons("Petri", "Haukionkala OY", "500m");
        personsList.add(persons);

        persons = new persons("Maija", "Maijala OY", "100m");
        personsList.add(persons);

        persons = new persons("Maria", "Maijala OY", "5m<");
        personsList.add(persons);

        persons = new persons("Marja", "Haukionkala OY", "out of range");
        personsList.add(persons);

        persons = new persons("Mikko", "Kuusila tmi.", "100m");
        personsList.add(persons);

        persons = new persons("Yi", "Kuusila tmi", "5m<");
        personsList.add(persons);

        persons = new persons("Kalle Kuusila", "Kuusila tmi", "100m");
        personsList.add(persons);

        mAdapter.notifyDataSetChanged();
    }
}