#

## Prerequisites

Make sure you have the following installed - [Visual Studio Code](https://code.visualstudio.com/) - [Devcontainers extension](https://marketplace.visualstudio.com/items?itemName=ms-vscode-remote.remote-containers) - [Docker](https://www.docker.com/) - [Docker Compose](https://docs.docker.com/compose/install/)

## General Setup

1. Open Project in Visual Studio Code

2. click on the bottom left corner of the window where it says "Open a Remote Window" and select "Reopen in Container"

3. Wait for the container to build

## Running Backend

1. Open the project in Visual Studio Code

2. Open a terminal in Visual Studio Code

3. Run the following commands:

```bash
cd backend
./gradlew bootRun
```

## Setup Frontend

1. Open the project in Visual Studio Code

2. Open a terminal in Visual Studio Code

3. Set the `VITE_API_BASE_URL` env variable in the .env file to the backend api address

4. Run the following commands:

```bash
cd frontend
bun install
```

## Running Frontend

1. Open the project in Visual Studio Code

2. Open a terminal in Visual Studio Code

3. Run the following commands:

```bash
cd frontend
bun dev
```

## Running Tests

### Backend Integration Tests

1. Open the project in Visual Studio Code

2. Open a terminal in Visual Studio Code

3. Run the following commands:

```bash
cd backend
./gradlew test
```

### Frontend E2E Tests

1. Open the project in Visual Studio Code

2. Open 3 terminals in Visual Studio Code

1st Terminal:

```bash
cd frontend
bun dev
```

2nd Terminal:

```bash
cd backend
./gradlew bootRun
```

3rd Terminal:

```bash
cd frontend
bun cypress install # First time only
bun cypress run
```
