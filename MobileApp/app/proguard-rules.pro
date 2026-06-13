# Rules for R8 / ProGuard when building a release with isMinifyEnabled = true.
# Used by app/build.gradle.kts:
# release {
#     isMinifyEnabled = true
#     isShrinkResources = true
# }

# ---------------------------------------------------------------------
# Kotlin metadata / annotations
# ---------------------------------------------------------------------
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes InnerClasses
-keepattributes EnclosingMethod
-keepattributes Exceptions
-keepattributes RuntimeVisibleAnnotations
-keepattributes RuntimeVisibleParameterAnnotations
-keepattributes AnnotationDefault

-keepclassmembers class kotlin.Metadata {
    public <methods>;
}

# ---------------------------------------------------------------------
# AndroidX / AppCompat / Material
# ---------------------------------------------------------------------
-dontwarn androidx.appcompat.**
-dontwarn com.google.android.material.**

# ---------------------------------------------------------------------
# Jetpack Compose
# AGP and Compose usually provide most required rules automatically.
# ---------------------------------------------------------------------
-dontwarn androidx.compose.**

# ---------------------------------------------------------------------
# Firebase / Google Play Services
# Firebase libraries usually ship consumer ProGuard rules,
# but these dontwarn lines prevent noisy release builds.
# ---------------------------------------------------------------------
-dontwarn com.google.firebase.**
-dontwarn com.google.android.gms.**
-dontwarn com.google.android.play.core.**

# ---------------------------------------------------------------------
# Room
# ---------------------------------------------------------------------
-keep class androidx.room.** { *; }
-keep @androidx.room.Entity class * { *; }
-keep class * extends androidx.room.RoomDatabase { <init>(...); }

-keepclassmembers class ** {
    @androidx.room.* <methods>;
}

-keepclassmembers class * {
    @androidx.room.ColumnInfo <fields>;
    @androidx.room.PrimaryKey <fields>;
    @androidx.room.Embedded <fields>;
    @androidx.room.Relation <fields>;
}

-keep class **_Impl { *; }

-dontwarn androidx.room.paging.**

# ---------------------------------------------------------------------
# Moshi with KSP codegen
# ---------------------------------------------------------------------
# Official Moshi/R8 rules are mostly supplied by Moshi itself,
# but these help when DTOs/adapters are used reflectively or from Retrofit.
-keep,allowobfuscation,allowshrinking @interface com.squareup.moshi.JsonClass

-keep @com.squareup.moshi.JsonClass class * { *; }

-keepclassmembers @com.squareup.moshi.JsonClass class * {
    @com.squareup.moshi.Json <fields>;
}

-if @com.squareup.moshi.JsonClass class *
-keep class <1>JsonAdapter {
    <init>(...);
    <fields>;
}

-dontwarn com.squareup.moshi.**
-dontwarn javax.annotation.**

# ---------------------------------------------------------------------
# Gson
# ---------------------------------------------------------------------
# Needed if Retrofit converter-gson is used for your API DTOs.
# If your DTOs use @SerializedName, keep annotated fields.
-keepattributes Signature
-keepattributes *Annotation*

-keep class com.google.gson.reflect.TypeToken { *; }
-keep class * extends com.google.gson.reflect.TypeToken

-keepclassmembers,allowobfuscation class * {
    @com.google.gson.annotations.SerializedName <fields>;
}

-dontwarn sun.misc.**

# ---------------------------------------------------------------------
# Retrofit
# ---------------------------------------------------------------------
# Keep service interface methods with @retrofit2.http.* annotations;
# otherwise R8 may strip information Retrofit needs to build requests.
-keep,allowobfuscation,allowshrinking interface retrofit2.Call
-keep,allowobfuscation,allowshrinking class retrofit2.Response

-keepclasseswithmembers,includedescriptorclasses class * {
    @retrofit2.http.* <methods>;
}

-keepclassmembers,allowshrinking,allowobfuscation interface * {
    @retrofit2.http.* <methods>;
}

-dontwarn retrofit2.**
-dontwarn javax.annotation.Nullable
-dontwarn javax.annotation.ParametersAreNonnullByDefault

# ---------------------------------------------------------------------
# OkHttp / Okio
# ---------------------------------------------------------------------
# OkHttp includes its own consumer rules, but these are common safe additions.
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.codehaus.mojo.animal_sniffer.*
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**

# ---------------------------------------------------------------------
# Kotlin Coroutines
# ---------------------------------------------------------------------
-keepclassmembers class kotlinx.coroutines.** {
    volatile <fields>;
}

-dontwarn kotlinx.coroutines.**

# ---------------------------------------------------------------------
# Coil
# ---------------------------------------------------------------------
-dontwarn coil.**

# ---------------------------------------------------------------------
# osmdroid
# ---------------------------------------------------------------------
# osmdroid uses reflection in several places.
-keep class org.osmdroid.** { *; }
-dontwarn org.osmdroid.**

# osmdroid may reference optional Apache HTTP / SLF4J classes depending on usage.
-dontwarn org.apache.http.**
-dontwarn org.slf4j.**

# ---------------------------------------------------------------------
# App models / API DTOs
# ---------------------------------------------------------------------
# If your network/database model classes are in specific packages,
# keeping them reduces risk with Gson/Moshi/Room reflection.
# Adjust package names if your project uses different folders.
-keep class com.karandaev.roadCrackDetector.data.** { *; }
-keep class com.karandaev.roadCrackDetector.domain.model.** { *; }
-keep class com.karandaev.roadCrackDetector.model.** { *; }
-keep class com.karandaev.roadCrackDetector.network.** { *; }

# ---------------------------------------------------------------------
# Tests / debug-only references
# ---------------------------------------------------------------------
-dontwarn org.junit.**
-dontwarn org.hamcrest.**