package ch.epfl.sweng.swenggolf;

import ch.epfl.sweng.swenggolf.database.ValueListener;

public class DatabaseFirebase/* extends Database*/ {

    //private final DatabaseReference myRef;


    /*public DatabaseFirebase(DatabaseReference myRef) {
        this.myRef = myRef;
    }*/


    //private DatabaseConnection db = DatabaseConnection.getInstance();
    private static ch.epfl.sweng.swenggolf.database.Database db = ch.epfl.sweng.swenggolf.database.Database.getInstance();

   // @Override
    public static void addUser(User user) {
        db.write("/users",user.getUserId(),user);
        //DatabaseReference tmpRef = myRef.child("users").child(user.getUserId());
       // tmpRef.setValue(user);
       // tmpRef.child("login").setValue("Google");
    }

   // @Override
    public static void getUser(final ValueListener listener, User user) {
        db.read("/users", user.getUserId(), listener, User.class);
       /* myRef.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = Config.getUser();
                if (dataSnapshot.exists()) {
                    user = dataSnapshot.getValue(User.class);
                }
                listener.onSuccess(dataSnapshot.exists(),user);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                listener.onFailure();
            }
        });*/

    }
}
