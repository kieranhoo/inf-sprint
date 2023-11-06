import React from "react";
import { Routes, Route } from "react-router-dom";
import UploadPage from "../pages/upload";
import { DocDetail } from "../pages/docdetail";
import ListDocs from "../pages/listdocs";

const RouterList = () => {
    return (
        <Routes>
            <Route path="/upload" element={<UploadPage />} />
            <Route path="/docdetail" element={<DocDetail />} />
            <Route path="/" element={<ListDocs />} />
            <Route path="*" element={<div>Error 404</div>} />
        </Routes>
    );
};

export default RouterList;