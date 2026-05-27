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

        // ✅ Store values
        localStorage.setItem("employeeId", data.employeeId);
        localStorage.setItem("role", data.role);
        localStorage.setItem("token", data.token);

        // ✅ Fetch employee details with JWT token
        try {

            const empResponse = await fetch(`/api/employees/${data.employeeId}`, {
                headers: {
                    "Authorization": "Bearer " + data.token
                }
            });

            const empData = await empResponse.json();

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