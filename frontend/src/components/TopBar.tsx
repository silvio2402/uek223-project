import { AppBar, Button, Toolbar, Typography } from "@mui/material"
import { Link } from "react-router-dom"
import { useActiveUser } from "../contexts/activeUser"
import { logoutAuth } from "../services/authService"

function TopBar() {
  const user = useActiveUser()

  return (
    <AppBar
      position="static"
      style={{
        backgroundColor: "white",
        color: "black",
        boxShadow: "none",
        borderBottom: "2px solid #e0e0e0",
      }}
    >
      <Toolbar>
        <Typography variant="h6" component="div" sx={{ flexGrow: 1 }}>
          <Link
            to="/"
            style={{ color: "inherit", textDecoration: "none" }}
            aria-label="Home"
          >
            OurSpace
          </Link>
        </Typography>
        {user === null && (
          <Button
            color="inherit"
            component={Link}
            to="/login"
            aria-label="Login"
          >
            Login
          </Button>
        )}
        {user != null && (
          <Button
            color="inherit"
            onClick={async () => logoutAuth()}
            aria-label="Logout"
          >
            Logout
          </Button>
        )}
      </Toolbar>
    </AppBar>
  )
}

export default TopBar
