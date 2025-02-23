import { QueryClientProvider } from "@tanstack/react-query"
import { CookiesProvider } from "react-cookie"
import "./App.css"
import { ActiveUserProvider } from "./contexts/activeUser"
import Router from "./router/Router"
import { queryClient } from "./services/hooks/queryClient"

function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <CookiesProvider>
        <ActiveUserProvider>
          <Router />
        </ActiveUserProvider>
      </CookiesProvider>
    </QueryClientProvider>
  )
}

export default App
