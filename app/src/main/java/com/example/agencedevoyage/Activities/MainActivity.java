package com.example.agencedevoyage.Activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agencedevoyage.Adapters.CategoryAdapter;
import com.example.agencedevoyage.Adapters.PopularAdapter;
import com.example.agencedevoyage.Domains.CategoryDomain;
import com.example.agencedevoyage.Domains.PopularDomain;
import com.example.agencedevoyage.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapterPop , adapterCat ;
    private RecyclerView recyclerViewPop , recyclerViewCat ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initRecyclerView() ;
    }
    private void initRecyclerView(){
        ArrayList<PopularDomain> items = new ArrayList<>() ;


        items.add(new PopularDomain (  "Mar caible, avendia lago", "Miami Beach",  "This 2 bed /1 bath home boasts an enormous,"
                +"open-living plan, accented by striking" +
                "architectural features and high-end finishes." +
                "Feel inspired by open sight lines that" +
                " embrace the outdoors, crowned by stunning" +
                "coffered ceilings. ",  2,  true,  4.8,  "pic1",  true,  1000));

        items.add(new PopularDomain (  "Passo Rolle, TN",  "Hawaii Beach",  "This 2 bed /1 bath home boasts an enormous,"+
                "open-living plan, accented by striking" +
                "architectural features and high-end finishes." +
                "Feel inspired by open sight lines that" +
                " embrace the outdoors, crowned by stunning" +
                "coffered ceilings. ", 1, false,  5,  "pic2",  false,  2500));
        items.add(new PopularDomain (  "Mar caible , avendia lago",  "Miami Beach",  "This 2 bed /1 bath home boasts an enormous,"
                +"open-living plan, accented by striking" +
                "architectural features and high-end finishes." +
                "Feel inspired by open sight lines that" +
                " embrace the outdoors, crowned by stunning" +" coffered ceilings. ",  3,  true,  4.3, "pic3",  true, 30000));

        recyclerViewPop = findViewById(R.id.view_pop);
        recyclerViewPop.setLayoutManager(new LinearLayoutManager( this, LinearLayoutManager.HORIZONTAL,  false));
        adapterPop = new PopularAdapter(items) ;
        recyclerViewPop.setAdapter(adapterPop);


        ArrayList<CategoryDomain> catsList =new ArrayList<>();
        catsList.add(new CategoryDomain(  "Beaches",  "cat1"));
        catsList.add(new CategoryDomain(  "Camps",  "cat2"));
        catsList.add(new CategoryDomain(  "Forest",  "cat3"));
        catsList.add(new CategoryDomain(  "Desert",  "cat4"));
        catsList.add(new CategoryDomain(  "Mountain",  "cat5"));
        recyclerViewCat =findViewById(R.id.view_cat);
        recyclerViewCat.setLayoutManager(new LinearLayoutManager(  this, LinearLayoutManager.HORIZONTAL,  false));
        adapterCat=new CategoryAdapter(catsList);
        recyclerViewCat.setAdapter(adapterCat);
    }
}