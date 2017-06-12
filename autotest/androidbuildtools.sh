
baseurl="https://dl.bintray.com/android/android-tools/com/android/tools"
#baseurl="https://search.maven.org/remotecontent?filepath=com/android/tools"
version="2.3.3"
path="/home/malin/android-studio/android-studio-2.3.3/gradle/m2repository/com/android/tools"
buildToolsVersion="25.3.0"
localpath=""

function mkDirFile {
    echo $localpath
    if [[ ! -e $localpath ]]; then
        mkdir $localpath
    fi
}

#gradle-2.3.0-sources.jar
#gradle-2.3.0-javadoc.jar
localpath=$path/build/gradle/$version
mkDirFile
cd ${localpath}
wget -c $baseurl/build/gradle/$version/gradle-$version.pom -O gradle-$version.pom
wget -c $baseurl/build/gradle/$version/gradle-$version.jar -O gradle-$version.jar
wget -c $baseurl/build/gradle/$version/gradle-$version-sources.jar -O gradle-$version-sources.jar
wget -c $baseurl/build/gradle/$version/gradle-$version-javadoc.jar -O gradle-$version-javadoc.jar

#gradle-core-2.3.0-sources.jar
#gradle-core-2.3.0-javadoc.jar

localpath=$path/build/gradle-core/$version
mkDirFile
cd $localpath
wget -c $baseurl/build/gradle-core/$version/gradle-core-$version.pom -O gradle-core-$version.pom
wget -c $baseurl/build/gradle-core/$version/gradle-core-$version.jar -O gradle-core-$version.jar
wget -c $baseurl/build/gradle-core/$version/gradle-core-$version-sources.jar -O gradle-core-$version-sources.jar
wget -c $baseurl/build/gradle-core/$version/gradle-core-$version-javadoc.jar -O gradle-core-$version-javadoc.jar


#gradle-api-2.3.0-sources.jar
#gradle-api-2.3.0-javadoc.jar
localpath=$path/build/gradle-api/$version
mkDirFile
cd $localpath
wget -c $baseurl/build/gradle-api/$version/gradle-api-$version.pom -O gradle-api-$version.pom
wget -c $baseurl/build/gradle-api/$version/gradle-api-$version.jar -O gradle-api-$version.jar
wget -c $baseurl/build/gradle-api/$version/gradle-api-$version-sources.jar -O gradle-api-$version-sources.jar
wget -c $baseurl/build/gradle-api/$version/gradle-api-$version-javadoc.jar -O gradle-api-$version-javadoc.jar


#builder-2.3.0-sources.jar
#builder-2.3.0-javadoc.jar
localpath=$path/build/builder/$version
mkDirFile
cd $localpath
wget -c $baseurl/build/builder/$version/builder-$version.pom -O builder-$version.pom
wget -c $baseurl/build/builder/$version/builder-$version.jar -O builder-$version.jar
wget -c $baseurl/build/builder/$version/builder-$version-sources.jar -O builder-$version-sources.jar
wget -c $baseurl/build/builder/$version/builder-$version-javadoc.jar -O builder-$version-javadoc.jar

#builder-test-api-2.3.0-sources.jar
#builder-test-api-2.3.0-javadoc.jar
localpath=$path/build/builder-test-api/$version
mkDirFile
cd $localpath
wget -c $baseurl/build/builder-test-api/$version/builder-test-api-$version.pom -O builder-test-api-$version.pom
wget -c $baseurl/build/builder-test-api/$version/builder-test-api-$version.jar -O builder-test-api-$version.jar
wget -c $baseurl/build/builder-test-api/$version/builder-test-api-$version-sources.jar -O builder-test-api-$version-sources.jar
wget -c $baseurl/build/builder-test-api/$version/builder-test-api-$version-javadoc.jar -O builder-test-api-$version-javadoc.jar


#builder-model-2.3.0-sources.jar
#builder-model-2.3.0-javadoc.jar
localpath=$path/build/builder-model/$version
mkDirFile
cd $localpath
wget -c $baseurl/build/builder-model/$version/builder-model-$version.pom -O builder-model-$version.pom
wget -c $baseurl/build/builder-model/$version/builder-model-$version.jar -O builder-model-$version.jar
wget -c $baseurl/build/builder-model/$version/builder-model-$version-sources.jar -O builder-model-$version-sources.jar
wget -c $baseurl/build/builder-model/$version/builder-model-$version-javadoc.jar -O builder-model-$version-javadoc.jar




