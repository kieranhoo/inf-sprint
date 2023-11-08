import { Outlet, useNavigate } from "react-router-dom";
import { toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';
import { useDispatch, useSelector } from "react-redux";
import { selectAccessToken, selectIsAuthenticated } from "../redux/selectors";
import { logoutRequest } from "../redux/reducers/authSlice";
import { useEffect } from "react";

const UserLayout = () => {
    const access_token = useSelector(selectAccessToken)
    const isAuthenticated = useSelector(selectIsAuthenticated)
    const dispatch = useDispatch()
    const navigate = useNavigate()
    const logout = () => {
        dispatch(logoutRequest(access_token))
    }
    useEffect(() => {
        if (isAuthenticated === false) {
            toast.success('Đăng xuất thành công')
            navigate('/')
        }
    }, [isAuthenticated]) // eslint-disable-line react-hooks/exhaustive-deps
    return (
        <div className="userlayout">
            <button onClick={() => logout()}>
                Logout
            </button>
            <div className="">
                <Outlet></Outlet>
            </div>
        </div>
    );
}

export default UserLayout;