#!/bin/sh

downloadPlugin() {
    local name=$1
    local version=$2

    local plugin_file=$PLUGIN_DIR/$name-$version.jar
if [ ! -f "$plugin_file" ]; then
    echo "Downloading plugin: $plugin_file"
    curl -o $plugin_file "https://binaries.sonarsource.com/Distribution/$name/$name-$version.jar"
fi
}


# Create directories
echo "Creating necessary directories"
BASE_DIR=$(pwd)
CONF_DIR=$BASE_DIR/conf
EXTENSION_DIR=$BASE_DIR/extensions
LOG_DIR=$BASE_DIR/logs
DATA_DIR=$BASE_DIR/data

mkdir -p $CONF_DIR
mkdir -p $EXTENSION_DIR
mkdir -p $LOG_DIR
mkdir -p $DATA_DIR


# Download kotlin language plugin
PLUGIN_DIR=$EXTENSION_DIR/plugins
mkdir -p $PLUGIN_DIR

downloadPlugin 'sonar-kotlin-plugin' '1.5.0.315'
downloadPlugin 'sonar-java-plugin' '6.0.2.20657'
downloadPlugin 'sonar-jacoco-plugin' '1.0.2.475'


## Run sonarqube
#echo "Running sonarqube"
#docker run --rm \
#-p 9000:9000 \
#-v $CONF_DIR:/opt/sonarqube/conf \
#-v $EXTENSION_DIR:/opt/sonarqube/extensions \
#-v $LOG_DIR:/opt/sonarqube/logs \
#-v $DATA_DIR:/opt/sonarqube/data \
#sonarqube:7.9.2-community
