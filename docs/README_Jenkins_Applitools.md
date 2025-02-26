# Applitools Setup in Jenkins Server

### Step 1 - Install [**Applitools Eyes Plugin**](https://plugins.jenkins.io/applitools-eyes/)

NOTE: We are using a [pipeline project](../JenkinsFile). So the Applitools Plugin setup is done based on these [instructions](https://plugins.jenkins.io/applitools-eyes/#plugin-content-in-case-of-a-pipeline-project)
    
### Step 2 - Add APPLITOOLS_API_KEY as credentials in Jenkins Server

* Dashboard --> Manage Jenkins 

    <p>
      <img src="ApplitoolsConfiguration/1.%20ManageJenkins.png" width="360" alt="accessibility text">
    </p>

* Dashboard --> Manage Jenkins --> Credentials
    <p>
      <img src="ApplitoolsConfiguration/2.%20System_GlobalCredentials.png" width="360" alt="accessibility text">
    </p>

* Dashboard --> Manage Jenkins --> Credentials --> System --> Global credentials (unrestricted)

    <p>
      <img src="ApplitoolsConfiguration/3.%20AddCredentials.png" width="360" alt="accessibility text">
    </p>

* Add new credentials of type - **Secret Text**
    <p>
      <img src="ApplitoolsConfiguration/4.%20SecretText.png" width="360" alt="accessibility text">
    </p>

* Create a new Secret Text for **APPLITOOLS_API_KEY**
  * Secret -> Enter your APPLITOOLS_API_KEY
  * ID -> **APPLITOOLS_API_KEY**
  * Description -> **APPLITOOLS_API_KEY**
      <p>
        <img src="ApplitoolsConfiguration/5.%20AddSecretKey.png" width="360" alt="accessibility text">
      </p>

* A new Secret Text for **APPLITOOLS_API_KEY** is now created
      <p>
        <img src="ApplitoolsConfiguration/6.%20KeyIsAdded.png" width="360" alt="accessibility text">
      </p>
