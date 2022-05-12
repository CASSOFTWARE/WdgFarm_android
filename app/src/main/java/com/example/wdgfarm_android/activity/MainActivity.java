package com.example.wdgfarm_android.activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.example.wdgfarm_android.R;
import com.example.wdgfarm_android.fragment.DataFragment;
import com.example.wdgfarm_android.fragment.HistoryFragment;
import com.example.wdgfarm_android.fragment.SettingFragment;
import com.example.wdgfarm_android.fragment.WorkFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    // 마지막으로 뒤로가기 버튼을 눌렀던 시간 저장
    private long backKeyPressedTime = 0;
    private Toast toast;

    private FragmentManager mFragmentManager = getSupportFragmentManager();

    private BottomNavigationView bottomNavigationView;

    private final WorkFragment mWorkFragment = new WorkFragment();
    private final DataFragment mDataFragment = new DataFragment();
    private final SettingFragment mSettingFragment = new SettingFragment();
    private final HistoryFragment mListFragment = new HistoryFragment();


    private Fragment mActiveFragment = mWorkFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        bottomNavigationView = findViewById(R.id.bottom_navigation_view);


        //화면 항상 켜기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);

        mFragmentManager.beginTransaction().add(R.id.frame_layout, mWorkFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.frame_layout, mListFragment).hide(mListFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.frame_layout, mDataFragment).hide(mDataFragment).commit();
        mFragmentManager.beginTransaction().add(R.id.frame_layout, mSettingFragment).hide(mSettingFragment).commit();

        // bottomNavigationView의 아이템이 선택될 때 호출될 리스너 등록
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                FragmentTransaction transaction = mFragmentManager.beginTransaction();
                switch (item.getItemId()) {
                    case R.id.navigation_menu1: {
                        transaction.hide(mActiveFragment).show(mWorkFragment).commit();

                        mActiveFragment = mWorkFragment;
                        break;
                    }
                    case R.id.navigation_menu2: {
                        transaction.hide(mActiveFragment).show(mListFragment).commit();

                        mActiveFragment = mListFragment;
                        break;
                    }
                    case R.id.navigation_menu3: {
                        transaction.hide(mActiveFragment).show(mDataFragment).commit();

                        mActiveFragment = mDataFragment;
                        break;
                    }
                    case R.id.navigation_menu4: {
                        transaction.hide(mActiveFragment).show(mSettingFragment).commit();

                        mActiveFragment = mSettingFragment;
                        break;
                    }
                }
                return true;
            }
        });
    }

    //뒤로가기 버튼 두번 누르면 앱 종료
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        //뒤로가기 버튼 누르면 계량작업으로 이동
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        transaction.hide(mActiveFragment).show(mWorkFragment).commit();

        mActiveFragment = mWorkFragment;

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setSelectedItemId(R.id.navigation_menu1);


        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지났으면 Toast Show
        // 2000 milliseconds = 2 seconds
        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
            backKeyPressedTime = System.currentTimeMillis();
            toast.makeText(this, R.string.back_exit, Toast.LENGTH_SHORT).show();
            return;
        }
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간에 2초를 더해 현재시간과 비교 후
        // 마지막으로 뒤로가기 버튼을 눌렀던 시간이 2초가 지나지 않았으면 종료
        // 현재 표시된 Toast 취소
        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
            finish();
            toast.cancel();
        }
    }

    //화면 터치시 키보드 감추기
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        hideKeyboard();
        return super.dispatchTouchEvent(ev);
    }

    //키보드 감추기
    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}