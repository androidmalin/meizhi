
baseurl="https://search.maven.org/remotecontent?filepath=com/android/tools"
version="2.3.0"
path="/opt/android-studio/gradle/m2repository/com/android/tools"

#gradle-2.3.0-sources.jar
#gradle-2.3.0-javadoc.jar
cd $path/build/gradle/$version
wget -c $baseurl/build/gradle/$version/gradle-$version.jar -O gradle-$version.jar
wget -c $baseurl/build/gradle/$version/gradle-$version-sources.jar -O gradle-$version-sources.jar
wget -c $baseurl/build/gradle/$version/gradle-$version-javadoc.jar -O gradle-$version-javadoc.jar

#gradle-core-2.3.0-sources.jar
#gradle-core-2.3.0-javadoc.jar
cd $path/build/gradle-core/$version
wget -c $baseurl/build/gradle-core/$version/gradle-core-$version.jar -O gradle-core-$version.jar
wget -c $baseurl/build/gradle-core/$version/gradle-core-$version-sources.jar -O gradle-core-$version-sources.jar
wget -c $baseurl/build/gradle-core/$version/gradle-core-$version-javadoc.jar -O gradle-core-$version-javadoc.jar


#gradle-api-2.3.0-sources.jar
#gradle-api-2.3.0-javadoc.jar
cd $path/build/gradle-api/$version
wget -c $baseurl/build/gradle-api/$version/gradle-api-$version.jar -O gradle-api-$version.jar
wget -c $baseurl/build/gradle-api/$version/gradle-api-$version-sources.jar -O gradle-api-$version-sources.jar
wget -c $baseurl/build/gradle-api/$version/gradle-api-$version-javadoc.jar -O gradle-api-$version-javadoc.jar


#builder-2.3.0-sources.jar
#builder-2.3.0-javadoc.jar
cd $path/build/builder/$version
wget -c $baseurl/build/builder/$version/builder-$version.jar -O builder-$version.jar
wget -c $baseurl/build/builder/$version/builder-$version-sources.jar -O builder-$version-sources.jar
wget -c $baseurl/build/builder/$version/builder-$version-javadoc.jar -O builder-$version-javadoc.jar

#builder-test-api-2.3.0-sources.jar
#builder-test-api-2.3.0-javadoc.jar
cd $path/build/builder-test-api/$version
wget -c $baseurl/build/builder-test-api/$version/builder-test-api-$version.jar -O builder-test-api-$version.jar
wget -c $baseurl/build/builder-test-api/$version/builder-test-api-$version-sources.jar -O builder-test-api-$version-sources.jar
wget -c $baseurl/build/builder-test-api/$version/builder-test-api-$version-javadoc.jar -O builder-test-api-$version-javadoc.jar


#builder-model-2.3.0-sources.jar
#builder-model-2.3.0-javadoc.jar
cd $path/build/builder-model/$version
wget -c $baseurl/build/builder-model/$version/builder-model-$version.jar -O builder-model-$version.jar
wget -c $baseurl/build/builder-model/$version/builder-model-$version-sources.jar -O builder-model-$version-sources.jar
wget -c $baseurl/build/builder-model/$version/builder-model-$version-javadoc.jar -O builder-model-$version-javadoc.jar


cd $path/build/gradle-experimental/0.9.0
wget -c $baseurl/build/gradle-experimental/0.9.0/gradle-experimental-0.9.0.jar -O gradle-experimental-0.9.0.jar
wget -c $baseurl/build/gradle-experimental/0.9.0/gradle-experimental-0.9.0-sources.jar -O gradle-experimental-0.9.0-sources.jar
wget -c $baseurl/build/gradle-experimental/0.9.0/gradle-experimental-0.9.0-javadoc.jar -O gradle-experimental-0.9.0-javadoc.jar

echo "50-----------------------------------"
cd $path/build/manifest-merger/25.3.0
wget -c $baseurl/build/manifest-merger/25.3.0/manifest-merger-25.3.0.jar -O manifest-merger-25.3.0.jar
wget -c $baseurl/build/manifest-merger/25.3.0/manifest-merger-25.3.0-sources.jar -O manifest-merger-25.3.0-sources.jar
wget -c $baseurl/build/manifest-merger/25.3.0/manifest-merger-25.3.0-javadoc.jar -O manifest-merger-25.3.0-javadoc.jar
echo "54-----------------------------------"
cd $path/build/transform-api/2.0.0-deprecated-use-gradle-api
wget -c $baseurl/build/transform-api/2.0.0-deprecated-use-gradle-api/transform-api-2.0.0-deprecated-use-gradle-api.jar -O transform-api-2.0.0-deprecated-use-gradle-api.jar
wget -c $baseurl/build/transform-api/2.0.0-deprecated-use-gradle-api/transform-api-2.0.0-deprecated-use-gradle-api-sources.jar -O transform-api-2.0.0-deprecated-use-gradle-api-sources.jar
wget -c $baseurl/build/transform-api/2.0.0-deprecated-use-gradle-api/transform-api-2.0.0-deprecated-use-gradle-api-javadoc.jar -O transform-api-2.0.0-deprecated-use-gradle-api-javadoc.jar

