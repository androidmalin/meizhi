#!/bin/bash
cd .. && gradle -q app:dependencyInsight --dependency okhttp --configuration compile && \
sleep 0.5 \
&& gradle -q app:dependencyInsight --dependency rxjava --configuration compile && \
sleep 0.5 \
&& gradle -q app:dependencyInsight --dependency com.android.support:support-annotations --configuration compile
