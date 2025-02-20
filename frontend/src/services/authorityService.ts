import authorities from "../config/authorities"
import { User } from "../types/models/User.model"

export function getUserAuthorities(user: User): Set<authorities> {
  return new Set(
    user.roles.flatMap((role) => role.authorities.map((a) => a.name))
  )
}

export function userHasAnyAuthority(
  user: User,
  authorities: authorities[]
): boolean {
  const userAuthorities = getUserAuthorities(user)
  return authorities.some((authority) => userAuthorities.has(authority))
}
