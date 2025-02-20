import { Navigate } from "react-router-dom"
import authorities from "../config/authorities"
import { userHasAnyAuthority } from "../services/authorityService"
import { useActiveUserQuery } from "../services/hooks/userHooks"

function ProtectedRoute({
  element,
  anyAuthorityOf,
}: {
  element: React.ReactElement
  anyAuthorityOf?: authorities[]
}) {
  const { data: activeUser, isFetched } = useActiveUserQuery()

  if (!isFetched) {
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
