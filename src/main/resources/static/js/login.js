async function loginUser() {
    const email = document.getElementById("email").value.trim();
    const password = document.getElementById("password").value.trim();

    const response = await fetch("/api/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    const data = await response.json();

    if (data.status === "success") {
        localStorage.setItem("employeeId", data.employeeId);
        localStorage.setItem("role", data.role);

        // ✅ Fetch full employee details after login
        try {
            const empResponse = await fetch(`/api/employees/${data.employeeId}`);
            const empData = await empResponse.json();

            // ✅ Save full employee object
            localStorage.setItem("employee", JSON.stringify(empData));
        } catch (error) {
            console.error("⚠️ Failed to fetch employee details:", error);
        }

        // ✅ Redirect based on role
        if (data.role === "MANAGER") {
            window.location.href = "manager-dashboard.html";
        } else {
            window.location.href = "employee-dashboard.html";
        }
    } else {
        alert("Invalid login credentials!");
    }
}

function logout() {
    localStorage.clear();
    window.location.href = "login.html";
}