localpath=$path/build/gradle-experimental/0.8.0
mkDirFile
cd $localpath
wget -c $baseurl/build/gradle-experimental/0.8.0/gradle-experimental-0.8.0.pom -O gradle-experimental-0.8.0.pom
wget -c $baseurl/build/gradle-experimental/0.8.0/gradle-experimental-0.8.0.jar -O gradle-experimental-0.8.0.jar
wget -c $baseurl/build/gradle-experimental/0.8.0/gradle-experimental-0.8.0-sources.jar -O gradle-experimental-0.8.0-sources.jar
wget -c $baseurl/build/gradle-experimental/0.8.0/gradle-experimental-0.8.0-javadoc.jar -O gradle-experimental-0.8.0-javadoc.jar



localpath=$path/build/gradle-experimental/0.9.0
mkDirFile
cd $localpath
wget -c $baseurl/build/gradle-experimental/0.9.0/gradle-experimental-0.9.0.pom -O gradle-experimental-0.9.0.pom
wget -c $baseurl/build/gradle-experimental/0.9.0/gradle-experimental-0.9.0.jar -O gradle-experimental-0.9.0.jar
wget -c $baseurl/build/gradle-experimental/0.9.0/gradle-experimental-0.9.0-sources.jar -O gradle-experimental-0.9.0-sources.jar
wget -c $baseurl/build/gradle-experimental/0.9.0/gradle-experimental-0.9.0-javadoc.jar -O gradle-experimental-0.9.0-javadoc.jar



localpath=$path/build/gradle-experimental/0.9.1
mkDirFile
cd $localpath
wget -c $baseurl/build/gradle-experimental/0.9.1/gradle-experimental-0.9.1.pom -O gradle-experimental-0.9.1.pom
wget -c $baseurl/build/gradle-experimental/0.9.1/gradle-experimental-0.9.1.jar -O gradle-experimental-0.9.1.jar
wget -c $baseurl/build/gradle-experimental/0.9.1/gradle-experimental-0.9.1-sources.jar -O gradle-experimental-0.9.1-sources.jar
wget -c $baseurl/build/gradle-experimental/0.9.1/gradle-experimental-0.9.1-javadoc.jar -O gradle-experimental-0.9.1-javadoc.jar


localpath=$path/build/manifest-merger/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/build/manifest-merger/$buildToolsVersion/manifest-merger-$buildToolsVersion.pom -O manifest-merger-$buildToolsVersion.pom
wget -c $baseurl/build/manifest-merger/$buildToolsVersion/manifest-merger-$buildToolsVersion.jar -O manifest-merger-$buildToolsVersion.jar
wget -c $baseurl/build/manifest-merger/$buildToolsVersion/manifest-merger-$buildToolsVersion-sources.jar -O manifest-merger-$buildToolsVersion-sources.jar
wget -c $baseurl/build/manifest-merger/$buildToolsVersion/manifest-merger-$buildToolsVersion-javadoc.jar -O manifest-merger-$buildToolsVersion-javadoc.jar


localpath=$path/build/transform-api/2.0.0-deprecated-use-gradle-api
mkDirFile
cd $localpath
wget -c $baseurl/build/transform-api/2.0.0-deprecated-use-gradle-api/transform-api-2.0.0-deprecated-use-gradle-api.pom -O transform-api-2.0.0-deprecated-use-gradle-api.pom
wget -c $baseurl/build/transform-api/2.0.0-deprecated-use-gradle-api/transform-api-2.0.0-deprecated-use-gradle-api.jar -O transform-api-2.0.0-deprecated-use-gradle-api.jar
wget -c $baseurl/build/transform-api/2.0.0-deprecated-use-gradle-api/transform-api-2.0.0-deprecated-use-gradle-api-sources.jar -O transform-api-2.0.0-deprecated-use-gradle-api-sources.jar
wget -c $baseurl/build/transform-api/2.0.0-deprecated-use-gradle-api/transform-api-2.0.0-deprecated-use-gradle-api-javadoc.jar -O transform-api-2.0.0-deprecated-use-gradle-api-javadoc.jar



localpath=$path/annotations/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/annotations/$buildToolsVersion/annotations-$buildToolsVersion.pom -O annotations-$buildToolsVersion.pom
wget -c $baseurl/annotations/$buildToolsVersion/annotations-$buildToolsVersion.jar -O annotations-$buildToolsVersion.jar
wget -c $baseurl/annotations/$buildToolsVersion/annotations-$buildToolsVersion-sources.jar -O annotations-$buildToolsVersion-sources.jar
wget -c $baseurl/annotations/$buildToolsVersion/annotations-$buildToolsVersion-javadoc.jar -O annotations-$buildToolsVersion-javadoc.jar




