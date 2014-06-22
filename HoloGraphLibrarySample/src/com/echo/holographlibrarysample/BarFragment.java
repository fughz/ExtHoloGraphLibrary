/*
 * 	   Created by Daniel Nadeau
 * 	   daniel.nadeau01@gmail.com
 * 	   danielnadeau.blogspot.com
 * 
 * 	   Licensed to the Apache Software Foundation (ASF) under one
       or more contributor license agreements.  See the NOTICE file
       distributed with this work for additional information
       regarding copyright ownership.  The ASF licenses this file
       to you under the Apache License, Version 2.0 (the
       "License"); you may not use this file except in compliance
       with the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

       Unless required by applicable law or agreed to in writing,
       software distributed under the License is distributed on an
       "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
       KIND, either express or implied.  See the License for the
       specific language governing permissions and limitations
       under the License.
 */

package com.echo.holographlibrarysample;

import android.animation.Animator;
import android.annotation.TargetApi;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.CycleInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.Toast;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.BarGraph.OnBarClickedListener;
import com.echo.holographlibrary.HoloGraphAnimate;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

public class BarFragment extends Fragment {

    BarGraph bg;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_bargraph, container, false);
        final Resources resources = getResources();
        ArrayList<Bar> aBars = new ArrayList<Bar>();
        Bar bar = new Bar();
        bar.setColor(resources.getColor(R.color.green_light));
        bar.setSelectedColor(resources.getColor(R.color.transparent_orange));
        bar.setName("Test1");
        bar.setValue(1000);
        bar.setValueString("$1,000");
        aBars.add(bar);
        bar = new Bar();
        bar.setColor(resources.getColor(R.color.orange));
        bar.setName("Test2");
        bar.setValue(2000);
        bar.setValueString("$2,000");
        aBars.add(bar);
        bar = new Bar();
        bar.setColor(resources.getColor(R.color.purple));
        bar.setName("Test3");
        bar.setValue(1500);
        bar.setValueString("$1,500");
        aBars.add(bar);

        final BarGraph barGraph = (BarGraph) v.findViewById(R.id.bargraph);
        bg = barGraph;
        barGraph.setBars(aBars);

        barGraph.setOnBarClickedListener(new OnBarClickedListener() {

            @Override
            public void onClick(int index) {
                Toast.makeText(getActivity(),
                        "Bar " + index + " clicked " + String.valueOf(barGraph.getBars().get(index).getValue()),
                        Toast.LENGTH_SHORT)
                        .show();
            }
        });
        Button animateBarButton = (Button) v.findViewById(R.id.animateBarButton);
        Button animateInsertBarButton = (Button) v.findViewById(R.id.animateInsertBarButton);
        Button animateDelteBarButton = (Button) v.findViewById(R.id.animateDeleteBarButton);
        animateBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (Bar b : barGraph.getBars()) {
                    b.setGoalValue((float) Math.random() * 1000);
                    b.setValuePrefix("$");//display the prefix throughout the animation
                    Log.d("goal val", String.valueOf(b.getGoalValue()));
                }
                barGraph.setDuration(1500);//default if unspecified is 300 ms
                barGraph.setInterpolator(new AccelerateDecelerateInterpolator());//IMPORTANT: Read source comment before using
                barGraph.setAnimationListener(getAnimationListener());
                barGraph.animateToGoalValues();

            }
        });
        animateInsertBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barGraph.cancelAnimating(); //must clear existing to call onAnimationEndListener cleanup BEFORE adding new bars
                int newPosition = new Random().nextInt(barGraph.getBars().size());
                Bar bar = new Bar();
                bar.setColor(resources.getColor(R.color.blue));
                bar.setName("Insert bar " + String.valueOf(barGraph.getBars().size()));
                bar.setValue(0);
                bar.mAnimateSpecial = HoloGraphAnimate.ANIMATE_INSERT;
                barGraph.getBars().add(1,bar);
                for (Bar b : barGraph.getBars()) {
                    b.setGoalValue((float) Math.random() * 1000);
                    b.setValuePrefix("$");//display the prefix throughout the animation
                    Log.d("goal val", String.valueOf(b.getGoalValue()));
                }
                barGraph.setDuration(1500);//default if unspecified is 300 ms
                barGraph.setInterpolator(new DecelerateInterpolator());//Don't use over/undershoot interpolator for insert/delete
                barGraph.setAnimationListener(getAnimationListener());
                barGraph.animateToGoalValues();

            }
        });
        animateDelteBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                barGraph.cancelAnimating(); //must clear existing to call onAnimationEndListener cleanup BEFORE adding new bars
                int newPosition = new Random().nextInt(barGraph.getBars().size());
                Bar bar = barGraph.getBars().get(newPosition);
                bar.mAnimateSpecial = HoloGraphAnimate.ANIMATE_DELETE;
                for (Bar b : barGraph.getBars()) {
                    b.setGoalValue((float) Math.random() * 1000);
                    b.setValuePrefix("$");//display the prefix throughout the animation
                    Log.d("goal val", String.valueOf(b.getGoalValue()));
                }
                bar.setGoalValue(0);//animate to 0 then delete
                barGraph.setDuration(1500);//default if unspecified is 300 ms
                barGraph.setInterpolator(new AccelerateDecelerateInterpolator());//Don't use over/undershoot interpolator for insert/delete
                barGraph.setAnimationListener(getAnimationListener());
                barGraph.animateToGoalValues();

            }
        });
        return v;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR1)
    public Animator.AnimatorListener getAnimationListener(){
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1)
            return new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {//consider calling makeValueS
                    ArrayList<Bar> newBars = new ArrayList<Bar>();
                    for (Bar b : bg.getBars()){
                        if (b.mAnimateSpecial != HoloGraphAnimate.ANIMATE_DELETE){
                            b.mAnimateSpecial = HoloGraphAnimate.ANIMATE_NORMAL;
                            newBars.add(b);
                        }
                    }
                    bg.setBars(newBars);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            };
        else return null;

    }
}
