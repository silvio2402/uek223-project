import api from "../config/api"

export async function findAll() {
  return api.get("/roles")
}
