import { Button, Grid, Link, Paper, TextField, Typography } from "@mui/material"

import { Form, Formik } from "formik"
import { useNavigate } from "react-router-dom"
import * as Yup from "yup"
import { loginAuth } from "../services/authService"

const validationSchema = Yup.object().shape({
  email: Yup.string(),
  password: Yup.string(),
})

function Login() {
  const paperStyle = {
    padding: 20,
    height: "70vh",
    width: 280,
    margin: "20px auto",
  }
  const btnstyle = { margin: "8px 0" }
  const navigate = useNavigate()

  const handleSubmit = async (values: { email: string; password: string }) => {
    await loginAuth({
      email: values.email.toLowerCase(),
      password: values.password,
    })
    navigate("/")
  }

  return (
    <Grid>
      <Paper elevation={10} style={paperStyle}>
        <Grid>
          <h2>Sign In</h2>
          <p>Default login:</p>
          <p>email: admin@example.com</p>
          <p>pw: 1234</p>
        </Grid>

        <Formik
          initialValues={{
            email: "",
            password: "",
          }}
          enableReinitialize
          validationSchema={validationSchema}
          onSubmit={handleSubmit}
          validateOnChange
          isInitialValid
        >
          {(props) => (
            <Form onSubmit={props.handleSubmit}>
              <TextField
                label="email"
                id="email"
                placeholder="Enter username"
                fullWidth
                required
                autoFocus
                onChange={props.handleChange}
                onBlur={props.handleBlur}
                value={props.values.email}
              />
              {props.errors.email && (
                <div id="feedback">{props.errors.email}</div>
              )}

              <TextField
                id="password"
                label="password"
                placeholder="Enter password"
                type="password"
                fullWidth
                required
                onChange={props.handleChange}
                onBlur={props.handleBlur}
                value={props.values.password}
              />
              {props.errors.password && (
                <div id="feedback">{props.errors.password}</div>
              )}

              <Button
                type="submit"
                color="primary"
                variant="contained"
                style={btnstyle}
                fullWidth
              >
                Sign in
              </Button>
            </Form>
          )}
        </Formik>
        <Typography>
          <Link href="#">Forgot password ?</Link>
        </Typography>
        <Typography>
          {" "}
          Do you have an account ?<Link href="#">Sign Up</Link>
        </Typography>
      </Paper>
    </Grid>
  )
}

export default Login
