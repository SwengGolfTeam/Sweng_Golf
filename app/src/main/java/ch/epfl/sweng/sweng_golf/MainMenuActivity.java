package ch.epfl.sweng.sweng_golf;

import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class MainMenuActivity extends AppCompatActivity {
    private GoogleSignInAccount account;

    @Override
    protected void onCreate(Bundle savedInstances){
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main_menu);
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        TextView username = findViewById(R.id.username);
        TextView usermail = findViewById(R.id.usermail);
        ImageView userpic = findViewById(R.id.userpic);
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String name = account.getDisplayName();
            String mail = account.getEmail();
            if(name != null){username.setText(name);}
            if(mail != null){usermail.setText(mail);}
            Uri userpicuri = account.getPhotoUrl();
            if(userpicuri != null){
                Drawable d = userpic.getDrawable();
                Drawable dup = null;
                try {
                    FileInputStream up = new FileInputStream(new File(userpicuri.toString()));
                    dup = Drawable.createFromStream(up,"userpicsrc");
                } catch (IOException ignored) {}
                userpic.setImageDrawable(dup == null ? d : dup);
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar_main,menu);
        return true;
    }
}
