# Jenkins server and agent setup

---
### Install Docker Desktop on macOS
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

### One time activity: Create a separate docker network for Jenkins
```shell
  docker network create jenkins_network
```

### Get the latest Jenkins server
```shell
    docker pull jenkins/jenkins:lts
```

### Start the Jenkins server
```shell
    docker rm jenkins

    docker run -d \
    --name jenkins \
    --restart unless-stopped \
    --network jenkins_network \
    -p 8080:8080 \
    -p 50000:50000 \
    -v jenkins_home:/var/jenkins_home \
    -e TZ=Asia/Kolkata \
    jenkins/jenkins:lts
```

### Access Jenkins
Open your browser and go to:
```shell
   open http://localhost:8080
```
   
### One time activity: Get the initial Admin password
```shell
    docker exec jenkins cat /var/jenkins_home/secrets/initialAdminPassword
```

### Complete the Setup
- Install recommended plugins.
- Create an admin user.

### Stop Jenkins server
```shell
    docker stop jenkins
```
