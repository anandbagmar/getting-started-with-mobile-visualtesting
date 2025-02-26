# Jenkins Agent Setup

### Step 1 - Create a new agent

Create a new node - **Dashboard --> Nodes --> New Node** 
<p>
  <img src="CreateAgent/1.%20CreateAgent.png" width="360" alt="accessibility text">
</p>

### Step 2 - Configure the agent

Configure the node
<p>
  <img src="CreateAgent/2.%20ConfigureAgent.png" width="360" alt="accessibility text">
</p>

### Step 3 - Start the agent

#### Fetch the `agent.jar` (Unix)
```shell
  curl -sO http://localhost:8080/jnlpJars/agent.jar
```

#### Fetch the `agent.jar` (Windows)
```commandline
  curl.exe -sO http://localhost:8080/jnlpJars/agent.jar
```

#### Start the agent
Run the command from your agent page: ex: http://localhost:8080/computer/agent1/
<p>
<img src="CreateAgent/3.%20AgentConfiguration.png" width="360" alt="accessibility text">
</p>

Example: 
 ```shell
    cd ~/projects/jenkins/
    java -jar agent.jar -url http://localhost:8080/ -secret 1beec4b865d6afc6fe2068a385e7b8b4ae225a5efb8dd48efb9252a32f94305b -name agent1 -webSocket -workDir "/Users/anand.bagmar/projects/jenkins/agent1"
```

### Step 4 - Stop the agent

Run this command from command line: (Unix)

```shell
  ps -ef | grep agent.jar | awk '{print $2}' | xargs kill 
```
