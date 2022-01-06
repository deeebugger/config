#!/bin/bash
cp -f templates/deployments/dev-deployment.yml environments/DEV/deployments/deployment.yml
cp -f templates/deployments/stg-deployment.yml environments/STG/deployments/deployment.yml
find environments/ -name "deployment.yml" -type f -exec sed -i "s/TagVersion/$1/g" {} \;
find environments/ -name "deployment.yml" -type f -exec sed -i "s/DockerUser/$2/g" {} \;


