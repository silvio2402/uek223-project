import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Tooltip,
} from "@mui/material"
import { useMyListEntriesQuery } from "../services/hooks/myListEntryHook"
import { MyListEntry } from "../types/models/MyListEntry.model"

function MyListEntriesPage() {
  const { data: myListEntries, isLoading } = useMyListEntriesQuery({})

  if (isLoading) {
    return <Box>Loading...</Box>
  }

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>Title</TableCell>
            <TableCell>Text</TableCell>
            <TableCell>Importance</TableCell>
            <TableCell>Creation Date</TableCell>
            {/* TODO: Add more columns here */}
          </TableRow>
        </TableHead>
        <TableBody>
          {myListEntries?.map((myListEntry: MyListEntry) => (
            <TableRow key={myListEntry.id}>
              <TableCell component="th" scope="row">
                <Tooltip
                  title={`MyListEntry ID: ${myListEntry.id}`}
                  placement="right"
                >
                  <span>...</span>
                </Tooltip>
              </TableCell>
              <TableCell>{myListEntry.title}</TableCell>
              <TableCell>{myListEntry.text}</TableCell>
              <TableCell>{myListEntry.importance}</TableCell>
              <TableCell>{myListEntry.creation_date}</TableCell>
              {/* TODO: Add more data here */}
            </TableRow>
          ))}
        </TableBody>
        {/* TODO: Add TableFooter for pagination */}
      </Table>
      {/* TODO: Add sorting and filtering */}
    </TableContainer>
  )
}

export default MyListEntriesPage
