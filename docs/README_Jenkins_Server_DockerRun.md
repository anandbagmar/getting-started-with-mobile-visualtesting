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

### **Step 2: Start Jenkins server using docker run command**

```shell
    docker stop jenkins
    
    docker rm jenkins

    docker run -d \
    --name jenkins \
    -p 8080:8080 \
    -p 50000:50000 \
    -v jenkins_home:/var/jenkins_home \
    -v /etc/localtime:/etc/localtime:ro \
    -v /etc/timezone:/etc/timezone:ro \
    -e TZ=Asia/Kolkata \
    jenkins/jenkins:lts    
```

---

### **Step 3: Access Jenkins**
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
---

### **Step 4: Stop Jenkins**
To stop the Jenkins server, run:
```shell
  docker stop jenkins
```

- This will stop and remove the Jenkins container but retain the `jenkins_home` volume for data persistence.

---