echo "59-----------------------------------"
cd $path/annotations/25.3.0
wget -c $baseurl/annotations/25.3.0/annotations-25.3.0.jar -O annotations-25.3.0.jar
wget -c $baseurl/annotations/25.3.0/annotations-25.3.0-sources.jar -O annotations-25.3.0-sources.jar
wget -c $baseurl/annotations/25.3.0/annotations-25.3.0-javadoc.jar -O annotations-25.3.0-javadoc.jar
echo "65-----------------------------------"






echo "70-----------------------------------"
cd $path/sdklib/25.3.0
wget -c $baseurl/sdklib/25.3.0/sdklib-25.3.0.jar -O sdklib-25.3.0.jar
wget -c $baseurl/sdklib/25.3.0/sdklib-25.3.0-sources.jar -O sdklib-25.3.0-sources.jar
wget -c $baseurl/sdklib/25.3.0/sdklib-25.3.0-javadoc.jar -O sdklib-25.3.0-javadoc.jar

echo "75-----------------------------------"
cd $path/repository/25.3.0
wget -c $baseurl/repository/25.3.0/repository-25.3.0.jar -O repository-25.3.0.jar
wget -c $baseurl/repository/25.3.0/repository-25.3.0-sources.jar -O repository-25.3.0-sources.jar
wget -c $baseurl/repository/25.3.0/repository-25.3.0-javadoc.jar -O repository-25.3.0-javadoc.jar

echo "80-----------------------------------"
cd $path/dvlib/25.3.0
wget -c $baseurl/dvlib/25.3.0/dvlib-25.3.0.jar -O dvlib-25.3.0.jar
wget -c $baseurl/dvlib/25.3.0/dvlib-25.3.0-sources.jar -O dvlib-25.3.0-sources.jar
wget -c $baseurl/dvlib/25.3.0/dvlib-25.3.0-javadoc.jar -O dvlib-25.3.0-javadoc.jar


echo "86-----------------------------------"
cd $path/common/25.3.0
wget -c $baseurl/common/25.3.0/common-25.3.0.jar -O common-25.3.0.jar
wget -c $baseurl/common/25.3.0/common-25.3.0-sources.jar -O common-25.3.0-sources.jar
wget -c $baseurl/common/25.3.0/common-25.3.0-javadoc.jar -O common-25.3.0-javadoc.jar


echo "92-----------------------------------"
cd $path/sdk-common/25.3.0
wget -c $baseurl/sdk-common/25.3.0/sdk-common-25.3.0.jar -O sdk-common-25.3.0.jar
wget -c $baseurl/sdk-common/25.3.0/sdk-common-25.3.0-sources.jar -O sdk-common-25.3.0-sources.jar
wget -c $baseurl/sdk-common/25.3.0/sdk-common-25.3.0-javadoc.jar -O sdk-common-25.3.0-javadoc.jar
echo "96-----------------------------------"
#analytics-library
cd $path/analytics-library/protos/25.3.0
wget -c $baseurl/analytics-library/protos/25.3.0/protos-25.3.0.jar -O protos-25.3.0.jar
wget -c $baseurl/analytics-library/protos/25.3.0/protos-25.3.0-sources.jar -O protos-25.3.0-sources.jar
wget -c $baseurl/analytics-library/protos/25.3.0/protos-25.3.0-javadoc.jar -O protos-25.3.0-javadoc.jar

echo "102-----------------------------------"
cd $path/analytics-library/shared/25.3.0
wget -c $baseurl/analytics-library/shared/25.3.0/shared-25.3.0.jar -O shared-25.3.0.jar
wget -c $baseurl/analytics-library/shared/25.3.0/shared-25.3.0-sources.jar -O shared-25.3.0-sources.jar
wget -c $baseurl/analytics-library/shared/25.3.0/shared-25.3.0-javadoc.jar -O shared-25.3.0-javadoc.jar



cd $path/analytics-library/tracker/25.3.0
wget -c $baseurl/analytics-library/tracker/25.3.0/tracker-25.3.0.jar -O tracker-25.3.0.jar
wget -c $baseurl/analytics-library/tracker/25.3.0/tracker-25.3.0-sources.jar -O tracker-25.3.0-sources.jar
wget -c $baseurl/analytics-library/tracker/25.3.0/tracker-25.3.0-javadoc.jar -O tracker-25.3.0-javadoc.jar


