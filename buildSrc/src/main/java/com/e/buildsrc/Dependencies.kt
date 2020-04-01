object ApplicationId {
    const val id = "com.bicifiapp"
}

private object Modules {
    const val app = ":app"
    const val notificationssettings = ":notificationssettings"
}

object Releases {
    const val versionCode = 1
    const val versionName = "1.0"
}

object Versions {
    const val gradle = "3.5.1"
    const val kotlin = "1.3.41"
    const val recycler = "1.0.0"
    const val glide = "4.10.0"
    const val crashlytics = "2.10.1"
    const val koin = "2.0.1"
    const val timber = "4.7.1"
    const val design = "1.0.0"
    const val appcompat = "1.1.0"
    const val ktx = "1.2.0"
    const val junit = "4.12"
    const val constraint = "1.1.3"
    const val runner = "1.2.0"
    const val expresso = "3.2.0"
    const val lifecycle = "2.2.0"
    const val firebaseAuth = "19.2.0"
    const val firebaseAnalytics = "17.2.3"
    const val firebaseInAppMessage = "19.0.3"
    const val firebasePerf = "19.0.5"
    const val mockitoCore = "3.0.0"
    const val mockitoInline = "3.0.0"
    const val coroutinesTest = "1.3.3"
    const val mockk = "1.9.3"
    const val spekDsl = "2.0.9"
    const val spekRunner = "2.0.9"
    const val onboarder = "1.0.4"
    const val fragmentKtx = "1.2.3"
    const val constraintLayout = "1.1.3"
    const val facebook = "[4,5)"
    const val google = "17.0.0"
    const val firestoreKtx = "21.4.2"
    const val androidTools = "3.6.1"
    const val googleServices = "4.3.3"

    const val legacySupport = "1.0.0"
    const val compileSdk = 29
    const val buildToolsVersion = "29.0.2"
    const val minSdk = 21
    const val targetSdk = 29
}

object Classpath {
    const val androidTools = "com.android.tools.build:gradle:${Versions.androidTools}"
    const val kotlinGradlePlugin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Versions.kotlin}"
    const val googleServices = "com.google.gms:google-services:${Versions.googleServices}"
}

object Libraries {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val kotlinJdk8 = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:${Versions.kotlin}"
    const val ktx = "androidx.core:core-ktx:${Versions.ktx}"
    const val fragmentKtx = "androidx.fragment:fragment-ktx:${Versions.fragmentKtx}"
    const val recyclerview = "androidx.recyclerview:recyclerview:${Versions.recycler}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val glideCompile = "com.github.bumptech.glide:compiler:${Versions.glide}"
    const val koinAndroid = "org.koin:koin-android:${Versions.koin}"
    const val koinViewModel = "org.koin:koin-androidx-viewmodel:${Versions.koin}"
    const val timber = "com.jakewharton.timber:timber:${Versions.timber}"
    const val lifecycleExtensions = "androidx.lifecycle:lifecycle-extensions:${Versions.lifecycle}"
    const val lifecycleCompiler = "androidx.lifecycle:lifecycle-compiler:${Versions.lifecycle}"
    const val lifecycleRuntime = "androidx.lifecycle:lifecycle-runtime-ktx:${Versions.lifecycle}"
    const val lifecycle = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.lifecycle}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val appCompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val legacySupport = "androidx.legacy:legacy-support-v4:${Versions.legacySupport}"
}

object ExternalLibraries {
    const val onboarder = "com.codemybrainsout.onboarding:onboarder:${Versions.onboarder}"
    const val google = "com.google.android.gms:play-services-auth:${Versions.google}"
    const val facebookAuth = "com.facebook.android:facebook-android-sdk:${Versions.facebook}"
}

object SupportLibraries {
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val design = "com.google.android.material:material:${Versions.design}"
    const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.constraint}"
}

object Testing {
    const val junit = "junit:junit:${Versions.junit}"
    const val runner = "androidx.test:runner:${Versions.runner}"
    const val expresso = "androidx.test.espresso:espresso-core:${Versions.expresso}"
    const val mockitoCore = "org.mockito:mockito-core:${Versions.mockitoCore}"
    const val mockitoInline = "org.mockito:mockito-inline:${Versions.mockitoInline}"
    const val coroutinesTest =
        "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutinesTest}"
    const val mockk = "io.mockk:mockk:${Versions.mockk}"
    const val spekDsl = "org.spekframework.spek2:spek-dsl-jvm:${Versions.spekDsl}"
    const val spekRunner = "org.spekframework.spek2:spek-runner-junit5:${Versions.spekRunner}"
}

object TestLibraries {
    const val junit = "junit:junit:${Versions.junit}"
}

object CommonModules {

}

object Firebase {
    const val firebaseAuth = "com.google.firebase:firebase-auth:${Versions.firebaseAuth}"
    const val firebaseAnalytics =
        "com.google.firebase:firebase-analytics:${Versions.firebaseAnalytics}"
    const val firebaseInAppMessaging =
        "com.google.firebase:firebase-inappmessaging-display:${Versions.firebaseInAppMessage}"
    const val firebasePerf = "com.google.firebase:firebase-perf:${Versions.firebasePerf}"
    const val crashlytics = "com.crashlytics.sdk.android:crashlytics:${Versions.crashlytics}"
    const val firestoreKtx = "com.google.firebase:firebase-firestore-ktx:${Versions.firestoreKtx}"
}