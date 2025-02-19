import { useEffect, useState } from "react"
import { useNavigate, useParams } from "react-router-dom"
import { addUser, getUser, updateUser } from "../../../services/userService"
import { User } from "../../../types/models/User.model"
import UserForm from "../../UserForm"

const UserPage = () => {
  const navigate = useNavigate()
  const { userId } = useParams()
  const [user, setUser] = useState<User>({
    id: "",
    firstName: "",
    lastName: "",
    email: "",
    roles: [],
  })

  useEffect(() => {
    return () => {
      if (userId) {
        getUser(userId).then((res) => {
          return setUser(res)
        })
      }
    }
  }, [userId])

  const submitActionHandler = (values: User) => {
    if (userId !== undefined) {
      updateUser(values).then(() => {
        navigate("../users")
      })
    } else {
      addUser(values).then(() => {
        navigate("/users")
      })
    }
  }

  return <UserForm user={user} submitActionHandler={submitActionHandler} />
}
export default UserPage
