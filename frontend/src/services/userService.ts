import api from "../config/api"
import { User } from "../types/models/User.model"
import { UserRegister } from "../types/models/UserRegister.model"
import { queryClient } from "./hooks/queryClient"

export async function getUser(userID: string) {
  const { data } = await api.get<User>(`/user/${userID}`)
  return data
}

export async function updateUser(user: User) {
  await api.put(`/user/${user.id}`, user)
  queryClient.invalidateQueries({ queryKey: ["users"] })
}

export async function addUser(user: UserRegister) {
  const { data } = await api.post<User>("/user/register", user)
  queryClient.invalidateQueries({ queryKey: ["users"] })
  return data
}

export async function getAllUsers() {
  const { data } = await api.get<User[]>(`/user`)
  return data
}

export async function deleteUser(id: string) {
  await api.delete(`/user/${id}`)
  queryClient.invalidateQueries({ queryKey: ["users"] })
}
