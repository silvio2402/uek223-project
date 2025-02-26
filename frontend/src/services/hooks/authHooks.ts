import { useEffect, useState } from "react"
import { useLocation } from "react-router-dom"
import { authCookies, getAuthTokens } from "../authService"

export function useAuthTokens() {
  const location = useLocation()

  const [authTokens, setAuthTokens] = useState(() => getAuthTokens())

  useEffect(() => {
    setAuthTokens(() => getAuthTokens())

    const changeHandler = () => setAuthTokens(() => getAuthTokens())

    authCookies.addChangeListener(changeHandler)
    return () => authCookies.removeChangeListener(changeHandler)
  }, [location.key])

  return authTokens
}
