import { jwtDecode } from "jwt-decode"
import Cookies from "universal-cookie"
import api from "../config/api"
import RefreshTokenNotFoundException from "../exceptions/RefreshTokenNotFoundException"
import { AuthRequest } from "../types/models/AuthRequest.model"
import { AuthResponse } from "../types/models/AuthResponse.model"

export const authCookies = new Cookies(null, { path: "/" })

function setAuthCookies(authResponse: AuthResponse): void {
  const { accessToken, refreshToken } = authResponse

  const { exp: accessTokenExpiry } = jwtDecode(accessToken)
  const { exp: refreshTokenExpiry } = jwtDecode(refreshToken)

  authCookies.set("accessToken", accessToken, {
    expires: accessTokenExpiry ? new Date(accessTokenExpiry * 1000) : undefined,
  })
  authCookies.set("refreshToken", refreshToken, {
    expires: refreshTokenExpiry
      ? new Date(refreshTokenExpiry * 1000)
      : undefined,
  })
}

export function getAccessToken() {
  return authCookies.get<string | undefined>("accessToken")
}

export function getRefreshToken() {
  return authCookies.get<string | undefined>("refreshToken")
}

export function getAuthTokens() {
  return {
    accessToken: getAccessToken(),
    refreshToken: getRefreshToken(),
  }
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
  if (refreshToken === undefined) {
    throw new RefreshTokenNotFoundException()
  }
  const { data: authResponse } = await api.post<AuthResponse>("/auth/refresh")

  setAuthCookies(authResponse)
}

export async function logoutAuth(): Promise<void> {
  authCookies.remove("accessToken")
  authCookies.remove("refreshToken")
}
