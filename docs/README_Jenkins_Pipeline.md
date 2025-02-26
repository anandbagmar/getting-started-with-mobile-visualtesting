# Jenkins Pipeline Setup

### Step 1 - Create a new job

Create a new job 
<p>
  <img src="1.%20Create%20Job.png" width="360" alt="accessibility text">
</p>

### Step 2 - Create a new job

Choose Job type: Pipeline
<p>
  <img src="2.%20Pipeline.png" width="360" alt="accessibility text">
</p>

### Step 3 - Configure job - 

#### General section
* Add description: **Run Appium Android Tests**
* Select **Do not allow concurrent builds**
* Select **Github project** with url: https://github.com/anandbagmar/getting-started-with-mobile-visualtesting

    <p>
      <img src="3.%20General.png" width="360" alt="accessibility text">
    </p>

#### Pipeline section
* Under **Definition**, select **Pipeline script from SCM**
* Under **SCM**, select **Git**
* Under **Repository URL**, type: **https://github.com/anandbagmar/getting-started-with-mobile-visualtesting** 
* Under **Branch Specifier (blank for 'any')**, type: ***/main** 
* Under **Script Path**, type: ***JenkinsFile**
* Select **Lightweight checkout**
* Click **Save** to create the job

    <p>
      <img src="4.%20Pipeline.png" width="360" alt="accessibility text">
    </p>