localpath=$path/sdklib/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/sdklib/$buildToolsVersion/sdklib-$buildToolsVersion.pom -O sdklib-$buildToolsVersion.pom
wget -c $baseurl/sdklib/$buildToolsVersion/sdklib-$buildToolsVersion.jar -O sdklib-$buildToolsVersion.jar
wget -c $baseurl/sdklib/$buildToolsVersion/sdklib-$buildToolsVersion-sources.jar -O sdklib-$buildToolsVersion-sources.jar
wget -c $baseurl/sdklib/$buildToolsVersion/sdklib-$buildToolsVersion-javadoc.jar -O sdklib-$buildToolsVersion-javadoc.jar


localpath=$path/repository/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/repository/$buildToolsVersion/repository-$buildToolsVersion.pom -O repository-$buildToolsVersion.pom
wget -c $baseurl/repository/$buildToolsVersion/repository-$buildToolsVersion.jar -O repository-$buildToolsVersion.jar
wget -c $baseurl/repository/$buildToolsVersion/repository-$buildToolsVersion-sources.jar -O repository-$buildToolsVersion-sources.jar
wget -c $baseurl/repository/$buildToolsVersion/repository-$buildToolsVersion-javadoc.jar -O repository-$buildToolsVersion-javadoc.jar


localpath=$path/dvlib/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/dvlib/$buildToolsVersion/dvlib-$buildToolsVersion.pom -O dvlib-$buildToolsVersion.pom
wget -c $baseurl/dvlib/$buildToolsVersion/dvlib-$buildToolsVersion.jar -O dvlib-$buildToolsVersion.jar
wget -c $baseurl/dvlib/$buildToolsVersion/dvlib-$buildToolsVersion-sources.jar -O dvlib-$buildToolsVersion-sources.jar
wget -c $baseurl/dvlib/$buildToolsVersion/dvlib-$buildToolsVersion-javadoc.jar -O dvlib-$buildToolsVersion-javadoc.jar



localpath=$path/common/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/common/$buildToolsVersion/common-$buildToolsVersion.pom -O common-$buildToolsVersion.pom
wget -c $baseurl/common/$buildToolsVersion/common-$buildToolsVersion.jar -O common-$buildToolsVersion.jar
wget -c $baseurl/common/$buildToolsVersion/common-$buildToolsVersion-sources.jar -O common-$buildToolsVersion-sources.jar
wget -c $baseurl/common/$buildToolsVersion/common-$buildToolsVersion-javadoc.jar -O common-$buildToolsVersion-javadoc.jar



localpath=$path/sdk-common/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/sdk-common/$buildToolsVersion/sdk-common-$buildToolsVersion.pom -O sdk-common-$buildToolsVersion.pom
wget -c $baseurl/sdk-common/$buildToolsVersion/sdk-common-$buildToolsVersion.jar -O sdk-common-$buildToolsVersion.jar
wget -c $baseurl/sdk-common/$buildToolsVersion/sdk-common-$buildToolsVersion-sources.jar -O sdk-common-$buildToolsVersion-sources.jar
wget -c $baseurl/sdk-common/$buildToolsVersion/sdk-common-$buildToolsVersion-javadoc.jar -O sdk-common-$buildToolsVersion-javadoc.jar


localpath=$path/analytics-library/protos/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/analytics-library/protos/$buildToolsVersion/protos-$buildToolsVersion.pom -O protos-$buildToolsVersion.pom
wget -c $baseurl/analytics-library/protos/$buildToolsVersion/protos-$buildToolsVersion.jar -O protos-$buildToolsVersion.jar
wget -c $baseurl/analytics-library/protos/$buildToolsVersion/protos-$buildToolsVersion-sources.jar -O protos-$buildToolsVersion-sources.jar
wget -c $baseurl/analytics-library/protos/$buildToolsVersion/protos-$buildToolsVersion-javadoc.jar -O protos-$buildToolsVersion-javadoc.jar


