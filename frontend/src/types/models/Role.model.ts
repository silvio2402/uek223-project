import roles from "../../config/roles"
import { Authority } from "./Authority.model"

export type Role = {
  id: string
  name: roles
  authorities: Authority[]
}
