import Button from "@mui/material/Button"
import Card from "@mui/material/Card"
import CardActions from "@mui/material/CardActions"
import CardContent from "@mui/material/CardContent"
import { useEffect, useState } from "react"
import { useNavigate } from "react-router-dom"
import { deleteUser, getAllUsers } from "../../../services/userService"
import { User } from "../../../types/models/User.model"

function UserTable() {
  const navigate = useNavigate()
  const [users, setUsers] = useState<User[]>([])

  useEffect(() => {
    getAllUsers().then((data) => {
      setUsers(data)
    })
  }, [])

  const handleAdd = () => {
    navigate("../useredit/")
  }

  const handleEdit = (id: string) => {
    navigate("../useredit/" + id)
  }

  const handleDelete = (id: string) => {
    deleteUser(id)
  }

  return (
    <>
      {users.map((user) => (
        <div key={user.id}>
          <Card sx={{ minWidth: 275 }}>
            <CardContent>
              {user.firstName} {user.lastName} {user.email}
              <CardActions>
                <Button
                  size="small"
                  color="primary"
                  variant="contained"
                  onClick={() => handleEdit(user.id)}
                >
                  Edit
                </Button>
                <Button
                  size="small"
                  color="error"
                  variant="contained"
                  onClick={() => handleDelete(user.id)}
                >
                  Delete
                </Button>
              </CardActions>
            </CardContent>
          </Card>
        </div>
      ))}
      <Button
        size="small"
        color="success"
        variant="contained"
        onClick={handleAdd}
      >
        Add
      </Button>
    </>
  )
}

export default UserTable
