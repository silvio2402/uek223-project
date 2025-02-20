import { createContext, PropsWithChildren, useContext } from "react"
import { useActiveUserQuery } from "../services/hooks/userHooks"
import { User } from "../types/models/User.model"

const ActiveUserContext = createContext<User | null>(null)

export function ActiveUserProvider({ children }: PropsWithChildren<unknown>) {
  const { data: user } = useActiveUserQuery()

  return <ActiveUserContext value={user ?? null}>{children}</ActiveUserContext>
}

export function useActiveUser() {
  return useContext(ActiveUserContext)
}
