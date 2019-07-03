# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/malin/sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript inter
# class:
#-keepclassmembers class fqcn.of.javascript.inter.for.webview {
#   public *;
#}

#参考文章
#https://developer.android.com/studio/build/shrink-code.html#unused-alt-resources

#-------------------------------optimize-------------------------------
#https://developer.android.com/studio/build/shrink-code.html#unused-alt-resources
#/sdk/tools/proguard/proguard-android-optimize.txt
#-------------------------------common-------------------------------
#https://developer.android.com/studio/build/shrink-code.html#unused-alt-resources
#https://medium.com/google-developers/practical-proguard-rules-examples-5640a3907dc9
# 混淆时所用的算法
-optimizations !code/simplification/arithmetic,!code/simplification/cast,!field/*,!class/merging/*
#代码压缩级别
-optimizationpasses 7
-dontshrink
-flattenpackagehierarchy
-verbose
-dontskipnonpubliclibraryclassmembers
-ignorewarnings


#混淆时预校验
-dontpreverify

#使用大小写混合
-dontusemixedcaseclassnames

# 指定不去忽略非公共的库类。
-dontskipnonpubliclibraryclasses

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#记录日志
-verbose

#忽略警告，避免打包时某些警告出现
-ignorewarnings

#保留Annotation不混淆,避免混淆泛型;抛出异常时保留代码行号
-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,*Annotation*,EnclosingMethod,LocalVariableTable,*JavascriptInterface*
###keepattributes###


# 保持哪些类不被混淆
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.google.vending.licensing.ILicensingService
-keep public class com.android.vending.licensing.ILicensingService
-keep public class * extends java.lang.Throwable {*;}
-keep public class * extends java.lang.Exception {*;}

-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v7.app.AppCompatActivity
#-------------------------------common-------------------------------


#-------------------------------通用混淆-------------------------------
-renamesourcefileattribute SourceFile

# For native methods, see http://proguard.sourceforge.net/manual/examples.html#native
-keepclasseswithmembernames class * {
    native <methods>;
}

# keep setters in Views so that animations can still work.
# see http://proguard.sourceforge.net/manual/examples.html#beans
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}

# We want to keep methods in Activity that could be used in the XML attribute onClick
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}

# For enumeration classes, see http://proguard.sourceforge.net/manual/examples.html#enumerations
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}


#不混淆Parcelable类方法
-keepclassmembers class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator CREATOR;
}

-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

-keep class **.R
-keepclassmembers class **.R$* {
    public static <fields>;
}


# The support library contains references to newer platform versions.
# Don't warn about those in case this app is linking against an older
# platform version.  We know about them, and they are safe.
-dontwarn android.support.**

#-------------------------------Understand the @Keep support annotation.-------------------------------
-keep class android.support.annotation.Keep

-keep @android.support.annotation.Keep class * {*;}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <methods>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <fields>;
}

-keepclasseswithmembers class * {
    @android.support.annotation.Keep <init>(...);
}
#-------------------------------Understand the @Keep support annotation.-------------------------------


# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}

# 保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}

#-------------------------------通用混淆-------------------------------



#-------------------------------RxJava-------------------------------
#http://www.on1024.com/2016/01/13/%E5%8D%87%E7%BA%A7%E5%88%B0%20RxAndroid%201.x.x%20%E4%B9%8B%E5%90%8E%E6%B7%B7%E6%B7%86%E8%A6%81%E6%B3%A8%E6%84%8F%E7%9A%84/
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
 long producerIndex;
 long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
 rx.internal.util.atomic.LinkedQueueNode consumerNode;
}

-dontnote rx.**
#-------------------------------RxJava-------------------------------


#R
-keep public class meizhi.meizhi.malin.R$*{
		public static final int *;
}

#BuildConfig
-keep class meizhi.meizhi.malin.BuildConfig { *; }
#Binder
-keep public class * extends android.os.Binder

##自定义控件不参与混淆
-keep class meizhi.meizhi.malin.view.** { *; }
-keep class meizhi.meizhi.malin.widget.** { *; }
-dontwarn meizhi.meizhi.malin.view.**
-dontwarn meizhi.meizhi.malin.widget.**

# Keep the support library
-keep class android.support.** { *; }
-keep interface android.support.** { *; }



#-------------------------------retrofit2-------------------------------
#https://github.com/square/retrofit/blob/master/retrofit/src/main/resources/META-INF/proguard/retrofit2.pro
# Retrofit does reflection on generic parameters. InnerClasses is required to use Signature and
# EnclosingMethod is required to use InnerClasses.
-keepattributes Signature, InnerClasses, EnclosingMethod

# Retrofit does reflection on method and parameter annotations.
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations

# Retain service method parameters when optimizing.
-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

# Ignore annotation used for build tooling.
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement

# Ignore JSR 305 annotations for embedding nullability information.
-dontwarn javax.annotation.**

# Guarded by a NoClassDefFoundError try/catch and only used when on the classpath.
-dontwarn kotlin.Unit

# Top-level functions that can only be used by Kotlin.
-dontwarn retrofit2.KotlinExtensions

# With R8 full mode, it sees no subtypes of Retrofit interfaces since they are created with a Proxy
# and replaces all potential values with null. Explicitly keeping the interfaces prevents this.
-if interface * { @retrofit2.http.* <methods>; }
-keep,allowobfuscation interface <1>
#-------------------------------retrofit2-------------------------------

#-------------------------------okhttp-------------------------------
#https://github.com/square/okhttp/blob/master/okhttp/src/main/resources/META-INF/proguard/okhttp3.pro
# JSR 305 annotations are for embedding nullability information.
-dontwarn javax.annotation.**

# A resource is loaded with a relative path so the package of this class must be preserved.
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase

# Animal Sniffer compileOnly dependency to ensure APIs are compatible with older versions of Java.
-dontwarn org.codehaus.mojo.animal_sniffer.*

# OkHttp platform used only on JVM and when Conscrypt dependency is available.
-dontwarn okhttp3.internal.platform.ConscryptPlatform
#-------------------------------okhttp-------------------------------


#-------------------------------Gson-------------------------------
##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { <fields>; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

# Prevent R8 from leaving Data object members always null
-keepclassmembers,allowobfuscation class * {
  @com.google.gson.annotations.SerializedName <fields>;
}

##---------------End: proguard configuration for Gson  ----------
#-------------------------------Gson-------------------------------


#-------------------------------rxjava-------------------------------
#https://github.com/ReactiveX/RxJava/issues/6243#issuecomment-428801060
#RxJava 2.x doesn't need proguard rules
#-------------------------------rxjava-------------------------------

#-------------------------------rxandroid-------------------------------
#https://github.com/ReactiveX/RxAndroid/issues/350#issuecomment-403863741
#ProGuard rules. Nothing requires ProGuard.
#-------------------------------rxandroid-------------------------------

#-------------------------------PersistentCookieJar-------------------------------
#https://github.com/franmontiel/PersistentCookieJar
#ProGuard rules. Nothing requires ProGuard.
#-------------------------------PersistentCookieJar-------------------------------


# Remove logging calls
-assumenosideeffects class android.util.Log {
    public static boolean isLoggable(java.lang.String, int);
    public static int v(...);
    public static int i(...);
    public static int w(...);
    public static int d(...);
    public static int e(...);
}