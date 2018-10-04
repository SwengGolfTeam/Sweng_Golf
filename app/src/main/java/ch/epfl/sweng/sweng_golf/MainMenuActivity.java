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
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main_menu);
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
        setSupportActionBar(tb);
        setUserDisplay();
    }

    private boolean setUserDisplay() {
        account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            boolean name = setUserName();
            boolean mail = setUserMail();
            boolean pic = setUserPic();
            return name & mail & pic;
        }else{
            return false;
        }
    }

    private boolean setUserName(){
        TextView username = findViewById(R.id.username);
        String name = account.getDisplayName();
        if(name != null){
            username.setText(name);
            return true;
        }
        return false;
    }

    private boolean setUserMail(){
        TextView usermail = findViewById(R.id.usermail);
        String mail = account.getEmail();
        if(mail != null){
            usermail.setText(mail);
            return true;
        }
        return false;
    }

    private boolean setUserPic() {
        ImageView userpic = findViewById(R.id.userpic);
        Drawable pic = null;
        try {
            pic = getUserPic();
        } catch (IOException e){
            throw new RuntimeException(e.getMessage());
        }
        if(pic != null){
            userpic.setImageDrawable(pic);
            return true;
        }else{
            return false;
        }
    }

    private Drawable getUserPic() throws IOException {
        Uri userpicuri = account.getPhotoUrl();
        if(userpicuri == null){return null;}
        FileInputStream up = null;
        Drawable d = null;
        try {
            up = new FileInputStream(new File(userpicuri.toString()));
            d = Drawable.createFromStream(up,"userpicsrc");
        } catch (IOException ignored) {
            return null;
        } finally {
            if(up != null){up.close();}
        }
        return d;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar_main,menu);
        return true;
    }
}
