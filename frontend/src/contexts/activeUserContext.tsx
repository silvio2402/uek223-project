import {
  createContext,
  useCallback,
  useContext,
  useEffect,
  useState,
} from "react"
import api from "../config/api"
import roles from "../config/roles"
import {
  clearAuthorities,
  initAuthoritySet,
} from "../services/authorityService"
import { getUser } from "../services/userService"
import { User } from "../types/models/User.model"
import { Nullable } from "../types/Nullable"

// Storage keys for the user and token
export const USER_DATA_LOCAL_STORAGE_KEY = "user"
export const TOKEN_LOCAL_STORAGE_KEY = "token"

export type ActiveUserContextType = {
  user: Nullable<User>
  login: (email: string, password: string) => Promise<boolean>
  logout: () => void
  setActiveUser: (user: User) => void
  loadActiveUser: () => void
  checkRole: (roleToCheck: keyof typeof roles) => boolean
}

const noContextProviderFound = () => {
  throw new Error("No provider for the ActiveUserContext found")
}

const defaultContextValue: ActiveUserContextType = {
  user: null,
  login: noContextProviderFound,
  logout: noContextProviderFound,
  setActiveUser: noContextProviderFound,
  loadActiveUser: noContextProviderFound,
  checkRole: noContextProviderFound,
}

const activeUserContext =
  createContext<ActiveUserContextType>(defaultContextValue)

type ActiveUserContextProviderProps = {
  children: React.ReactNode
}

export function useActiveUser() {
  return useContext(activeUserContext)
}

export function ActiveUserContextProvider({
  children,
}: ActiveUserContextProviderProps) {
  // Load the user data from the localStorage
  const loadSavedUserData = (): Nullable<User> => {
    const storeUser = localStorage.getItem(USER_DATA_LOCAL_STORAGE_KEY)
    if (storeUser === null) return null
    return JSON.parse(storeUser)
  }

  // Initialize the user state with the data from the localStorage
  const [user, setUser] = useState<Nullable<User>>(() => loadSavedUserData())

  const setActiveUser = (updatedUser: User) => {
    setUser(updatedUser)
    localStorage.setItem(
      USER_DATA_LOCAL_STORAGE_KEY,
      JSON.stringify(updatedUser)
    )
  }

  const resetAuthorization = () => {
    // Reset the stored data inside the AuthorityService.
    clearAuthorities()
    localStorage.clear()
    setUser(null)
  }

  const logout = () => {
    // If no token is saved inside the local storage clear the localStorage directly
    if (localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY) === null) {
      resetAuthorization()
      return
    }
    // If a token is present send a logout-request and clear the localStorage afterwards
    api.get("/logout").finally(resetAuthorization)

    window.location.href = "/login"
  }

  const login = async (email: string, password: string) => {
    let response
    try {
      response = await api.post("user/login", { email, password })
    } catch {
      return false
    }
    console.log(response.headers.authorization)
    localStorage.setItem(
      TOKEN_LOCAL_STORAGE_KEY,
      response.headers.authorization
    )
    setActiveUser(response.data)
    return true
  }

  const loadActiveUser = useCallback(async () => {
    if (user) {
      const fetchedUser = await getUser(user.id)
      setActiveUser(fetchedUser)
      return fetchedUser
    }
    return null
  }, [user])

  function activeUserHasRole(roleToCheck: keyof typeof roles): boolean {
    return user ? user.roles.some((role) => role.name === roleToCheck) : false
  }

  useEffect(() => {
    const token = localStorage.getItem(TOKEN_LOCAL_STORAGE_KEY)
    if (token !== null) {
      loadActiveUser()
    }
  }, [loadActiveUser])

  useEffect(() => {
    if (user !== null) {
      initAuthoritySet(user)
      localStorage.setItem(USER_DATA_LOCAL_STORAGE_KEY, JSON.stringify(user))
    }
  }, [user])

  return (
    <div>
      <activeUserContext.Provider
        value={{
          user,
          setActiveUser,
          login,
          logout,
          loadActiveUser,
          checkRole: activeUserHasRole,
        }}
      >
        {children}
      </activeUserContext.Provider>
    </div>
  )
}
