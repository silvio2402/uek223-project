import { useMutation, useQuery } from "@tanstack/react-query"
import { jwtDecode } from "jwt-decode"
import { User } from "../../types/models/User.model"
import {
  addUser,
  deleteUser,
  getAllUsers,
  getUser,
  updateUser,
} from "../userService"
import { useAccessToken } from "./authHooks"
import { queryClient } from "./queryClient"

export function useActiveUserQuery(
  options?: Partial<Parameters<typeof useQuery<User>>[0]>
) {
  const accessToken = useAccessToken()
  const userId = accessToken ? jwtDecode(accessToken).sub : undefined
  return useUserQuery(userId ?? "", {
    enabled: userId !== undefined,
    ...options,
  })
}

export function useUserQuery(
  userID: string,
  options?: Partial<Parameters<typeof useQuery<User>>[0]>
) {
  return useQuery<User>({
    queryKey: ["users", userID],
    queryFn: () => getUser(userID),
    ...options,
  })
}

export function useUpdateUserMutation() {
  return useMutation({
    mutationFn: updateUser,
    onSuccess: (_, variables) => {
      queryClient.invalidateQueries({ queryKey: ["users", variables.id] })
    },
  })
}

export function useAddUserMutation() {
  return useMutation({
    mutationFn: addUser,
    onSuccess: (user) => {
      queryClient.invalidateQueries({ queryKey: ["users", user.id] })
    },
  })
}

export function useUsersQuery() {
  return useQuery<User[]>({
    queryKey: ["users"],
    queryFn: getAllUsers,
  })
}

export function useDeleteUserMutation() {
  return useMutation({
    mutationFn: deleteUser,
    onSuccess: (_, id) => {
      queryClient.invalidateQueries({ queryKey: ["users", id] })
    },
  })
}
