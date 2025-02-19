import { Route, Routes } from "react-router-dom"
import authorities from "../config/authorities"
import HomePage from "../pages/HomePage"
import LoginPage from "../pages/LoginPage/LoginPage"
import UserPage from "../pages/UserPage/UserPage"
import UserTable from "../pages/UserPage/UserTable"
import PrivateRoute from "./PrivateRoute"

/**
 * Router component renders a route switch with all available pages
 */
function Router() {
  //const { checkRole } = useContext(ActiveUserContext);

  /** navigate to different "home"-locations depending on Role the user have */

  return (
    <Routes>
      <Route path={"/"} element={<HomePage />} />
      <Route path={"/login"} element={<LoginPage />} />

      <Route
        path={"/users"}
        element={<PrivateRoute requiredAuths={[]} element={<UserTable />} />}
      />
      <Route
        path="/useredit"
        element={
          <PrivateRoute
            requiredAuths={[
              authorities.USER_DEACTIVATE,
              authorities.USER_CREATE,
            ]}
            element={<UserPage />}
          ></PrivateRoute>
        }
      />
      <Route
        path="/useredit/:userId"
        element={
          <PrivateRoute
            requiredAuths={[authorities.USER_READ]}
            element={<UserPage />}
          ></PrivateRoute>
        }
      />

      <Route path="*" element={<div>Not Found</div>} />
    </Routes>
  )
}

export default Router
