import authorities from "../config/authorities"
import { Authority } from "../types/models/Authority.model"
import { Role } from "../types/models/Role.model"

const authoritySet = new Set<authorities>()

export async function initAuthoritySet(
  user = JSON.parse(localStorage.getItem("user") || "{}")
) {
  const roles = user && user.roles ? user.roles : []
  roles.forEach((role: Role) => {
    role.authorities.forEach((authority: Authority) => {
      authoritySet.add(authority.name)
    })
  })
}

export async function hasAuthority(authority: authorities) {
  await initAuthoritySet()
  return authoritySet.has(authority)
}

export async function hasAuthorities(authorities: authorities[]) {
  await initAuthoritySet()
  for (const element of authorities) {
    if (!authoritySet.has(element)) {
      return false
    }
  }
  return true
}

export async function hasAnyAuthority(authorities: authorities[]) {
  for (const element of authorities) {
    if (authoritySet.has(element)) {
      return true
    }
  }
  return false
}

export function clearAuthorities(): void {
  authoritySet.clear()
}
