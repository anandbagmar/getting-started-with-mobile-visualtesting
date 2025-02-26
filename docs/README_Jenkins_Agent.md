# Jenkins Agent Setup

### Step 1 - Create a new agent

Create a new node - **Dashboard --> Nodes --> New Node** 
<p>
  <img src="1.%20CreateAgent.png" width="360" alt="accessibility text">
</p>

### Step 2 - Configure the agent

Configure the node
<p>
  <img src="2.%20ConfigureAgent.png" width="360" alt="accessibility text">
</p>

### Step 3 - Start the agent

#### Run from agent command line: (Unix)

```shell
  
  cd ../jenkins-docker/agent1

  curl -sO http://localhost:8080/jnlpJars/agent.jar

  java -jar agent.jar -url http://localhost:8080/ -secret ccbda3b9a6430c02a180a1e88ff7d1fee5c8eb3500c59e0a62c8b715204535ef -name agent1 -webSocket -workDir "./jenkins-docker/agent1"
```
 
Run from agent command line: (Windows)
```commandline
    cd ..\jenkins-docker\agent1

    curl.exe -sO http://localhost:8080/jnlpJars/agent.jar

    java -jar agent.jar -url http://localhost:8080/ -secret ccbda3b9a6430c02a180a1e88ff7d1fee5c8eb3500c59e0a62c8b715204535ef -name agent1 -webSocket -workDir "./jenkins-docker/agent1"
```

If you prefer to use TCP instead of WebSockets, remove the -webSocket option. Run java -jar agent.jar -help for more.

Note: PowerShell users must use curl.exe instead of curl because curl is a default PowerShell cmdlet alias for Invoke-WebRequest.

### Step 4 - Stop the agent

Run this command from command line: (Unix)

```shell
  ps -ef | grep agent.jar | awk '{print $2}' | xargs kill 
```
