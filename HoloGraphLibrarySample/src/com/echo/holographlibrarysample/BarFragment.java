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

import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.actionbarsherlock.app.SherlockFragment;
import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.echo.holographlibrary.BarGraph.OnBarClickedListener;

import java.util.ArrayList;

public class BarFragment extends SherlockFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.fragment_bargraph, container, false);
        final Resources resources = getResources();
        ArrayList<Bar> points = new ArrayList<Bar>();
        Bar d = new Bar();
        d.setColor(resources.getColor(R.color.green_light));
        d.setName("Test1");
        d.setValue(1000);
        d.setValueString("$1,000");
        Bar d2 = new Bar();
        d2.setColor(resources.getColor(R.color.orange));
        d2.setName("Test2");
        d2.setValue(2000);
        d2.setValueString("$2,000");
        points.add(d);
        points.add(d2);

        BarGraph g = (BarGraph) v.findViewById(R.id.bargraph);
        g.setBars(points);

        g.setOnBarClickedListener(new OnBarClickedListener() {

            @Override
            public void onClick(int index) {

            }

        });

        return v;
    }
}
