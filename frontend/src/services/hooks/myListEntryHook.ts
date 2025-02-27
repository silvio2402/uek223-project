import { useInfiniteQuery, useMutation, useQuery } from "@tanstack/react-query"
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
    queryKey: ["mylistentries", queryParams],
    queryFn: () => getMyListEntries(queryParams),
    ...options,
  })
}

export function useMyListEntriesInfiniteQuery(
  queryParams?: QueryParams,
  options?: Partial<Parameters<typeof useInfiniteQuery<MyListEntry[]>>[0]>
) {
  return useInfiniteQuery<MyListEntry[]>({
    queryKey: ["mylistentries", queryParams],
    initialPageParam: 0,
    queryFn: ({ pageParam }) =>
      getMyListEntries({
        ...queryParams,
        paginate: { page: pageParam as number, ...queryParams?.paginate },
      }),
    getNextPageParam: (_, allPages) => {
      const lastPage = allPages[allPages.length - 1]
      return lastPage.length === 0 ? undefined : allPages.length
    },
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