localpath=$path/analytics-library/shared/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/analytics-library/shared/$buildToolsVersion/shared-$buildToolsVersion.pom -O shared-$buildToolsVersion.pom
wget -c $baseurl/analytics-library/shared/$buildToolsVersion/shared-$buildToolsVersion.jar -O shared-$buildToolsVersion.jar
wget -c $baseurl/analytics-library/shared/$buildToolsVersion/shared-$buildToolsVersion-sources.jar -O shared-$buildToolsVersion-sources.jar
wget -c $baseurl/analytics-library/shared/$buildToolsVersion/shared-$buildToolsVersion-javadoc.jar -O shared-$buildToolsVersion-javadoc.jar



localpath=$path/analytics-library/tracker/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/analytics-library/tracker/$buildToolsVersion/tracker-$buildToolsVersion.pom -O tracker-$buildToolsVersion.pom
wget -c $baseurl/analytics-library/tracker/$buildToolsVersion/tracker-$buildToolsVersion.jar -O tracker-$buildToolsVersion.jar
wget -c $baseurl/analytics-library/tracker/$buildToolsVersion/tracker-$buildToolsVersion-sources.jar -O tracker-$buildToolsVersion-sources.jar
wget -c $baseurl/analytics-library/tracker/$buildToolsVersion/tracker-$buildToolsVersion-javadoc.jar -O tracker-$buildToolsVersion-javadoc.jar


localpath=$path/ddms/ddmlib/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/ddms/ddmlib/$buildToolsVersion/ddmlib-$buildToolsVersion.pom -O ddmlib-$buildToolsVersion.pom
wget -c $baseurl/ddms/ddmlib/$buildToolsVersion/ddmlib-$buildToolsVersion.jar -O ddmlib-$buildToolsVersion.jar
wget -c $baseurl/ddms/ddmlib/$buildToolsVersion/ddmlib-$buildToolsVersion-sources.jar -O ddmlib-$buildToolsVersion-sources.jar
wget -c $baseurl/ddms/ddmlib/$buildToolsVersion/ddmlib-$buildToolsVersion-javadoc.jar -O ddmlib-$buildToolsVersion-javadoc.jar


localpath=$path/layoutlib/layoutlib-api/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/layoutlib/layoutlib-api/$buildToolsVersion/layoutlib-api-$buildToolsVersion.pom -O layoutlib-api-$buildToolsVersion.pom
wget -c $baseurl/layoutlib/layoutlib-api/$buildToolsVersion/layoutlib-api-$buildToolsVersion.jar -O layoutlib-api-$buildToolsVersion.jar
wget -c $baseurl/layoutlib/layoutlib-api/$buildToolsVersion/layoutlib-api-$buildToolsVersion-sources.jar -O layoutlib-api-$buildToolsVersion-sources.jar
wget -c $baseurl/layoutlib/layoutlib-api/$buildToolsVersion/layoutlib-api-$buildToolsVersion-javadoc.jar -O layoutlib-api-$buildToolsVersion-javadoc.jar


localpath=$path/lint/lint/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/lint/lint/$buildToolsVersion/lint-$buildToolsVersion.pom -O lint-$buildToolsVersion.pom
wget -c $baseurl/lint/lint/$buildToolsVersion/lint-$buildToolsVersion.jar -O lint-$buildToolsVersion.jar
wget -c $baseurl/lint/lint/$buildToolsVersion/lint-$buildToolsVersion-sources.jar -O lint-$buildToolsVersion-sources.jar
wget -c $baseurl/lint/lint/$buildToolsVersion/lint-$buildToolsVersion-javadoc.jar -O lint-$buildToolsVersion-javadoc.jar



localpath=$path/lint/lint-api/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/lint/lint-api/$buildToolsVersion/lint-api-$buildToolsVersion.pom -O lint-api-$buildToolsVersion.pom
wget -c $baseurl/lint/lint-api/$buildToolsVersion/lint-api-$buildToolsVersion.jar -O lint-api-$buildToolsVersion.jar
wget -c $baseurl/lint/lint-api/$buildToolsVersion/lint-api-$buildToolsVersion-sources.jar -O lint-api-$buildToolsVersion-sources.jar
wget -c $baseurl/lint/lint-api/$buildToolsVersion/lint-api-$buildToolsVersion-javadoc.jar -O lint-api-$buildToolsVersion-javadoc.jar



localpath=$path/lint/lint-checks/$buildToolsVersion
mkDirFile
cd $localpath
wget -c $baseurl/lint/lint-checks/$buildToolsVersion/lint-checks-$buildToolsVersion.pom -O lint-checks-$buildToolsVersion.pom
wget -c $baseurl/lint/lint-checks/$buildToolsVersion/lint-checks-$buildToolsVersion.jar -O lint-checks-$buildToolsVersion.jar
wget -c $baseurl/lint/lint-checks/$buildToolsVersion/lint-checks-$buildToolsVersion-sources.jar -O lint-checks-$buildToolsVersion-sources.jar
wget -c $baseurl/lint/lint-checks/$buildToolsVersion/lint-checks-$buildToolsVersion-javadoc.jar -O lint-checks-$buildToolsVersion-javadoc.jar


