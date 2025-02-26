# Jenkins Server Setup

Here’s a **step-by-step guide** to set up Jenkins in a Docker container on macOS using **Docker Compose**. This includes configuring Jenkins to use the local timezone (IST).

---

### **Step 1: Install Docker Desktop on macOS**
1. **Download Docker Desktop**:
    - Go to the [Docker Desktop for Mac](https://www.docker.com/products/docker-desktop) website.
    - Download and install Docker Desktop.

2. **Start Docker Desktop**:
    - Open Docker Desktop from your Applications folder.
    - Ensure Docker is running (you should see the Docker whale icon in the menu bar).

3. **Verify Docker Installation**:
   Open a terminal and run:
   ```bash
   docker --version
   docker-compose --version
   ```
   This should display the Docker and Docker Compose versions, confirming they are installed and working.

---

### **Step 2: Create a `docker-compose.yml` File**
Create a `docker-compose.yml` file to define the Jenkins service. This file will:
- Use the Jenkins LTS Docker image.
- Persist Jenkins data in a Docker volume.
- Set the timezone to IST.
- Expose Jenkins on port `8080`.

1. Create a directory for your Jenkins setup:
   ```bash
   mkdir ../jenkins-docker
   cd ../jenkins-docker
   ```

2. Create the `docker-compose.yml` file:
   ```yaml
   version: '3.8'
   services:
     jenkins:
       image: jenkins/jenkins:lts
       container_name: jenkins
       ports:
         - "8080:8080" # Jenkins web UI
         - "50000:50000" # Jenkins agent communication
       volumes:
         - jenkins_home:/var/jenkins_home # Persistent Jenkins data
         - /etc/localtime:/etc/localtime:ro # Sync host timezone
         - /etc/timezone:/etc/timezone:ro # Sync host timezone configuration
       environment:
         - TZ=Asia/Kolkata # Set timezone to IST
       restart: unless-stopped # Automatically restart the container unless stopped manually
   volumes:
     jenkins_home: # Declare the Jenkins data volume
   ```

#### **Explanation of the `docker-compose.yml` File**:
- `image: jenkins/jenkins:lts`: Uses the Jenkins LTS Docker image.
- `container_name: jenkins`: Names the container `jenkins`.
- `ports`: Maps ports `8080` (Jenkins web UI) and `50000` (Jenkins agent communication).
- `volumes`:
    - `jenkins_home:/var/jenkins_home`: Persists Jenkins data in a Docker volume.
    - `/etc/localtime:/etc/localtime:ro`: Syncs the host's timezone with the container.
    - `/etc/timezone:/etc/timezone:ro`: Syncs the host's timezone configuration with the container.
- `environment`:
    - `TZ=Asia/Kolkata`: Sets the timezone to Indian Standard Time (IST).

---

### **Step 3: Start Jenkins with Docker Compose**
Run the following command to start Jenkins:
```bash
docker-compose up -d
```

- The `-d` flag runs the container in detached mode (in the background).
- Docker Compose will:
    - Pull the Jenkins LTS image (if not already downloaded).
    - Create the `jenkins_home` volume.
    - Start the Jenkins container.

---

### **Step 4: Access Jenkins**
1. Open your browser and go to:
   ```
   http://localhost:8080
   ```
2. **Unlock Jenkins**:
    - To get the initial admin password, run:
      ```bash
      docker logs jenkins
      ```
    - Look for a line that says:
      ```
      Jenkins initial setup is required. An admin user has been created and a password generated.
      Please use the following password to proceed to installation:
      <password>
      ```
    - Copy the password and paste it into the Jenkins setup page.

3. **Complete the Setup**:
    - Install recommended plugins.
    - Create an admin user.
    - Install [**Applitools Eyes Plugin**](https://plugins.jenkins.io/applitools-eyes/)
        - NOTE: We are using a [pipeline project](../JenkinsFile). So the Applitools Plugin setup is done based on these [instructions](https://plugins.jenkins.io/applitools-eyes/#plugin-content-in-case-of-a-pipeline-project)
---

### **Step 5: Stop Jenkins**
To stop the Jenkins server, run:
```bash
docker-compose down
```

- This will stop and remove the Jenkins container but retain the `jenkins_home` volume for data persistence.

---

### **Step 6: Restart Jenkins**
To restart Jenkins, simply run:
```bash
docker-compose up -d
```

- This will reuse the existing `jenkins_home` volume, so your data and configurations will remain intact.

---
