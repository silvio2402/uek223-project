import api from "../config/api"

const roleSet = new Set()

export async function findAll() {
  return api.get("/roles")
}
