package coms.s.loginhere;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.EditText;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeActivity extends Activity {
     CircleImageView circleImageViewtwitter;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        String usernameTwitter = getIntent().getStringExtra("username");
        String emailTwitter = getIntent().getStringExtra("email") ;
        String profileurlTwitter = getIntent().getStringExtra("profileurl") ;

        TextView usernameTwitterTextview = findViewById(R.id.TV_username);
        EditText emailTwitterEdittext = findViewById(R.id.TV_email);
        circleImageViewtwitter=findViewById(R.id.profilepicTwitter);

        RequestOptions requestOptions=new RequestOptions()
                                        .placeholder(R.drawable.bckgrnd2)
                                        .centerCrop();
        Glide.with(HomeActivity.this).load(profileurlTwitter).apply(requestOptions).into(circleImageViewtwitter);
        usernameTwitterTextview.setText(usernameTwitter);
        emailTwitterEdittext.setText(emailTwitter);
    }
}