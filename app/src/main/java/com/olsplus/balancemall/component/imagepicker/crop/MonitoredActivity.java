/*
 * Copyright 2016 Eric Liu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.olsplus.balancemall.component.imagepicker.crop;

import com.olsplus.balancemall.core.app.BaseCompatActivity;

import java.util.ArrayList;

/*
 * Modified from original in AOSP.
 */
public abstract class MonitoredActivity extends BaseCompatActivity {

    private final ArrayList<LifeCycleListener> listeners = new ArrayList<>();

    public interface LifeCycleListener {
        void onActivityCreated(MonitoredActivity activity);

        void onActivityDestroyed(MonitoredActivity activity);

        void onActivityStarted(MonitoredActivity activity);

        void onActivityStopped(MonitoredActivity activity);
    }

    public static class LifeCycleAdapter implements LifeCycleListener {
        public void onActivityCreated(MonitoredActivity activity) {
        }

        public void onActivityDestroyed(MonitoredActivity activity) {
        }

        public void onActivityStarted(MonitoredActivity activity) {
        }

        public void onActivityStopped(MonitoredActivity activity) {
        }
    }

    public void addLifeCycleListener(LifeCycleListener listener) {
        if (listeners.contains(listener)) return;
        listeners.add(listener);
    }

    public void removeLifeCycleListener(LifeCycleListener listener) {
        listeners.remove(listener);
    }

    // onCreate()
    @Override
    protected void getExtras() {
        for (LifeCycleListener listener : listeners) {
            listener.onActivityCreated(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityDestroyed(this);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityStarted(this);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (LifeCycleListener listener : listeners) {
            listener.onActivityStopped(this);
        }
    }

}
