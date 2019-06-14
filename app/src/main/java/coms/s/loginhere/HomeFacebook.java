package coms.s.loginhere;

import android.app.Activity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import de.hdodenhof.circleimageview.CircleImageView;

public class HomeFacebook extends Activity {

    TextView fbname;
    EditText fbemail;
    CircleImageView circleImageFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homefb);

        fbname= findViewById(R.id.namefb);
        fbemail = findViewById(R.id.emailfb);
        circleImageFb = findViewById(R.id.profilepicfb);

        String first_namefb = getIntent().getStringExtra("firstnamefb");
        String last_namefb = getIntent().getStringExtra("lastnamefb") ;
        String imageurlfb = getIntent().getStringExtra("imageurlfb") ;
        String newemailfb = getIntent().getStringExtra("emailfb") ;

        fbname.setText(first_namefb+" "+last_namefb);
        fbemail.setText(newemailfb);
        RequestOptions requestOptions=new RequestOptions();
        requestOptions.dontAnimate();
        Glide.with(HomeFacebook.this).load(imageurlfb).into(circleImageFb);
    }
}