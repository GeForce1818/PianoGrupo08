package sv.edu.catolica.pianogrupo08;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public abstract class MenuActivity extends AppCompatActivity {

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    private void showChangePianoDialog() {
        final String[] opciones = {
                "Piano Tradicional",
                "Piano Infantil de la Selva",
                "Piano de Instrumentos"
        };

        new AlertDialog.Builder(this)
                .setTitle("Cambiar Tipo de Piano")
                .setItems(opciones, (dialog, which) -> {
                    Class<?> target = null;
                    switch (which) {
                        case 0:
                            target = PianoTradicionalActivity.class;
                            break;
                        case 1:
                            target = PianoAnimales.class;
                            break;
                        case 2:
                            target = PianoInstrumentosActivity.class;
                            break;
                    }

                    if (target != null && !getClass().equals(target)) {
                        startActivity(new Intent(this, target));
                    }
                })
                .show();
    }

    private void exitApp() {
        finishAffinity();
        System.exit(0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_change_piano) {
            showChangePianoDialog();
            return true;
        } else if (id == R.id.action_about) {
            startActivity(new Intent(this, AcercaDeActivity.class));
            return true;
        } else if (id == R.id.action_exit) {
            exitApp();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
