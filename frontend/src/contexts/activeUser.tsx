import { useQuery, UseQueryResult } from "@tanstack/react-query"
import { jwtDecode } from "jwt-decode"
import { createContext, PropsWithChildren, useContext, useMemo } from "react"
import { useAuthTokens } from "../services/hooks/authHooks"
import { getUser } from "../services/userService"
import { User } from "../types/models/User.model"

const ActiveUserContext = createContext<
  UseQueryResult<User | null, Error> | undefined
>(undefined)

export function ActiveUserProvider({ children }: PropsWithChildren<unknown>) {
  const authTokens = useAuthTokens()
  const userId = useMemo(() => {
    const { accessToken, refreshToken } = authTokens
    let userId = accessToken ? jwtDecode(accessToken).sub : undefined
    userId = userId ?? (refreshToken ? jwtDecode(refreshToken).sub : undefined)
    return userId
  }, [authTokens])

  const queryResult = useQuery<User | null>({
    queryKey: ["users", userId],
    queryFn: () => {
      return userId ? getUser(userId) : null
    },
  })

  return <ActiveUserContext value={queryResult}>{children}</ActiveUserContext>
}

export function useActiveUserContext() {
  const context = useContext(ActiveUserContext)
  if (context === undefined) {
    throw new Error(
      "active user hooks must be used within a ActiveUserProvider"
    )
  }
  return context
}

export function useActiveUser() {
  return useActiveUserContext().data
}
