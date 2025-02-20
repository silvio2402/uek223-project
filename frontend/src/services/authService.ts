import { jwtDecode } from "jwt-decode"
import Cookies from "universal-cookie"
import api from "../config/api"
import RefreshTokenNotFoundException from "../exceptions/RefreshTokenNotFoundException"
import { AuthRequest } from "../types/models/AuthRequest.model"
import { AuthResponse } from "../types/models/AuthResponse.model"

const authCookies = new Cookies(null, { path: "/" })

function setAuthCookies(authResponse: AuthResponse): void {
  const { accessToken, refreshToken } = authResponse

  const { exp: accessTokenExpiry } = jwtDecode(accessToken)
  const { exp: refreshTokenExpiry } = jwtDecode(refreshToken)

  authCookies.set("accessToken", accessToken, {
    expires: accessTokenExpiry ? new Date(accessTokenExpiry * 1000) : undefined,
    secure: true,
  })
  authCookies.set("refreshToken", refreshToken, {
    expires: refreshTokenExpiry
      ? new Date(refreshTokenExpiry * 1000)
      : undefined,
    secure: true,
  })
}

export function getAccessToken() {
  return authCookies.get<string>("accessToken")
}

export async function loginAuth(authRequest: AuthRequest): Promise<void> {
  const { data: authResponse } = await api.post<AuthResponse>(
    "/auth/authenticate",
    authRequest
  )

  setAuthCookies(authResponse)
}

export async function refreshAuth(): Promise<void> {
  const refreshToken = authCookies.get<string>("refreshToken")
  if (!refreshToken) {
    throw new RefreshTokenNotFoundException()
  }
  const { data: authResponse } = await api.post<AuthResponse>(
    "/auth/refresh",
    null,
    {
      headers: { Authorization: `Bearer ${refreshToken}` },
    }
  )

  setAuthCookies(authResponse)
}

export async function logoutAuth(): Promise<void> {
  authCookies.remove("accessToken")
  authCookies.remove("refreshToken")
}
