import { Box, Container } from "@mui/material"
import { Outlet } from "react-router-dom"
import TopBar from "./TopBar"

function Layout() {
  return (
    <Box sx={{ display: "flex", flexDirection: "column", minHeight: "100vh" }}>
      <TopBar />
      <Container maxWidth="lg" sx={{ mt: 3, mb: 3, flexGrow: 1 }}>
        <Outlet />
      </Container>
    </Box>
  )
}

export default Layout
