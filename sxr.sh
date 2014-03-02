sbt 'project core' sxr &&
cd core/target/scala-2.10/classes.sxr/ &&
zip -r ghscala.zip ./* &&
mv ghscala.zip ~/Dropbox/

