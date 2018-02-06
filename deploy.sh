#!/bin/bash
#   PAW Deploying script by Julian Benitez and Juan Franco Caracciolo
#   Configure your variables on the following lines
#   All rights reserved - PawGroup4

USERNAME=paw-2017a-4
PASSWORD=ooc4Choo
PAWSERVER=10.16.1.110
WEBAPP="webapp"
DEVELOPMENT=default
PRODUCTION=production
# This script will automatically change your spring profile on the web.xml file
# Make Sure you have set the spring.profiles.active param to either DEVELOPMENT or PRODUCTION in your web.xml
# If all you want is to deploy as if, the run the -no-pck flag
# This script must be placed on the root of your project
#
# END OF CONFIGURATION


WEBXML="${WEBAPP}/src/main/webapp/WEB-INF/web.xml"
LOCALF=0
PKGF=1
DEPLOYF=1
WARNF=1
ERRORF=0
LINENUMBER=$(cat ${WEBXML} | grep -n 'spring.profiles.active' | cut -d : -f 1)
LINENUMBER=$((LINENUMBER + 1))

F_READ_RET=""
function readFile {
    F_READ_RET=""
    F_READ_RET=$(cat ${WEBXML} | sed -n ${LINENUMBER},${LINENUMBER}p | egrep -o "${DEVELOPMENT}|${PRODUCTION}")
      if [ ${F_READ_RET} == "" ]
      then
           echo "Error reading ${WEBXML} , Make Sure you have set the spring.profiles.active param to either ${DEVELOPMENT} or ${PRODUCTION} in your web.xml"
           exit 0
      fi
}
readFile
LASTCONF=${F_READ_RET}

function replace {
           readFile
           sed -i ${LINENUMBER}s/${F_READ_RET}/${1}/ ${WEBXML}
}

while test $# -gt 0; do
        case "$1" in
                -h|--help)
                        echo "-local                    package for a local repository and avoid deploying"
                        echo " "
                        echo "-no-pkg                   avoid recompiling the package"
                        echo " "
                        echo "-no-deploy                avoid deploying the webapp"
                        echo " "
                        echo "-h, --help                show brief help"
                        echo " "
                        echo "-no-warning               wont check for warnings when packaging"
                        exit 0
                        ;;
                -local)
                        LOCALF=1
                        DEPLOYF=0
                        ;;
                -no-pkg)
                        PKGF=0
                        ;;
                -no-deploy)
                        DEPLOYF=0
                        ;;
                -no-warning)
                        WARNF=0
                        ;;
        esac
        shift
done

if [ ${LOCALF} -eq 1 ]
then
    echo "Building with ${DEVELOPMENT} profile"
    replace ${DEVELOPMENT}
else
    echo "Building with ${PRODUCTION} profile"
    replace ${PRODUCTION}
fi

if [ ${PKGF} -eq 0 ]
then
    if [ ${DEPLOYF} -eq 0 ]
    then
    echo "Nothing was done"
    replace ${LASTCONF}
    exit 0
    fi
fi

if [ ${PKGF} -eq 1 ]
then
    echo "Building package......"
    MATCH="ERROR"
    if [ ${WARNF} -eq 1 ]
    then
        MATCH="WARNING|ERROR"
    fi
    mvn clean package | egrep ${MATCH} && ERRORF=1

   if [ ${ERRORF} -eq 1 ]
   then
        echo "Error building the package"
        replace ${LASTCONF}
        exit -1
   fi

   echo "Package Built Successfully"
   replace ${LASTCONF}

fi

if [ ${DEPLOYF} -eq 1 ]
then
    echo "Deploying..."
    if [ ${PKGF} -eq 1 ]
    then
        echo removing old pkg
        rm app.war
        mv ${WEBAPP}/target/webapp.war app.war
    fi
    echo "Enter username"
    read username
    echo "Enter password"
    read -s pass
    filename=app.war
    cmd=" echo -ne \"cd web/ \n put $filename \n \" | sshpass -p ${PASSWORD} sftp -oStrictHostKeyChecking=no ${USERNAME}@${PAWSERVER} "
    echo $cmd
    echo -ne "pwd\n put $filename\n" | sshpass -p $pass sftp $username@pampero.itba.edu.ar && echo -ne $cmd | sshpass -p $pass ssh $username@pampero.itba.edu.ar
    echo "Deployed"
fi