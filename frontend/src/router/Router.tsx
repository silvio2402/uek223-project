import { Route, Routes } from "react-router-dom"
import UserTable from "../components/UserTable"
import authorities from "../config/authorities"
import HomePage from "../pages/HomePage"
import LoginPage from "../pages/LoginPage"
import UserPage from "../pages/UserPage"
import ProtectedRoute from "./ProtectedRoute"

function Router() {
  return (
    <Routes>
      <Route path={"/"} element={<HomePage />} />
      <Route path={"/login"} element={<LoginPage />} />

      <Route
        path={"/users"}
        element={<ProtectedRoute element={<UserTable />} />}
      />
      <Route
        path="/useredit"
        element={
          <ProtectedRoute
            anyAuthorityOf={[
              authorities.USER_DEACTIVATE,
              authorities.USER_CREATE,
            ]}
            element={<UserPage />}
          />
        }
      />
      <Route
        path="/useredit/:userId"
        element={
          <ProtectedRoute
            anyAuthorityOf={[authorities.USER_READ]}
            element={<UserPage />}
          />
        }
      />

      <Route path="*" element={<div>Not Found</div>} />
    </Routes>
  )
}

export default Router
