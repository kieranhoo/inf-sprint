import React from "react";
import { Routes, Route } from "react-router-dom";
import UploadPage from "../pages/upload";
import { DocDetail } from "../pages/docdetail";

const RouterList = () => {
    return (
        <Routes>
            <Route path="/" element={<UploadPage />} />
            <Route path="/docdetail" element={<DocDetail />}/>
            <Route path="*" element={<div>Error 404</div>} />
        </Routes>
    );
};

export default RouterList;