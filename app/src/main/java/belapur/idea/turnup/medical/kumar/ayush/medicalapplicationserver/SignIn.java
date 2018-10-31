package belapur.idea.turnup.medical.kumar.ayush.medicalapplicationserver;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import belapur.idea.turnup.medical.kumar.ayush.medicalapplicationserver.Common.Common;
import belapur.idea.turnup.medical.kumar.ayush.medicalapplicationserver.Model.User;

public class SignIn extends AppCompatActivity
{
    EditText edtPhone, edtPassword ;
    Button btnSignIn;

    FirebaseDatabase db;
    DatabaseReference users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPhone = (EditText) findViewById(R.id.edtPhone);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);

        db = FirebaseDatabase.getInstance();
        users = db.getReference("User");

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SignInUser(edtPhone.getText().toString(),edtPassword.getText().toString());
            }
        });
    }

    private void SignInUser(String phone, String password)
    {
        final ProgressDialog mDialog  = new ProgressDialog(SignIn.this);
        mDialog.setMessage("Please Waiting...");
        mDialog.show();

        final String localPhone = phone ;
        final String localPassword = password ;
        users.addValueEventListener(new ValueEventListener()
        {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot)
            {
                if (dataSnapshot.child(localPhone).exists())
                {
                    mDialog.dismiss();
                    User user = dataSnapshot.child(localPhone).getValue(User.class);
                    user.setPhone(localPhone);
                    if (Boolean.parseBoolean(user.getIsStaff()))
                    {
                        if (user.getPassword().equals(localPassword))
                        {
                            Intent intent = new Intent(SignIn.this,Home.class);
                            Common.currentUser = user;
                            startActivity(intent);
                            finish();
                        }
                        else
                        {
                            Toast.makeText(SignIn.this,"Wrong Password",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(SignIn.this,"Please login with staff account",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(SignIn.this,"User not exsist in database",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
