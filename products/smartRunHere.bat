call FindJavaHome.bat
set path=%JAVA_HOME%;%path%
@start javaw -Duser.home="." -mx512m -cp mail.jar;activation.jar;RealmSpeakFull.jar com.robin.magic_realm.RealmSpeak.RealmSpeakFrame %1