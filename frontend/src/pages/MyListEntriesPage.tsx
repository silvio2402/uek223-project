import {
  Add as AddIcon,
  Delete as DeleteIcon,
  Edit as EditIcon,
} from "@mui/icons-material"
import {
  Box,
  IconButton,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableFooter,
  TableHead,
  TableRow,
  TableSortLabel,
  Tooltip,
} from "@mui/material"
import { useCallback, useEffect, useMemo, useRef, useState } from "react"
import { useNavigate } from "react-router-dom"
import authorities from "../config/authorities"
import { useActiveUser } from "../contexts/activeUser"
import { getUserAuthorities } from "../services/authorityService"
import { useMyListEntriesInfiniteQuery } from "../services/hooks/myListEntryHook"
import { deleteMyListEntry } from "../services/myListEntryService"
import { MyListEntry } from "../types/models/MyListEntry.model"

const columns: { key: keyof MyListEntry; label: string }[] = [
  { key: "id", label: "ID" },
  { key: "title", label: "Title" },
  { key: "text", label: "Text" },
  { key: "importance", label: "Importance" },
  { key: "creation_date", label: "Creation Date" },
]

function MyListEntriesPage() {
  const navigate = useNavigate()

  const tableContainerRef = useRef<HTMLDivElement>(null)

  const activeUser = useActiveUser()
  const userAuthorities = activeUser
    ? getUserAuthorities(activeUser)
    : new Set()

  const [sortDirection, setSortDirection] = useState<"asc" | "desc">("asc")
  const [sortBy, setSortBy] = useState<keyof MyListEntry>("title")

  const { data, isFetching, fetchNextPage } = useMyListEntriesInfiniteQuery({
    sort: {
      sortBy: [sortBy],
      sortDirection,
    },
    paginate: {
      size: 5,
    },
  })

  const myListEntries = useMemo(
    () => data?.pages.flatMap((page) => page) ?? [],
    [data]
  )

  const totalFetched = myListEntries.length

  const fetchMoreOnBottomReached = useCallback(
    (containerRefElement?: HTMLDivElement | null) => {
      if (containerRefElement) {
        const { scrollHeight, scrollTop, clientHeight } = containerRefElement

        if (
          scrollHeight - scrollTop - clientHeight < 200 &&
          !isFetching &&
          totalFetched
        ) {
          fetchNextPage()
        }
      }
    },

    [fetchNextPage, isFetching, totalFetched]
  )

  useEffect(() => {
    fetchMoreOnBottomReached(tableContainerRef.current)
  }, [fetchMoreOnBottomReached])

  const handleSortClick = (property: keyof MyListEntry) => {
    const isAsc = sortBy === property && sortDirection === "asc"
    setSortDirection(isAsc ? "desc" : "asc")
    setSortBy(property)
  }

  const handleDeleteEntry = async (id: string) => {
    await deleteMyListEntry(id)
  }

  const canModifyEntry = (entry: MyListEntry) =>
    userAuthorities.has(authorities.MYLISTENTRY_MODIFY_ALL) ||
    (entry.user_id === activeUser?.id &&
      userAuthorities.has(authorities.MYLISTENTRY_MODIFY_OWN))

  const canDeleteEntry = (entry: MyListEntry) =>
    userAuthorities.has(authorities.MYLISTENTRY_DELETE_ALL) ||
    (entry.user_id === activeUser?.id &&
      userAuthorities.has(authorities.MYLISTENTRY_DELETE_OWN))

  return (
    <TableContainer
      component={Paper}
      onScroll={(event) => {
        const target = event.target as HTMLDivElement
        fetchMoreOnBottomReached(target)
      }}
      ref={tableContainerRef}
      sx={{
        maxHeight: "60vh",
      }}
    >
      <Table stickyHeader>
        <TableHead>
          <TableRow>
            {columns.map((column) => (
              <TableCell key={column.key}>
                <TableSortLabel
                  active={sortBy === column.key}
                  direction={sortBy === column.key ? sortDirection : "asc"}
                  onClick={() => handleSortClick(column.key)}
                >
                  {column.label}
                </TableSortLabel>
              </TableCell>
            ))}
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {myListEntries?.map((myListEntry: MyListEntry) => (
            <TableRow key={myListEntry.id}>
              {columns.map((column) =>
                column.key === "id" ? (
                  <TableCell component="th" scope="row" key={column.key}>
                    <Tooltip
                      title={`MyListEntry ID: ${myListEntry.id}`}
                      placement="right"
                    >
                      <span>...</span>
                    </Tooltip>
                  </TableCell>
                ) : (
                  <TableCell key={column.key}>
                    {myListEntry[column.key]}
                  </TableCell>
                )
              )}
              <TableCell>
                {canModifyEntry(myListEntry) && (
                  <IconButton
                    color="primary"
                    onClick={() =>
                      navigate(`/mylistentries/edit/${myListEntry.id}`)
                    }
                  >
                    <EditIcon />
                  </IconButton>
                )}
                {canDeleteEntry(myListEntry) && (
                  <IconButton
                    color="error"
                    onClick={() => handleDeleteEntry(myListEntry.id)}
                  >
                    <DeleteIcon />
                  </IconButton>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
        <TableFooter
          style={{
            position: "sticky",
            insetBlockEnd: 0,
            backgroundColor: "white",
          }}
        >
          <TableRow>
            <TableCell colSpan={columns.length + 1}>
              <Box>
                <IconButton
                  color="primary"
                  aria-label="Add MyListEntry"
                  onClick={() => navigate("/mylistentries/create")}
                >
                  <AddIcon />
                </IconButton>
              </Box>
            </TableCell>
          </TableRow>
        </TableFooter>
      </Table>
    </TableContainer>
  )
}

export default MyListEntriesPage
