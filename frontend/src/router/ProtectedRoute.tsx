import { Navigate, useLocation } from "react-router-dom"
import authorities from "../config/authorities"
import { useActiveUserContext } from "../contexts/activeUser"
import { userHasAnyAuthority } from "../services/authorityService"

function ProtectedRoute({
  element,
  anyAuthorityOf,
}: {
  element: React.ReactElement
  anyAuthorityOf?: authorities[]
}) {
  const { data: activeUser, isPending } = useActiveUserContext()
  const location = useLocation()

  if (isPending) {
    return
  }

  if (!activeUser) {
    const redirectUrl = `${location.pathname}${location.search}${location.hash}`
    const to = {
      pathname: "/login",
      search: `?redirectUrl=${encodeURIComponent(redirectUrl)}`,
    }
    return <Navigate to={to} replace />
  }

  const hasAnyAuthority =
    !anyAuthorityOf || userHasAnyAuthority(activeUser, anyAuthorityOf)

  if (!hasAnyAuthority) {
    return <Navigate to={"/unauthorized"} replace />
  }

  return element
}

export default ProtectedRoute
