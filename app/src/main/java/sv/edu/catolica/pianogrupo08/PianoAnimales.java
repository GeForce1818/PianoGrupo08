package sv.edu.catolica.pianogrupo08;

import android.content.Intent;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Bundle;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

public class PianoAnimales extends MenuActivity {

    private SoundPool soundPool;
    private SparseIntArray soundMap;
    private boolean soundsLoaded = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_piano_animales);
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
                Toast.makeText(PianoAnimales.this, "Error al cargar un sonido de animal", Toast.LENGTH_SHORT).show();
            }
        });

        soundMap = new SparseIntArray();
        soundMap.put(R.raw.leon, soundPool.load(this, R.raw.leon, 1));
        soundMap.put(R.raw.mono, soundPool.load(this, R.raw.mono , 1));
        soundMap.put(R.raw.elefante, soundPool.load(this, R.raw.elefante, 1));
        soundMap.put(R.raw.pajaro, soundPool.load(this, R.raw.pajaro, 1));
        soundMap.put(R.raw.rana, soundPool.load(this, R.raw.rana, 1));
        soundMap.put(R.raw.lobo, soundPool.load(this, R.raw.lobo, 1));
        soundMap.put(R.raw.delfin, soundPool.load(this, R.raw.delfin, 1));
    }

    private void setupPianoKeys() {
        MaterialCardView teclaDo = findViewById(R.id.tecla_leon);
        if (teclaDo != null) {
            teclaDo.setOnClickListener(v -> {
                animateKeyPress(v);
                playSound(R.raw.leon, "León");
            });
        }

        MaterialCardView teclaRe = findViewById(R.id.tecla_mono);
        if (teclaRe != null) {
            teclaRe.setOnClickListener(v -> {
                animateKeyPress(v);
                playSound(R.raw.mono, "Mono");
            });
        }

        MaterialCardView teclaMi = findViewById(R.id.tecla_elefante);
        if (teclaMi != null) {
            teclaMi.setOnClickListener(v -> {
                animateKeyPress(v);
                playSound(R.raw.elefante, "Elefante");
            });
        }

        MaterialCardView teclaFa = findViewById(R.id.tecla_pajaro);
        if (teclaFa != null) {
            teclaFa.setOnClickListener(v -> {
                animateKeyPress(v);
                playSound(R.raw.pajaro, "Pájaro");
            });
        }

        MaterialCardView teclaSol = findViewById(R.id.tecla_rana);
        if (teclaSol != null) {
            teclaSol.setOnClickListener(v -> {
                animateKeyPress(v);
                playSound(R.raw.rana, "Rana");
            });
        }

        MaterialCardView teclaLa = findViewById(R.id.tecla_Lobo);
        if (teclaLa != null) {
            teclaLa.setOnClickListener(v -> {
                animateKeyPress(v);
                playSound(R.raw.lobo, "Lobo");
            });
        }

        MaterialCardView teclaSi = findViewById(R.id.tecla_delfin);
        if (teclaSi != null) {
            teclaSi.setOnClickListener(v -> {
                animateKeyPress(v);
                playSound(R.raw.delfin, "Delfín");
            });
        }
    }

    private void animateKeyPress(View v) {
        v.animate().scaleX(0.95f).scaleY(0.95f).setDuration(50).withEndAction(() -> {
            v.animate().scaleX(1f).scaleY(1f).setDuration(50);
        });
    }

    private void playSound(int soundResource, String animalName) {
        if (soundsLoaded) {
            int soundId = soundMap.get(soundResource);
            soundPool.play(soundId, 1.0f, 1.0f, 1, 0, 1.0f);
            Toast.makeText(this, "Animal: " + animalName, Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.action_change_piano) {
            showChangePianoDialog();
            return true;
        } else if (itemId == R.id.action_about) {
            Intent intent = new Intent(this, AcercaDeActivity.class);
            startActivity(intent);
            return true;
        } else if (itemId == R.id.action_exit) {
            exitApp();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void showChangePianoDialog() {
        final String[] pianoTypes = {"Piano Tradicional", "Piano Infantil de la Selva", "Piano de Instrumentos Musicales"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecciona un tipo de piano");
        builder.setItems(pianoTypes, (dialog, which) -> {

            if (which == 0) {
                Toast.makeText(this, "Cambiando al Piano Tradicional...", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(PianoAnimales.this, PianoTradicionalActivity.class);
                startActivity(intent);
                finish();

            } else if (which == 1) {
                Toast.makeText(this, "Ya estás en el Piano de Animales", Toast.LENGTH_SHORT).show();

            } else if (which == 2) {
                // Opción 3: Cambiar a Piano de Instrumentos
                Toast.makeText(this, "Cambiando al Piano de Instrumentos...", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(PianoAnimales.this, PianoInstrumentos.class);
                //startActivity(intent);
                finish();
            }
        });
        builder.show();
    }


    private void exitApp() {
        if (soundPool != null) {
            soundPool.release();
            soundPool = null;
        }
        finishAffinity();
    }
}
