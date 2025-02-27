import api from "../config/api"
import { MyListEntry } from "../types/models/MyListEntry.model"
import { MyListEntryCreate } from "../types/models/MyListEntryCreate.model"
import { buildQueryParams, QueryParams } from "../types/QueryParams"
import { queryClient } from "./hooks/queryClient"

export async function getMyListEntries(queryParams?: QueryParams) {
  const params = buildQueryParams(queryParams || {})
  const response = await api.get<MyListEntry[]>(`/mylistentry`, {
    params,
  })
  return response.data
}

export async function getMyListEntry(id: string) {
  const response = await api.get<MyListEntry>(`/mylistentry/${id}`)
  return response.data
}

export async function createMyListEntry(data: MyListEntryCreate) {
  const response = await api.post<MyListEntry>(`/mylistentry`, data)
  await queryClient.invalidateQueries({ queryKey: ["mylistentries"] })
  return response.data
}

export async function updateMyListEntry(data: MyListEntry) {
  const { id } = data
  const response = await api.put<MyListEntry>(`/mylistentry/${id}`, data)
  await queryClient.invalidateQueries({ queryKey: ["mylistentries"] })
  return response.data
}

export async function deleteMyListEntry(id: string) {
  await api.delete(`/mylistentry/${id}`)
  await queryClient.invalidateQueries({ queryKey: ["mylistentries"] })
}
