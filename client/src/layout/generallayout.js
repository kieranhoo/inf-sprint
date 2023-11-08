import { Outlet } from "react-router-dom";

const GeneralLayout = () => {
    return (
        <div className="">
            <div className="">
                <Outlet></Outlet>
            </div>
        </div>
    );
}

export default GeneralLayout;