#!/bin/sh
# ----------------------------------------------------------------------------
# Maven Start Up Batch script (Unix shell script)
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
# Optional ENV vars:
# ------------------
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
# ----------------------------------------------------------------------------

# Determine script directory
MAVEN_PROJECTBASEDIR="${BASH_SOURCE[0]}"
while [ -h "$MAVEN_PROJECTBASEDIR" ] ; do
  ls=$(ls -ld "$MAVEN_PROJECTBASEDIR")
  link=$(expr "$ls" : '.*-> \(.*\)$')
  if expr "$link" : '/.*' > /dev/null; then
    MAVEN_PROJECTBASEDIR="$link"
  else
    MAVEN_PROJECTBASEDIR=$(dirname "$MAVEN_PROJECTBASEDIR")/"$link"
  fi
done
MAVEN_PROJECTBASEDIR=$(dirname "$MAVEN_PROJECTBASEDIR")

MAVEN_WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
MAVEN_WRAPPER_PROPERTIES="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.properties"

# Default JVM options
MAVEN_OPTS="${MAVEN_OPTS:--Xmx512m}"

# Determine Java executable
if [ -n "$JAVA_HOME" ]; then
  JAVACMD="$JAVA_HOME/bin/java"
else
  JAVACMD="java"
fi

exec "$JAVACMD" \
  $MAVEN_OPTS \
  -classpath "$MAVEN_WRAPPER_JAR" \
  "-Dmaven.home=${M2_HOME}" \
  "-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}" \
  org.apache.maven.wrapper.MavenWrapperMain "$@"
