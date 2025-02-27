import { CssBaseline } from "@mui/material"
import { QueryClientProvider } from "@tanstack/react-query"
import { ReactQueryDevtools } from "@tanstack/react-query-devtools"
import { CookiesProvider } from "react-cookie"
import "./App.css"
import { ActiveUserProvider } from "./contexts/activeUser"
import Router from "./router/Router"
import { queryClient } from "./services/hooks/queryClient"

function App() {
  return (
    <>
      <CssBaseline />
      <QueryClientProvider client={queryClient}>
        <ReactQueryDevtools initialIsOpen={false} />
        <CookiesProvider>
          <ActiveUserProvider>
            <Router />
          </ActiveUserProvider>
        </CookiesProvider>
      </QueryClientProvider>
    </>
  )
}

export default App
