import "./App.css"
import { ActiveUserContextProvider } from "./contexts/activeUserContext"
import Router from "./router/Router"

function App() {
  return (
    <ActiveUserContextProvider>
      <Router />
    </ActiveUserContextProvider>
  )
}

export default App
