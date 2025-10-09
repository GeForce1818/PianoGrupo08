package sv.edu.catolica.pianogrupo08;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.material.card.MaterialCardView;
import java.util.HashMap;

public class PianoInstrumentosActivity extends MenuActivity {

    private SoundPool soundPool;
    private final HashMap<String, Integer> sounds = new HashMap<>();
    private final HashMap<String, Long> lastPlayTime = new HashMap<>();
    private static final long MIN_TIME_BETWEEN_SOUNDS = 300; // 300ms mínimo entre sonidos

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrumentos);

        initializeSoundPool();
        loadSounds();
        bindInstruments();
    }

    private void initializeSoundPool() {
        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(7)
                .setAudioAttributes(attrs)
                .build();
    }

    private void loadSounds() {
        // Cargar sonidos
        sounds.put("Guitarra", soundPool.load(this, R.raw.guitarra, 1));
        sounds.put("Violín", soundPool.load(this, R.raw.violin, 1));
        sounds.put("Flauta", soundPool.load(this, R.raw.flauta, 1));
        sounds.put("Trompeta", soundPool.load(this, R.raw.trompeta, 1));
        sounds.put("Batería", soundPool.load(this, R.raw.bateria, 1));
        sounds.put("Piano", soundPool.load(this, R.raw.piano, 1));
        sounds.put("Saxofón", soundPool.load(this, R.raw.saxofon, 1));

        // Inicializar tiempos de reproducción
        for (String instrument : sounds.keySet()) {
            lastPlayTime.put(instrument, 0L);
        }
    }

    private void bindInstruments() {
        // Vincular instrumentos usando MaterialCardView
        bindInstrument(R.id.key_guitarra, "Guitarra");
        bindInstrument(R.id.key_violin, "Violín");
        bindInstrument(R.id.key_flauta, "Flauta");
        bindInstrument(R.id.key_trompeta, "Trompeta");
        bindInstrument(R.id.key_bateria, "Batería");
        bindInstrument(R.id.key_piano, "Piano");
        bindInstrument(R.id.key_saxofon, "Saxofón");
    }

    private void bindInstrument(int viewId, String name) {
        MaterialCardView card = findViewById(viewId);
        if (card != null) {
            card.setOnClickListener(v -> {
                // Prevenir spam de clicks
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastPlayTime.get(name) < MIN_TIME_BETWEEN_SOUNDS) {
                    return;
                }
                lastPlayTime.put(name, currentTime);

                // Animación y reproducción
                animateKeyPress(v);
                playSound(name);
                showInstrumentName(name);
            });
        }
    }

    private void animateKeyPress(View v) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).withEndAction(() -> {
            v.animate().scaleX(1f).scaleY(1f).setDuration(50);
        });
    }

    private void playSound(String name) {
        Integer soundId = sounds.get(name);
        if (soundId != null && soundId > 0) {
            soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
        }
    }

    private void showInstrumentName(String name) {
        Toast.makeText(this, "Reproduciendo: " + name, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (soundPool != null) {
            soundPool.autoResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (soundPool != null) {
            soundPool.autoPause();
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
}