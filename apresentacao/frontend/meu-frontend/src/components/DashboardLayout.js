// frontend/meu-frontend/src/components/DashboardLayout.js
import React from "react";
import Navbar from "./Navbar";
import "../css/base.css";

const DashboardLayout = ({ children }) => {
  return (
    <div>
      <Navbar />
      <div className="main-content">
        {children}
      </div>
    </div>
  );
};

export default DashboardLayout;
