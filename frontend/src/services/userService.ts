import api from "../config/api"
import { User } from "../types/models/User.model"

export async function getUser(userID: string): Promise<User> {
  const { data } = await api.get<User>(`/user/${userID}`)
  return data
}

export async function updateUser(user: User): Promise<void> {
  await api.put(`/user/${user.id}`, user)
}

export async function addUser(user: User): Promise<User> {
  const { data } = await api.post("/user/registerUser", user)
  return data
}

export async function getAllUsers(): Promise<User[]> {
  const { data } = await api.get<User[]>(`/user`)
  return data
}

export async function deleteUser(id: string): Promise<void> {
  await api.delete(`/user/${id}`)
}
