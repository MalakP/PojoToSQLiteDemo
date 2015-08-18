package digitalfish.test.pojotosqlitedemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import digitalfish.test.pojotosqlitedemo.DataClasses.User;
import digitalfish.test.pojotosqlitedemo.DataClasses.Visit;
import digitalfish.test.pojotosqlitedemo.SQLite.DatabaseHelper;

public class MainActivity extends AppCompatActivity {

    //Butter knife entries for casting widgets from layout.
    @InjectView(R.id.button_write)
    Button mButtonWrite;
    @InjectView(R.id.button_read)
    Button mButtonRead;
    @InjectView(R.id.text_field)
    TextView mTextField;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //Write data to SQLite
        mButtonWrite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Single row data example
                User lUser = new User();
                lUser.setLogin("db_login");
                lUser.setPassword("db password");
                lUser.setUserName("writed to SQLite");
                DatabaseHelper.getInstance(MainActivity.this).addObjectToTable(lUser, User.class.getName(), true);

                //List of objects of Visit
                List<Visit> lVisits = new ArrayList<Visit>();
                for (int i = 0; i < 10; i++) {
                    Visit lVisit = new Visit();
                    lVisit.setId((long) i);
                    lVisit.setName("Vist no." + i);
                    lVisits.add(lVisit);
                }
                DatabaseHelper.getInstance(MainActivity.this).addListOfObjects(lVisits, Visit.class.getName(), false);
            }
        });

        //Read data from SQLite
        mButtonRead.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User lUser = (User) DatabaseHelper.getInstance(MainActivity.this).getObjectFromTable(User.class.getName());
                if(lUser!=null)
                    mTextField.setText(lUser.getUserName()+", "+lUser.getLogin()+", "+lUser.getPassword());
                else{
                    mTextField.setText("NoData in DB, write first");
                    return;
                }
                //Read from database and get list of selected objects objects (in this case visits where id<3)
                List<Object> lVisits = DatabaseHelper.getInstance(MainActivity.this).getListOfObjects(Visit.class.getName(),"id<?", new String[]{String.valueOf(3)});

                for(Object visit: lVisits)
                {
                    mTextField.append("\n"+((Visit)visit).getName());
                }

            }
        });

    }


}
