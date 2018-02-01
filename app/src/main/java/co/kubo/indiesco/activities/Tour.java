package co.kubo.indiesco.activities;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RadioGroup;

import butterknife.ButterKnife;
import co.kubo.indiesco.Adaptadores.CustomPagerAdapter;
import co.kubo.indiesco.R;
import co.kubo.indiesco.utils.SharedPreferenceManager;


public class Tour extends AppCompatActivity {
    CustomPagerAdapter mCustomPagerAdapter;
    public ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tour);
        ButterKnife.bind(this);
        mCustomPagerAdapter = new CustomPagerAdapter(this);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        final RadioGroup radioGroup = (RadioGroup)findViewById(R.id.radiogroup);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }
            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        radioGroup.check(R.id.radioButton);
                        break;
                    case 1:
                        radioGroup.check(R.id.radioButton2);
                        break;
                    case 2:
                        radioGroup.check(R.id.radioButton3);
                        break;
                    default:
                        break;
                }//switch
            }//public void onPageSelected
            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        viewPager.setAdapter(mCustomPagerAdapter);
    }
    public void abrirLogin(){
        SharedPreferenceManager.setPrimeraVez(Tour.this, true);
        Intent mainIntent = new Intent(Tour.this, Login.class);
        mainIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(mainIntent);
        finish();
    }//public void abrirLogin
}
