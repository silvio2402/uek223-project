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
  Tooltip,
} from "@mui/material"
import { useNavigate } from "react-router-dom"
import authorities from "../config/authorities"
import { useActiveUser } from "../contexts/activeUser"
import { getUserAuthorities } from "../services/authorityService"
import { useUsersQuery } from "../services/hooks/userHooks"
import { deleteUser } from "../services/userService"
import { User } from "../types/models/User.model"

function UsersPage() {
  const navigate = useNavigate()
  const { data: users, isLoading } = useUsersQuery()
  const activeUser = useActiveUser()
  const userAuthorities = activeUser
    ? getUserAuthorities(activeUser)
    : new Set()

  const handleDelete = async (user: User) => {
    await deleteUser(user.id)
  }

  const handleEdit = (user: User) => {
    navigate("/users/edit/" + user.id)
  }

  if (isLoading) {
    return <Box>Loading...</Box>
  }

  const canModifyUser = (user: User) =>
    userAuthorities.has(authorities.USER_MODIFY_ALL) ||
    (user.id === activeUser?.id &&
      userAuthorities.has(authorities.USER_MODIFY_OWN))

  const canDeleteUser = (user: User) =>
    userAuthorities.has(authorities.USER_DELETE_ALL) ||
    (user.id === activeUser?.id &&
      userAuthorities.has(authorities.USER_DELETE_OWN))

  return (
    <TableContainer component={Paper}>
      <Table>
        <TableHead>
          <TableRow>
            <TableCell>ID</TableCell>
            <TableCell>First Name</TableCell>
            <TableCell>Last Name</TableCell>
            <TableCell>Email</TableCell>
            <TableCell>Actions</TableCell>
          </TableRow>
        </TableHead>
        <TableBody>
          {users?.map((user) => (
            <TableRow key={user.id}>
              <TableCell component="th" scope="row">
                <Tooltip title={`User ID: ${user.id}`} placement="right">
                  <span>...</span>
                </Tooltip>
              </TableCell>
              <TableCell>{user.firstName}</TableCell>
              <TableCell>{user.lastName}</TableCell>
              <TableCell>{user.email}</TableCell>
              <TableCell>
                {canModifyUser(user) && (
                  <IconButton color="primary" onClick={() => handleEdit(user)}>
                    <EditIcon />
                  </IconButton>
                )}
                {canDeleteUser(user) && (
                  <IconButton color="error" onClick={() => handleDelete(user)}>
                    <DeleteIcon />
                  </IconButton>
                )}
              </TableCell>
            </TableRow>
          ))}
        </TableBody>
        <TableFooter>
          <TableRow>
            <TableCell colSpan={5}>
              <Box display="flex" justifyContent="flex-end">
                <IconButton
                  color="primary"
                  onClick={() => navigate("/users/create")}
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

export default UsersPage
