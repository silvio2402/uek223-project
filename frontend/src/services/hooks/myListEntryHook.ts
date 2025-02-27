import { useMutation, useQuery } from "@tanstack/react-query"
import { MyListEntry } from "../../types/models/MyListEntry.model"
import { QueryParams } from "../../types/QueryParams"
import {
  createMyListEntry,
  deleteMyListEntry,
  getMyListEntries,
  getMyListEntry,
  updateMyListEntry,
} from "../myListEntryService"
import { queryClient } from "./queryClient"

export function useMyListEntryQuery(
  myListEntryId: string,
  options?: Partial<Parameters<typeof useQuery<MyListEntry>>[0]>
) {
  return useQuery<MyListEntry>({
    queryKey: ["mylistentries", myListEntryId],
    queryFn: () => getMyListEntry(myListEntryId),
    ...options,
  })
}

export function useMyListEntriesQuery({
  queryParams,
  options,
}: {
  queryParams?: QueryParams
  options?: Partial<Parameters<typeof useQuery<MyListEntry[]>>[0]>
}) {
  return useQuery<MyListEntry[]>({
    queryKey: ["mylistentries"],
    queryFn: () => getMyListEntries(queryParams),
    ...options,
  })
}

export function useUpdateMyListEntryMutation() {
  return useMutation({
    mutationFn: updateMyListEntry,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["mylistentries"] })
    },
  })
}

export function useAddMyListEntryMutation() {
  return useMutation({
    mutationFn: createMyListEntry,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["mylistentries"] })
    },
  })
}

export function useDeleteMyListEntryMutation() {
  return useMutation({
    mutationFn: (id: string) => {
      return deleteMyListEntry(id)
    },
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["mylistentries"] })
    },
  })
}
