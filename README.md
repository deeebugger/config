# Configs Repo
This repo will be used to maintain Jenkins pipeline job script, argocd installation steps and kubernetes template file.

## Jenkins files
Jenkins_build_pipeline.groovy (will be used to run the docker image build steps)
Jenkins_deploy_pipeline.groovy (will be used to get the latest image version and push config changes

## Argocd
Argocd is the continous deployment tool which will connect to this repo using ssh private key. 

### Setup steps

#### Install
1 Run `argocd_tool/install.sh`

2 Get your login password using `kubectl -n argocd get secret argocd-initial-admin-secret -o jsonpath="{.data.password}" | base64 -d; echo`. Default user is `admin`.

3 Connect your cluster with git repo using SSH key based authentication under settings >> repositories >> connect repo using SSH.

For more information refer to https://argo-cd.readthedocs.io/en/stable/getting_started/ 


#### Configure deploy job
After install the argocd, deploy `argocd_tool/dev-deployment-app.yml` on your cluster. This will pull latest changes and deploy the resources.
