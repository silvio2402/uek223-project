import { Button } from "@mui/material"
import { jwtDecode } from "jwt-decode"
import React from "react"
import { Navigate } from "react-router-dom"
import authorities from "../config/authorities"
import { useActiveUser } from "../contexts/activeUserContext"
import { hasAuthority } from "../services/authorityService"

interface Props {
  element: React.ReactElement
  requiredAuths: authorities[]
}

type JWTType = {
  iss: string
  exp: number
}

function PrivateRoute({
  requiredAuths: requiredAuths,
  element: RouteComponent,
}: Props) {
  const activeUserContext = useActiveUser()
  /**
   * isLoggedIn checks if the token, which is saved inside the localStorage,
   * exists, isn't expired yet and has been issued by the correct issuer.
   * If all of the above is true the user is seen as logged in.
   */
  const isLoggedIn = () => {
    let tokenString = localStorage.getItem("token")
    if (!tokenString) {
      console.error("no token found")
      return false
    }
    tokenString = tokenString.replace("Bearer ", "")
    const token: JWTType = jwtDecode(tokenString) as JWTType
    // Check if token does not exist or doesn't have an expiration claim or is expired.
    if (!token || !token.exp || token.exp < Date.now() / 1000) {
      return false
    }
    return true
  }
  /**
   * If the user is not logged in call activeUserContext.logout, which destroys
   * all locally saved user data and redirects to /login.
   */
  if (!isLoggedIn()) {
    activeUserContext.logout()
    return <Navigate to="/login" replace={true} />
  }
  /**
   * Check if the active user has at least 1 of the needed authorities.
   * If no authorities are needed true is returned.
   */
  const hasNeededAuthorities =
    requiredAuths.length === 0 || requiredAuths.some(hasAuthority)

  /**
   * If the user doesn't possess the needed authorities Redirect the user to
   * /unauthorized
   */
  if (!hasNeededAuthorities) {
    return <Navigate to="/unauthorized" replace={true} />
  }

  // All checks passed
  return (
    //Pagelayout puts the Navigation, Menu etc. around the component
    <div>
      <Button onClick={activeUserContext.logout}>Logout</Button>
      {RouteComponent}
    </div>
  )
}

export default PrivateRoute
