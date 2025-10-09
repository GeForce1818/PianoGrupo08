package sv.edu.catolica.pianogrupo08;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class PianoTradicionalActivity extends MenuActivity {

    private SoundPool soundPool;
    private SparseIntArray soundMap;
    private boolean soundsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        setupPianoKeys();
        initializeSoundPool();
    }

    private void initializeSoundPool() {
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(7)
                .setAudioAttributes(audioAttributes)
                .build();

        soundPool.setOnLoadCompleteListener((sp, sampleId, status) -> {
            if (status == 0) {
                soundsLoaded = true;
            } else {
                Toast.makeText(PianoTradicionalActivity.this, "Error al cargar un sonido", Toast.LENGTH_SHORT).show();
            }
        });

        soundMap = new SparseIntArray();
        soundMap.put(R.raw.nota_do, soundPool.load(this, R.raw.nota_do, 1));
        soundMap.put(R.raw.nota_re, soundPool.load(this, R.raw.nota_re, 1));
        soundMap.put(R.raw.nota_mi, soundPool.load(this, R.raw.nota_mi, 1));
        soundMap.put(R.raw.nota_fa, soundPool.load(this, R.raw.nota_fa, 1));
        soundMap.put(R.raw.nota_sol, soundPool.load(this, R.raw.nota_sol, 1));
        soundMap.put(R.raw.nota_la, soundPool.load(this, R.raw.nota_la, 1));
        soundMap.put(R.raw.nota_si, soundPool.load(this, R.raw.nota_si, 1));
    }


    private void setupPianoKeys() {
        MaterialCardView teclaDo = findViewById(R.id.tecla_Do);
        teclaDo.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_do, "Do");
        });

        MaterialCardView teclaRe = findViewById(R.id.tecla_Re);
        teclaRe.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_re, "Re");
        });

        MaterialCardView teclaMi = findViewById(R.id.tecla_Mi);
        teclaMi.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_mi, "Mi");
        });

        MaterialCardView teclaFa = findViewById(R.id.tecla_Fa);
        teclaFa.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_fa, "Fa");
        });

        MaterialCardView teclaSol = findViewById(R.id.tecla_Sol);
        teclaSol.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_sol, "Sol");
        });

        MaterialCardView teclaLa = findViewById(R.id.tecla_La);
        teclaLa.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_la, "La");
        });

        MaterialCardView teclaSi = findViewById(R.id.tecla_Si);
        teclaSi.setOnClickListener(v -> {
            animateKeyPress(v);
            playSound(R.raw.nota_si, "Si");
        });
    }

    private void animateKeyPress(View v) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).withEndAction(() -> {
            v.animate().scaleX(1f).scaleY(1f).setDuration(50);
        });
    }

    private void playSound(int soundResource, String noteName) {
        if (soundsLoaded) {
            int soundId = soundMap.get(soundResource);
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            Toast.makeText(this, "Nota: " + noteName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Sonidos aún no cargados", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
    }
}
