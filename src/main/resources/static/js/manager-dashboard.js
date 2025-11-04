document.addEventListener("DOMContentLoaded", () => {
    const employee = JSON.parse(localStorage.getItem("employee"));
    const role = localStorage.getItem("role");

    // 🔒 Block access if not logged in
    if (!employee || !role) {
        alert("Access denied! Please login first.");
        window.location.href = "login.html";
        return;
    }

    // 🔒 Block employees from opening manager dashboard
    if (role !== "MANAGER") {
        alert("Unauthorized access! Redirecting...");
        window.location.href = "login.html";
        return;
    }

    // ✅ Show manager info
    const info = document.getElementById("managerInfo");
    if (info && employee.name && employee.id) {
        info.textContent = `Welcome, ${employee.name} (ID: ${employee.id})`;
    }
});

document.addEventListener("DOMContentLoaded", fetchAllLeaves);
document.addEventListener("DOMContentLoaded", () => {
    const manager = JSON.parse(localStorage.getItem("manager"));
    if (manager) {
        document.getElementById("managerInfo").textContent =
            `Welcome, ${manager.name} (ID: ${manager.id})`;
    }
    fetchAllLeaves();
});

async function fetchAllLeaves() {
    try {
        const response = await fetch("/api/leaves");
        const leaves = await response.json();

        const tableBody = document.getElementById("allLeavesBody");
        tableBody.innerHTML = "";

        leaves.forEach(leave => {
            const row = document.createElement("tr");
            row.innerHTML = `
    <td>${leave.employee?.id || "N/A"}</td>
    <td>${leave.employee?.name || "N/A"}</td>
    <td>${leave.leaveType}</td>
    <td>${leave.startDate}</td>
    <td>${leave.endDate}</td>
     <td>${leave.reason}</td>
    <td class="${leave.status.toLowerCase()}">${leave.status}</td>
    <td>
        <button class="action-btn approve" onclick="updateStatus(${leave.id}, 'APPROVED')">Approve</button>
        <button class="action-btn reject" onclick="updateStatus(${leave.id}, 'REJECTED')">Reject</button>
    </td>
`;

            tableBody.appendChild(row);
        });
    } catch (err) {
        console.error("Error fetching leaves:", err);
    }
}

async function updateStatus(id, status) {
    try {
        const response = await fetch(`/api/leaves/${id}/status?status=${status}`, { method: "PUT" });
        if (response.ok) {
            alert(`Leave ${status.toLowerCase()} successfully!`);
            fetchAllLeaves();
        } else {
            alert("Failed to update status.");
        }
    } catch (err) {
        console.error("Error updating status:", err);
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}
