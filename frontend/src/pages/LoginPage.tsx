import { zodResolver } from "@hookform/resolvers/zod"
import {
  Alert,
  Button,
  Container,
  Stack,
  TextField,
  Typography,
} from "@mui/material"
import { AxiosError } from "axios"
import { useState } from "react"
import { useForm } from "react-hook-form"
import { useLocation, useNavigate } from "react-router-dom"
import * as z from "zod"
import { useActiveUser } from "../contexts/activeUser"
import { loginAuth, logoutAuth } from "../services/authService"
import { addUser } from "../services/userService"

const LoginSchema = z.object({
  email: z.string(),
  password: z.string(),
})

const RegisterSchema = z.object({
  email: z.string().email("Invalid email address"),
  password: z.string().min(8, "Password must be at least 8 characters"),
  firstName: z.string().min(2, "First name must be at least 2 characters"),
  lastName: z.string().min(2, "Last name must be at least 2 characters"),
})

type LoginValues = z.infer<typeof LoginSchema>
type RegisterValues = z.infer<typeof RegisterSchema>
type FormValues = {
  email: string
  password: string
  firstName?: string
  lastName?: string
}
interface ApiErrorResponse {
  message?: string
}

function LoginPage() {
  const navigate = useNavigate()
  const location = useLocation()
  const [isRegistering, setIsRegistering] = useState(false)
  const [formError, setFormError] = useState<string>("")
  const user = useActiveUser()

  const {
    register,
    handleSubmit,
    formState: { errors },
  } = useForm<FormValues>({
    resolver: zodResolver(isRegistering ? RegisterSchema : LoginSchema),
    defaultValues: {
      email: "",
      password: "",
      firstName: "",
      lastName: "",
    },
    mode: "onBlur",
  })

  const handleContinue = () => {
    const redirectUrl = new URLSearchParams(location.search).get("redirectUrl")
    const redirectTo = redirectUrl ? decodeURIComponent(redirectUrl) : "/"
    navigate(redirectTo.startsWith("/") ? redirectTo : "/")
  }

  const onSubmit = async (data: FormValues) => {
    setFormError("")
    try {
      if (isRegistering) {
        const registerData = data as RegisterValues
        try {
          const { email, password, firstName, lastName } = registerData
          await addUser({
            email: email.toLowerCase(),
            password,
            firstName,
            lastName,
          })
          setIsRegistering(false)
        } catch {
          setFormError("Registration failed")
        }
      } else {
        const loginData = data as LoginValues
        await loginAuth({
          email: loginData.email.toLowerCase(),
          password: loginData.password,
        })
        handleContinue()
      }
    } catch (error) {
      let errorMessage = "Authentication failed"

      if (error instanceof AxiosError) {
        const apiError = error as AxiosError<ApiErrorResponse>
        errorMessage =
          apiError.response?.data?.message || error.message || errorMessage
      } else if (error instanceof Error) {
        errorMessage = error.message || errorMessage
      }

      setFormError(errorMessage)
    }
  }

  if (user === undefined) {
    return <Typography>Loading...</Typography>
  }

  if (user != null) {
    return (
      <Container maxWidth="sm">
        <Stack spacing={2}>
          <Typography variant="h6" align="center">
            You are already logged in.
          </Typography>
          <Button onClick={logoutAuth} color="error" fullWidth>
            Logout
          </Button>
          <Button onClick={handleContinue} variant="contained" fullWidth>
            Continue
          </Button>
        </Stack>
      </Container>
    )
  }

  return (
    <Container maxWidth="sm">
      <Stack spacing={2} mt={4}>
        <Typography variant="h4" align="center">
          {isRegistering ? "Register" : "Login"}
        </Typography>

        {formError && <Alert severity="error">{formError}</Alert>}

        <form onSubmit={handleSubmit(onSubmit)}>
          <Stack spacing={2}>
            <TextField
              label="Email"
              type="email"
              fullWidth
              {...register("email")}
              error={!!errors.email}
              helperText={errors.email?.message}
            />
            {isRegistering && (
              <>
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
              </>
            )}
            <TextField
              label="Password"
              type="password"
              fullWidth
              {...register("password")}
              error={!!errors.password}
              helperText={errors.password?.message}
            />
            <Button type="submit" variant="contained" color="primary" fullWidth>
              {isRegistering ? "Register" : "Login"}
            </Button>
          </Stack>
        </form>

        <Stack spacing={2} textAlign="center">
          <Typography>
            {isRegistering
              ? "Already have an account?"
              : "Don't have an account?"}
            <Button
              color="primary"
              onClick={() => setIsRegistering(!isRegistering)}
            >
              {isRegistering ? "Login" : "Register"}
            </Button>
          </Typography>
        </Stack>
      </Stack>
    </Container>
  )
}

export default LoginPage
