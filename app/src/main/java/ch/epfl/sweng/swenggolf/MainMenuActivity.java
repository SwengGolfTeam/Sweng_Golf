package ch.epfl.sweng.swenggolf;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.TextView;

import ch.epfl.sweng.sweng_golf.R;

public class MainMenuActivity extends AppCompatActivity {
    private FirebaseAccount account;

    @Override
    protected void onCreate(Bundle savedInstances) {
        super.onCreate(savedInstances);
        setContentView(R.layout.activity_main_menu);
        android.support.v7.widget.Toolbar tb = findViewById(R.id.toolbar);
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
        if(textValue != null && textField != null){
            textField.setText(textValue);
            return true;
        }else{
            return false;
        }
    }

    private boolean setUserName(){
        return setValue(account.getName(), (TextView) findViewById(R.id.username));
    }

    private boolean setUserMail(){
        return setValue(account.getId(), (TextView) findViewById(R.id.usermail));
    }

    private void setUserPic(){
        //Picasso.with(this).load(account.getPhotoUrl()).error(android.R.drawable.btn_dialog).into((ImageView) findViewById(R.id.userpic));
    }

    /*private boolean setUserPic() {
        ImageView userpic = findViewById(R.id.userpic);
        Drawable pic = null;
        try {
            pic = getUserPic();
        } catch (IOException e){
            throw new IllegalArgumentException(e.getMessage());
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
        Drawable d;
        try {
            up = new FileInputStream(new File(userpicuri.toString()));
            d = Drawable.createFromStream(up,"userpicsrc");
        } catch (IOException ignored) {
            return null;
        } finally {
            if(up != null){up.close();}
        }
        return d;
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu_toolbar_main,menu);
        return true;
    }
}
