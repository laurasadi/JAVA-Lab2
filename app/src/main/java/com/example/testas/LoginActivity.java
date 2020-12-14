package com.example.testas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.corona.coronazp20t.R;


public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); //reikalinga atidaryti tuscia langa
        setContentView(R.layout.activity_login); //i tuscia langa ikrauna sukurta vaizda
        //visas kodas rasomas po sito komentaro
        Button loginbatonas = findViewById(R.id.loginbatonas);//tokiu budu issitraukiam elementus
        //is vaizdo
        final EditText usernametext = findViewById(R.id.usernametext);
        final EditText passwordtext = findViewById(R.id.passwordtext);

        final CheckBox rememberme = (CheckBox) findViewById(R.id.rememberMe);
        //bus konstruojamas vartotojo objektas perduodant context'a (langa kuriame esame)
        final User user=new User(LoginActivity.this);
        //patikriname, ar paskutini karta buvo pazymetas checkbox remember me
        rememberme.setChecked(user.isRememberedForLogin());

        //Aprasoma prisiminti mane checkbox logika
        if (rememberme.isChecked()){//jeigu checkbox buvo pazymetas-vartotojas pageidavo, kad informacija buvo issaugota
            usernametext.setText(user.getUsernameForLogin(),TextView.BufferType.EDITABLE);//setText-uzpildysime user informacija, editable - suteiksim galimybe paredaguoti duomenis
            passwordtext.setText(user.getPasswordForLogin(),TextView.BufferType.EDITABLE);
        } else {//jeigu checkbox buvo nepazymetas-vartotojas nenorejo, kad informacija buvo issaugota
            usernametext.setText("",TextView.BufferType.EDITABLE);
            passwordtext.setText("", TextView.BufferType.EDITABLE);
        }

        loginbatonas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //cia bus vygdomas kodas po batono paspaudimo
               /* Toast.makeText(LoginActivity.this,
                        "Prisijungimo Vardas:"+usernametext.getText().toString()+"\n"+
                        "Slaptazodis:"+passwordtext.getText().toString(),
                        Toast.LENGTH_SHORT).show(); */
                if (Validation.isValidUsername(usernametext.getText().toString()) && Validation.isValidPassword(passwordtext.getText().toString()))
                {
                    user.setUsernameForLogin(usernametext.getText().toString());
                    user.setPasswordForLogin(passwordtext.getText().toString());
                    if (rememberme.isChecked()){
                        user.setRemembermeKeyForLogin(true);
                    } else {
                        user.setRemembermeKeyForLogin(false);
                    }

                    //ketinimas pereiti i paieskos langa
                    Intent goToSearchActivity = new Intent(LoginActivity.this,//from
                            SearchActivity.class);//to
                    startActivity(goToSearchActivity);//cia pereina
                }
                else { //Kai duomenys neatinka reikalavimų
                    usernametext.setError(getResources().getString(R.string.login_invalid_username));
                    usernametext.requestFocus(); //Naudojamam laukely išmeta pranešimą
                }

            }
        });
    }

}
