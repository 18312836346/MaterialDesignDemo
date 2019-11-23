package com.example.materialdesigndemo.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.materialdesigndemo.R;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button  btnRecyclerView;
    private Button  btnTabLayout;
    private Button  btnNavigation;
    private  Button btnCoordinator;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        //设置标题栏的标题
        Toolbar toolBar = findViewById( R.id.tool_bar );
        toolBar.setTitle( "Android5.0新特性" );
        toolBar.setLogo( R.mipmap.photo );
        toolBar.setSubtitle( "Material Design控件使用" );
        setSupportActionBar( toolBar );


        btnRecyclerView = findViewById( R.id.btn_recyclerView );
        btnTabLayout = findViewById( R.id.btn_tabLayout );
        btnNavigation = findViewById( R.id.btn_navigation);
        btnCoordinator = findViewById( R.id.btn_collapseing );

        btnRecyclerView.setOnClickListener( this );
        btnTabLayout.setOnClickListener( this );
        btnNavigation.setOnClickListener( this );
        btnCoordinator.setOnClickListener( this );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btn_recyclerView:
                Intent intent = new Intent( MainActivity.this, RecyclverViewActivity.class);
                startActivity( intent );
                break;
            case R.id.btn_collapseing:
                intent= new Intent( MainActivity.this, CoordinatorActivity.class);
                startActivity( intent );
                break;
            case R.id.btn_tabLayout:

                break;
            case R.id.btn_navigation:
                break;
        }
    }
}
