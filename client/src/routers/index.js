import React from "react";
import { Route, Routes } from "react-router-dom"; // Đảm bảo bạn import đúng thứ tự: `Route` trước, sau đó `Routes`
import UploadPage from "../pages/upload";
import { DocDetail } from "../pages/docdetail";
import ListDocs from "../pages/listdocs";
import Login from "../pages/login";
import UserLayout from "../layout/userlayout";
import GeneralLayout from "../layout/generallayout";

const RouterList = () => {
    return (
        <Routes>
            <Route element={<GeneralLayout />}>
                <Route path="/" element={<Login />} />
            </Route>
            <Route element={<UserLayout />}>
                <Route path="/dashboard" index element={<ListDocs />} />
                <Route path="/upload" element={<UploadPage />} />
                <Route path="/document/:id" element={<DocDetail />} />
                <Route path="*" element={<div>Error 404</div>} />
            </Route>
        </Routes>
    );
};

export default RouterList;
