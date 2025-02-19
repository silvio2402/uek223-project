import roles from "../../config/roles"
import { Authority } from "./Authority.model"

/**
 * Role type
 */
export type Role = {
  id: string
  name: roles
  authorities: Authority[]
}
