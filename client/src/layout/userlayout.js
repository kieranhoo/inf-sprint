import { Outlet } from "react-router-dom";
import 'react-toastify/dist/ReactToastify.css';
import Header from "../components/header";

const UserLayout = () => {
    return (
        <div className="userlayout">
            <Header />
            <div className="wrapper">
                <Outlet></Outlet>
            </div>
        </div>
    );
}

export default UserLayout;