cd $path/ddms/ddmlib/25.3.0
wget -c $baseurl/ddms/ddmlib/25.3.0/ddmlib-25.3.0.jar -O ddmlib-25.3.0.jar
wget -c $baseurl/ddms/ddmlib/25.3.0/ddmlib-25.3.0-sources.jar -O ddmlib-25.3.0-sources.jar
wget -c $baseurl/ddms/ddmlib/25.3.0/ddmlib-25.3.0-javadoc.jar -O ddmlib-25.3.0-javadoc.jar


cd $path/external/lombok/lombok-ast/0.2.3
wget -c $baseurl/external/lombok/lombok-ast/0.2.3/lombok-ast-0.2.3.jar -O lombok-ast-0.2.3.jar
wget -c $baseurl/external/lombok/lombok-ast/0.2.3/lombok-ast-0.2.3-sources.jar -O lombok-ast-0.2.3-sources.jar
wget -c $baseurl/external/lombok/lombok-ast/0.2.3/lombok-ast-0.2.3-javadoc.jar -O lombok-ast-0.2.3-javadoc.jar



cd $path/external/com-intellij/uast/162.2228.14
wget -c $baseurl/external/com-intellij/uast/162.2228.14/uast-162.2228.14.jar -O uast-162.2228.14.jar
wget -c $baseurl/external/com-intellij/uast/162.2228.14/uast-162.2228.14-sources.jar -O uast-162.2228.14-sources.jar
wget -c $baseurl/external/com-intellij/uast/162.2228.14/uast-162.2228.14-javadoc.jar -O uast-162.2228.14-javadoc.jar


cd $path/jack/jack-api/0.13.0
wget -c $baseurl/jack/jack-api/0.13.0/jack-api-0.13.0.jar -O jack-api-0.13.0.jar
wget -c $baseurl/jack/jack-api/0.13.0/jack-api-0.13.0-sources.jar -O jack-api-0.13.0-sources.jar
wget -c $baseurl/jack/jack-api/0.13.0/jack-api-0.13.0-javadoc.jar -O jack-api-0.13.0-javadoc.jar



cd $path/jill/jill-api/0.10.0
wget -c $baseurl/jill/jill-api/0.10.0/jill-api-0.13.0.jar -O jill-api-0.10.0.jar
wget -c $baseurl/jill/jill-api/0.10.0/jill-api-0.13.0-sources.jar -O jill-api-0.10.0-sources.jar
wget -c $baseurl/jill/jill-api/0.10.0/jill-api-0.13.0-javadoc.jar -O jill-api-0.10.0-javadoc.jar



cd $path/layoutlib/layoutlib-api/25.3.0
wget -c $baseurl/layoutlib/layoutlib-api/25.3.0/layoutlib-api-25.3.0.jar -O layoutlib-api-25.3.0.jar
wget -c $baseurl/layoutlib/layoutlib-api/25.3.0/layoutlib-api-25.3.0-sources.jar -O layoutlib-api-25.3.0-sources.jar
wget -c $baseurl/layoutlib/layoutlib-api/25.3.0/layoutlib-api-25.3.0-javadoc.jar -O layoutlib-api-25.3.0-javadoc.jar


cd $path/lint/lint/25.3.0
wget -c $baseurl/lint/lint/25.3.0/lint-25.3.0.jar -O lint-25.3.0.jar
wget -c $baseurl/lint/lint/25.3.0/lint-25.3.0-sources.jar -O lint-25.3.0-sources.jar
wget -c $baseurl/lint/lint/25.3.0/lint-25.3.0-javadoc.jar -O lint-25.3.0-javadoc.jar



cd $path/lint/lint-api/25.3.0
wget -c $baseurl/lint/lint-api/25.3.0/lint-api-25.3.0.jar -O lint-api-25.3.0.jar
wget -c $baseurl/lint/lint-api/25.3.0/lint-api-25.3.0-sources.jar -O lint-api-25.3.0-sources.jar
wget -c $baseurl/lint/lint-api/25.3.0/lint-api-25.3.0-javadoc.jar -O lint-api-25.3.0-javadoc.jar



cd $path/lint/lint-checks/25.3.0
wget -c $baseurl/lint/lint-checks/25.3.0/lint-checks-25.3.0.jar -O lint-checks-25.3.0.jar
wget -c $baseurl/lint/lint-checks/25.3.0/lint-checks-25.3.0-sources.jar -O lint-checks-25.3.0-sources.jar
wget -c $baseurl/lint/lint-checks/25.3.0/lint-checks-25.3.0-javadoc.jar -O lint-checks-25.3.0-javadoc.jar
