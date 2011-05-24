#!/bin/sh
echo "<root><version>" > version.xml
cat ../Azuremd.Backend/src/org/azuremd/backend/Application.java | grep BackendVersion\( | cut -f 2 -d \" >> version.xml
echo "</version></root>" >> version.xml