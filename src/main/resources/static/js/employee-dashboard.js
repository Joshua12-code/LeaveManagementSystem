document.addEventListener("DOMContentLoaded", () => {
    const employee = JSON.parse(localStorage.getItem("employee"));
    const role = localStorage.getItem("role");

    if (!employee || !role) {
        alert("Access denied! Please login first.");
        window.location.href = "login.html";
        return;
    }

    // 🔒 Block managers from accessing employee dashboard
    if (role !== "EMPLOYEE") {
        alert("Unauthorized access! Redirecting...");
        window.location.href = "login.html";
        return;
    }

    // ✅ Show employee info if needed
    const info = document.getElementById("employeeInfo");
    if (info && employee.name && employee.id) {
        info.textContent = `Welcome, ${employee.name} (ID: ${employee.id})`;
    }
});

document.addEventListener("DOMContentLoaded", () => {
    loadEmployeeInfo();
    loadLeaveHistory();
});

// ✅ Load Employee Info from localStorage
function loadEmployeeInfo() {
    const employee = JSON.parse(localStorage.getItem("employee"));

    if (employee && employee.id) {
        document.getElementById("employeeName").textContent = `Name: ${employee.name}`;
        document.getElementById("employeeId").textContent = `ID: ${employee.id}`;
    } else {
        document.getElementById("employeeName").textContent = "Name: Unknown";
        document.getElementById("employeeId").textContent = "ID: Unknown";
    }
}

// ✅ Apply Leave Function
async function applyLeave() {
    const employeeId = localStorage.getItem("employeeId");
    const leaveType = document.getElementById("leaveType").value.trim();
    const fromDate = document.getElementById("fromDate").value;
    const toDate = document.getElementById("toDate").value;
    const reason = document.getElementById("reason").value.trim();

    if (!employeeId) {
        alert("Employee not logged in!");
        window.location.href = "login.html";
        return;
    }

    const leaveData = { employeeId, leaveType, fromDate, toDate, reason };

    try {
        const response = await fetch("/api/leaves/apply", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(leaveData)
        });

        const data = await response.json();

        if (response.ok && data.status === "success") {
            alert("✅ Leave applied successfully!");
            document.getElementById("leaveForm").reset();
            loadLeaveHistory();
        } else {
            alert(data.message || "❌ Failed to apply leave.");
        }
    } catch (error) {
        console.error("❌ Error applying leave:", error);
        alert("❌ Failed to apply leave. Please check console for details.");
    }
}

// ✅ Load Leave History for Logged-in Employee
async function loadLeaveHistory() {
    const employeeId = localStorage.getItem("employeeId");
    if (!employeeId) return;

    try {
        const response = await fetch(`/api/leaves/employee/${employeeId}`);
        const leaves = await response.json();

        const tableBody = document.getElementById("leaveHistoryBody");
        tableBody.innerHTML = "";

        if (!Array.isArray(leaves) || leaves.length === 0) {
            tableBody.innerHTML = `<tr><td colspan="5">No leave records found</td></tr>`;
            return;
        }

        leaves.forEach(l => {
            tableBody.innerHTML += `
                <tr>
                    <td>${l.id}</td>
                    <td>${l.leaveType}</td>
                    <td>${l.startDate}</td>
                    <td>${l.endDate}</td>
                    <td>${l.reason || '-'}</td>
                    <td><span class="${l.status?.toLowerCase() || 'pending'}">${l.status}</span></td>
                </tr>
            `;
        });
    } catch (error) {
        console.error("Error loading leave history:", error);
    }
}

// ✅ Logout
function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}