localpath=$path/external/lombok/lombok-ast/0.2.3
mkDirFile
cd $localpath
wget -c $baseurl/external/lombok/lombok-ast/0.2.3/lombok-ast-0.2.3.pom -O lombok-ast-0.2.3.pom
wget -c $baseurl/external/lombok/lombok-ast/0.2.3/lombok-ast-0.2.3.jar -O lombok-ast-0.2.3.jar
wget -c $baseurl/external/lombok/lombok-ast/0.2.3/lombok-ast-0.2.3-sources.jar -O lombok-ast-0.2.3-sources.jar
wget -c $baseurl/external/lombok/lombok-ast/0.2.3/lombok-ast-0.2.3-javadoc.jar -O lombok-ast-0.2.3-javadoc.jar



localpath=$path/external/com-intellij/uast/162.2228.14
mkDirFile
cd $localpath
wget -c $baseurl/external/com-intellij/uast/162.2228.14/uast-162.2228.14.pom -O uast-162.2228.14.pom
wget -c $baseurl/external/com-intellij/uast/162.2228.14/uast-162.2228.14.jar -O uast-162.2228.14.jar
wget -c $baseurl/external/com-intellij/uast/162.2228.14/uast-162.2228.14-sources.jar -O uast-162.2228.14-sources.jar
wget -c $baseurl/external/com-intellij/uast/162.2228.14/uast-162.2228.14-javadoc.jar -O uast-162.2228.14-javadoc.jar


localpath=$path/jack/jack-api/0.11.0
mkDirFile
cd $localpath
wget -c $baseurl/jack/jack-api/0.11.0/jack-api-0.11.0.pom -O jack-api-0.11.0.pom
wget -c $baseurl/jack/jack-api/0.11.0/jack-api-0.11.0.jar -O jack-api-0.11.0.jar
wget -c $baseurl/jack/jack-api/0.11.0/jack-api-0.11.0-sources.jar -O jack-api-0.11.0-sources.jar
wget -c $baseurl/jack/jack-api/0.11.0/jack-api-0.11.0-javadoc.jar -O jack-api-0.11.0-javadoc.jar


localpath=$path/jack/jack-api/0.10.0
mkDirFile
cd $localpath
wget -c $baseurl/jack/jack-api/0.10.0/jack-api-0.10.0.pom -O jack-api-0.10.0.pom
wget -c $baseurl/jack/jack-api/0.10.0/jack-api-0.10.0.jar -O jack-api-0.10.0.jar
wget -c $baseurl/jack/jack-api/0.10.0/jack-api-0.10.0-sources.jar -O jack-api-0.10.0-sources.jar
wget -c $baseurl/jack/jack-api/0.10.0/jack-api-0.10.0-javadoc.jar -O jack-api-0.10.0-javadoc.jar


localpath=$path/jack/jack-api/0.13.0
mkDirFile
cd $localpath
wget -c $baseurl/jack/jack-api/0.13.0/jack-api-0.13.0.pom -O jack-api-0.13.0.pom
wget -c $baseurl/jack/jack-api/0.13.0/jack-api-0.13.0.jar -O jack-api-0.13.0.jar
wget -c $baseurl/jack/jack-api/0.13.0/jack-api-0.13.0-sources.jar -O jack-api-0.13.0-sources.jar
wget -c $baseurl/jack/jack-api/0.13.0/jack-api-0.13.0-javadoc.jar -O jack-api-0.13.0-javadoc.jar


localpath=$path/jill/jill-api/0.10.0
mkDirFile
cd $localpath
wget -c $baseurl/jill/jill-api/0.10.0/jill-api-0.10.0.pom -O jill-api-0.10.0.pom
wget -c $baseurl/jill/jill-api/0.10.0/jill-api-0.10.0.jar -O jill-api-0.10.0.jar
wget -c $baseurl/jill/jill-api/0.10.0/jill-api-0.10.0-sources.jar -O jill-api-0.10.0-sources.jar
wget -c $baseurl/jill/jill-api/0.10.0/jill-api-0.10.0-javadoc.jar -O jill-api-0.10.0-javadoc.jar
