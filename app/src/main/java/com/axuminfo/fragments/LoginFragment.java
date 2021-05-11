package com.axuminfo.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.axuminfo.MainActivity;
import com.axuminfo.R;
import com.axuminfo.models.User;
import com.axuminfo.services.BackgroundWorker;

public class LoginFragment extends Fragment {

    private EditText usernameTf, passwordTf;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.login_page, container, false);

        usernameTf=(EditText) view.findViewById(R.id.username);
        passwordTf=(EditText) view.findViewById(R.id.password);

        Button submitBtn=(Button) view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.mainActivity.closeKeyboard();
                boolean success=submit(v);
            }
        });

        return view;
    }

    public boolean submit(View view){
        String username=usernameTf.getText().toString();
        String password=passwordTf.getText().toString();
        if(username==null||"".equals(username)){
            Toast.makeText(MainActivity.appContext, R.string.username_is_required, Toast.LENGTH_LONG).show();
            return false;
        }
        if(password==null||"".equals(password)){
            Toast.makeText(MainActivity.appContext, R.string.password_is_required, Toast.LENGTH_LONG).show();
            return false;
        }

        User.username=username;
        User.isLoggedIn=false;
        BackgroundWorker backgroundWorker=new BackgroundWorker(MainActivity.appContext);
        String OPERATION_TYPE="login";
        backgroundWorker.execute(OPERATION_TYPE, username, password);

        passwordTf.setText("");
        return true;
    }

}
