document.addEventListener("DOMContentLoaded", () => {

    const employee = JSON.parse(localStorage.getItem("employee"));
    const role = localStorage.getItem("role");
    const token = localStorage.getItem("token");

    // 🔒 Block access if not logged in
    if (!employee || !role || !token) {

        alert("Access denied! Please login first.");

        window.location.href = "login.html";
        return;
    }

    // 🔒 Only managers allowed
    if (role !== "MANAGER") {

        alert("Unauthorized access! Redirecting...");

        window.location.href = "login.html";
        return;
    }

    // ✅ Show manager info
    const info = document.getElementById("managerInfo");

    if (info && employee.name && employee.id) {

        info.textContent =
            `Welcome, ${employee.name} (ID: ${employee.id})`;
    }

    // ✅ Load leaves
    fetchAllLeaves();
});


// ✅ Fetch all leave requests
async function fetchAllLeaves() {

    const token = localStorage.getItem("token");

    try {

        const response = await fetch("/api/leaves", {

            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        const leaves = await response.json();

        const tableBody =
            document.getElementById("allLeavesBody");

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
                <td class="${leave.status.toLowerCase()}">
                    ${leave.status}
                </td>
                <td>
                    <button
                        class="action-btn approve"
                        onclick="updateStatus(${leave.id}, 'APPROVED')">
                        Approve
                    </button>

                    <button
                        class="action-btn reject"
                        onclick="updateStatus(${leave.id}, 'REJECTED')">
                        Reject
                    </button>
                </td>
            `;

            tableBody.appendChild(row);
        });

    } catch (err) {

        console.error("Error fetching leaves:", err);
    }
}


// ✅ Update leave status
async function updateStatus(id, status) {

    const token = localStorage.getItem("token");

    try {

        const response = await fetch(
            `/api/leaves/${id}/status?status=${status}`,
            {
                method: "PUT",

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );

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


// ✅ Logout
function logout() {

    localStorage.clear();

    window.location.href = "login.html";
}