import { zodResolver } from "@hookform/resolvers/zod"
import {
  Alert,
  Button,
  Container,
  Stack,
  TextField,
  Typography,
} from "@mui/material"
import { useState } from "react"
import { useForm } from "react-hook-form"
import { useNavigate } from "react-router-dom"
import { z } from "zod"
import { addUser } from "../services/userService"
import { UserRegister } from "../types/models/UserRegister.model"

const userSchema = z.object({
  firstName: z.string().min(2, "First name must be at least 2 characters"),
  lastName: z.string().min(2, "Last name must be at least 2 characters"),
  email: z.string().email("Invalid email address"),
  password: z.string().min(8, "Password must be at least 8 characters"),
})

type UserFormValues = z.infer<typeof userSchema>

function UserCreatePage() {
  const navigate = useNavigate()
  const [formError, setFormError] = useState<string>("")

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<UserFormValues>({
    resolver: zodResolver(userSchema),
    defaultValues: {
      firstName: "",
      lastName: "",
      email: "",
      password: "",
    },
  })

  const onSubmit = async (data: UserFormValues) => {
    setFormError("")
    try {
      const { firstName, lastName, email, password } = data
      await addUser({
        firstName,
        lastName,
        email: email.toLowerCase(),
        password,
      } as UserRegister)
      navigate("/users")
    } catch (error: unknown) {
      if (error instanceof Error) {
        setFormError(error.message || "Failed to create user")
      } else {
        setFormError("Failed to create user")
      }
    }
  }

  return (
    <Container maxWidth="sm">
      <Stack spacing={2} mt={4}>
        <Typography variant="h4" align="center">
          Create User
        </Typography>

        {formError && <Alert severity="error">{formError}</Alert>}

        <form onSubmit={handleSubmit(onSubmit)}>
          <Stack spacing={2}>
            <TextField
              label="First Name"
              type="text"
              fullWidth
              {...register("firstName")}
              error={!!errors.firstName}
              helperText={errors.firstName?.message}
            />
            <TextField
              label="Last Name"
              type="text"
              fullWidth
              {...register("lastName")}
              error={!!errors.lastName}
              helperText={errors.lastName?.message}
            />
            <TextField
              label="Email"
              type="email"
              fullWidth
              {...register("email")}
              error={!!errors.email}
              helperText={errors.email?.message}
            />
            <TextField
              label="Password"
              type="password"
              fullWidth
              {...register("password")}
              error={!!errors.password}
              helperText={errors.password?.message}
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
              Create
            </Button>
          </Stack>
        </form>
      </Stack>
    </Container>
  )
}

export default UserCreatePage
