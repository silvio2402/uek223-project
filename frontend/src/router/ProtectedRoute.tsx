import { Navigate } from "react-router-dom"
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

  if (isPending) {
    return
  }

  if (!activeUser) {
    return <Navigate to={"/login"} replace />
  }

  const hasAnyAuthority =
    !anyAuthorityOf || userHasAnyAuthority(activeUser, anyAuthorityOf)

  if (!hasAnyAuthority) {
    return <Navigate to={"/unauthorized"} replace />
  }

  return element
}

export default ProtectedRoute
