package sv.edu.catolica.pianogrupo08;

import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.HashMap;

public class PianoInstrumentosActivity extends MenuActivity {

    private SoundPool soundPool;
    private final HashMap<String, Integer> sounds = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instrumentos);

        AudioAttributes attrs = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_MEDIA)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();

        soundPool = new SoundPool.Builder()
                .setMaxStreams(7)
                .setAudioAttributes(attrs)
                .build();

        // Cargar sonidos
        sounds.put("Guitarra", soundPool.load(this, R.raw.guitarra, 1));
        sounds.put("Violín", soundPool.load(this, R.raw.violin, 1));
        sounds.put("Flauta", soundPool.load(this, R.raw.flauta, 1));
        sounds.put("Trompeta", soundPool.load(this, R.raw.trompeta, 1));
        sounds.put("Batería", soundPool.load(this, R.raw.bateria, 1));
        sounds.put("Piano", soundPool.load(this, R.raw.piano, 1));
        sounds.put("Saxofón", soundPool.load(this, R.raw.saxofon, 1));

        // Vincular botones
        bindInstrument(R.id.key_guitarra, "Guitarra");
        bindInstrument(R.id.key_violin, "Violín");
        bindInstrument(R.id.key_flauta, "Flauta");
        bindInstrument(R.id.key_trompeta, "Trompeta");
        bindInstrument(R.id.key_bateria, "Batería");
        bindInstrument(R.id.key_piano, "Piano");
        bindInstrument(R.id.key_saxofon, "Saxofón");
    }

    private void bindInstrument(int viewId, String name) {
        ImageButton btn = findViewById(viewId);
        if (btn != null) {
            btn.setOnClickListener(v -> {
                Integer soundId = sounds.get(name);
                if (soundId != null) {
                    int streamId = soundPool.play(soundId, 1f, 1f, 1, 0, 1f);
                    // limitar a 2s
                    btn.postDelayed(() -> soundPool.stop(streamId), 1000);
                }
                Toast.makeText(this, "Reproduciendo: " + name, Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        soundPool.autoPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        soundPool.release();
    }
}
