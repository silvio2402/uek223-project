import { Route, Routes } from "react-router-dom"
import Layout from "../components/Layout"
import authorities from "../config/authorities"
import HomePage from "../pages/HomePage"
import LoginPage from "../pages/LoginPage"
import UserCreatePage from "../pages/UserCreatePage"
import UserEditPage from "../pages/UserEditPage"
import UsersPage from "../pages/UsersPage"
import ProtectedRoute from "./ProtectedRoute"

function Router() {
  return (
    <Routes>
      <Route path={"/"} element={<Layout />}>
        <Route index element={<HomePage />} />

        <Route path={"/login"} element={<LoginPage />} />

        <Route
          path={"/users"}
          element={
            <ProtectedRoute
              anyAuthorityOf={[authorities.USER_READ_ALL]}
              element={<UsersPage />}
            />
          }
        />

        <Route
          path="/users/edit/:userId"
          element={
            <ProtectedRoute
              anyAuthorityOf={[
                authorities.USER_MODIFY_ALL,
                authorities.USER_MODIFY_OWN,
              ]}
              element={<UserEditPage />}
            />
          }
        />

        <Route
          path="/users/create"
          element={
            <ProtectedRoute
              anyAuthorityOf={[authorities.USER_MODIFY_ALL]}
              element={<UserCreatePage />}
            />
          }
        />

        <Route path="*" element={<div>Not Found</div>} />
      </Route>
    </Routes>
  )
}

export default Router
