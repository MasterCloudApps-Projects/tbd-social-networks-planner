
## EXTERNAL APIs: TWITTER AND INSTAGRAM

This application interacts with both Twitter and Instagram Graph Api. For each API we use a different way of communication. We want to explore the possibilities that we have. So, for one API we have used a library and for the other one we made the calls directly to the API.

## Twitter API:

To communicate our application with Twitter, we have created a Twitter Developer Account. Once we created the account, we create our app project to publish on behalf the user.
The twitter project is: social-networks-planner.
When you post a tweet using our application you will see a reference (next to the date and hour of the tweet) of this app. To post on behalf other user, the user has to grant permissions to our application.

**Steps to grant permissions to tbd-social-networks-planner:**
- First of all the user has to call the auth endpoint: https://ais-tbd-social-networks.herokuapp.com/api/v1/twitter/auth

- Then he has to visit the url provided to grant permissions to the app. Once he pushes the button, Twitter API will callbacks tbd-social-networks-api to finish the authentication process.

Now the user can post, retweet, like or whatever using our API. 

To manage all the credentials and tokens related to this process, and also to make all the authenticated calls to Twitter API we have used **Twitter4J** library.

**Change account:**

If the user wants to use a different account, but he is already logged in, he just goes to auth endpoint again. The authentication process will be restarted.

## Instagram Graph API:

In this case, we are sending the request using **RestTemplate** direct to Instagram Graph API. This time we avoid using any library. 

To grant permissions to our app the process was pretty similar to the twitter one. We have created a facebook developer account and also a project: tbd-social-network-planner.

**Steps to grant permissions to tbd-social-networks-planner:**

- The user has to call the endpoint: https://ais-tbd-social-networks.herokuapp.com/api/v1/instagram/login
- Then the user has to visit the url and enter the code provided in the response.  
- Call manually the callback endpoint once he entered the code provided: https://ais-tbd-social-networks.herokuapp.com/api/v1/instagram/login/callback/
  
Now the app will be granted to post on instagram.
  
To be able to use this API Graph the instagram account must be *Business Account* and have to *be linked to Facebook Page*. These requirements come from Facebook. 

**Change account:**

If the user wants to use a different account, but he is already logged in, he just goes to auth endpoint again. The authentication process will be restarted.


Here we had a problem and for this moment we cannot avoid it due to Facebook requirements. We weren't able to configure the Facebook Login callback, so the user has to call manually the endpoint once he enter the code provided and push the button.
After that, the user is able to post on instagram images and read about his user’s account using tbd-social-networks-planner

:warning: **Be careful!** :warning: At this moment we have no developed any "user manager", so if you authenticate your user with your account and other person enters at this time in our app located in heroku may post with your session. 
