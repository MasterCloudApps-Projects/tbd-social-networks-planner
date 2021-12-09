## GitHub Actions:
We have two workflows running in GitHub:
- `main.yml`: Triggered on pushing to main.
  The first runs the tests, if everything goes okay, then builds the project. After that, it generates an image and publishes it into Heroku Registry and DockerHub, finally deploys the application in Heroku.
  
- `main-pr.yml`: Triggered on a pull request over main.
The second one just runs the tests, in case everything goes right, it builds the project.
  
These two workflows are used to be more confident with the changes on our multiple deploys to productions, due to TBD methodology. Be sure that everything is working before the deploy is the key to continue uploading changes with no crashes and let the rest of the team work as well.

You can visit the files of the workflows to know more about them:
- [main.yml](https://github.com/MasterCloudApps-Projects/tbd-social-networks-planner/blob/main/.github/workflows/main.yml)
- [main-pr.yml](https://github.com/MasterCloudApps-Projects/tbd-social-networks-planner/blob/main/.github/workflows/main-pr.yml) 
