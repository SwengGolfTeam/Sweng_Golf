package ch.epfl.sweng.swenggolf;

import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MainMenuActivity extends AppCompatActivity {
    private FirebaseAccount account;
    private View nav;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main_menu);
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
        nav = ((NavigationView)(this.findViewById(R.id.drawer))).getHeaderView(0);
        setSupportActionBar(tb);
        setUserDisplay();
    }

    private boolean setUserDisplay() {
        account = FirebaseAccount.getCurrentUserAccount();
        if(account != null){
            boolean name = setUserName();
            boolean mail = setUserMail();
            setUserPic();
            return name & mail;
        }else{
            return false;
        }
    }

    private boolean setValue(String textValue, TextView textField){
        if(textValue != null && !textValue.equals("") && textField != null){
            textField.setText(textValue);
            return true;
        }else{
            return false;
        }
    }

    private boolean setUserName(){
        return setValue(account.getName(), (TextView) nav.findViewById(R.id.username));
    }

    private boolean setUserMail(){
        return setValue(account.getId(), (TextView) nav.findViewById(R.id.usermail));
    }

    private void setUserPic(){
        Picasso.with(this).load(account.getPhotoUrl()).error(android.R.drawable.btn_dialog).into((ImageView) nav.findViewById(R.id.userpic));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar_main,menu);
        return true;
    }
}
