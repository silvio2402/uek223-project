import { useCookies } from "react-cookie"

export function useAccessToken() {
  const [cookies] = useCookies<
    "accessToken",
    {
      accessToken?: string
    }
  >(["accessToken"])

  return cookies.accessToken
}

export function useRefreshToken() {
  const [cookies] = useCookies<
    "refreshToken",
    {
      refreshToken?: string
    }
  >(["refreshToken"])

  return cookies.refreshToken
}
