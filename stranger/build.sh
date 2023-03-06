
PROJECT_DIR=$PWD

echo "starting maven build ..."
mvn clean compile install -DskipTests

BUILD_STATUS=$?
echo "maven build status is - $BUILD_STATUS"

if [ "$BUILD_STATUS" -ne 0 ]
then
  echo "maven build failed"
  exit 1
fi

echo "maven build success, copying jar file..."
mkdir -p $PROJECT_DIR/target/

cp $PROJECT_DIR/stranger-app/src/main/resources/app  $PROJECT_DIR/target/
cp $PROJECT_DIR/stranger-app/target/stranger-app-1.0-SNAPSHOT.jar $PROJECT_DIR/target/app/lib