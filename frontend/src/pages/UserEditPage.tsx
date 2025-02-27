import { zodResolver } from "@hookform/resolvers/zod"
import { Box, Button, Stack, TextField } from "@mui/material"
import { useEffect } from "react"
import { useForm } from "react-hook-form"
import { Navigate, useNavigate, useParams } from "react-router-dom"
import { z } from "zod"
import authorities from "../config/authorities"
import { useActiveUser } from "../contexts/activeUser"
import { getUserAuthorities } from "../services/authorityService"
import { useUserQuery } from "../services/hooks/userHooks"
import { deleteUser, updateUser } from "../services/userService"

const userSchema = z.object({
  id: z.string(),
  email: z.string().email(),
  firstName: z.string().min(2),
  lastName: z.string().min(2),
})

type UserFormValues = z.infer<typeof userSchema>

function UserEditPage() {
  const { userId } = useParams()
  const navigate = useNavigate()

  const { data: user, isLoading } = useUserQuery(userId ?? "", {
    enabled: !!userId,
  })
  const activeUser = useActiveUser()
  const userAuthorities = activeUser
    ? getUserAuthorities(activeUser)
    : new Set()

  const { handleSubmit, register, setValue } = useForm<UserFormValues>({
    resolver: zodResolver(userSchema),
    defaultValues: {
      id: "",
      email: "",
      firstName: "",
      lastName: "",
    },
  })

  useEffect(() => {
    if (user) {
      setValue("id", user.id)
      setValue("email", user.email)
      setValue("firstName", user.firstName)
      setValue("lastName", user.lastName)
    }
  }, [user, setValue])

  if (!userId) {
    return <Navigate to="/users" replace />
  }

  const allowed =
    userAuthorities.has(authorities.USER_MODIFY_ALL) ||
    (userId === activeUser?.id &&
      userAuthorities.has(authorities.USER_MODIFY_OWN))

  if (!allowed) {
    return <Navigate to={"/unauthorized"} replace />
  }

  const onSubmit = async (data: UserFormValues) => {
    if (user) {
      try {
        await updateUser({ ...user, ...data })
        navigate("/users")
      } catch (error) {
        console.error("Failed to update user:", error)
      }
    }
  }

  const handleDelete = async () => {
    if (user) {
      try {
        await deleteUser(user.id)
        navigate("/users")
      } catch (error) {
        console.error("Failed to delete user:", error)
      }
    }
  }

  if (isLoading) {
    return <Box>Loading...</Box>
  }

  return (
    <Stack component={"form"} onSubmit={handleSubmit(onSubmit)} spacing={2}>
      <TextField
        label="First Name"
        defaultValue={user?.firstName || ""}
        {...register("firstName")}
      />
      <TextField
        label="Last Name"
        defaultValue={user?.lastName || ""}
        {...register("lastName")}
      />
      <TextField
        label="Email"
        defaultValue={user?.email || ""}
        {...register("email")}
      />
      <Button type="submit" variant="contained" color="primary">
        Save
      </Button>
      <Button variant="contained" color="error" onClick={handleDelete}>
        Delete
      </Button>
    </Stack>
  )
}

export default UserEditPage
