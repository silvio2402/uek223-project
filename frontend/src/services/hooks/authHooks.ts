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
