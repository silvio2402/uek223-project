import { Typography } from "@mui/material"
import { Box } from "@mui/system"
import { Link } from "react-router-dom"
import { useActiveUser } from "../contexts/activeUser"

function HomePage() {
  const activeUser = useActiveUser()

  return (
    <Box>
      <Typography variant="h2" mt={4}>
        Welcome to OurSpace
      </Typography>
      {activeUser && (
        <>
          <Link to="/mylistentries">My List Entries</Link>
          <br />
          <Link to="/users">Users</Link>
        </>
      )}
    </Box>
  )
}

export default HomePage
