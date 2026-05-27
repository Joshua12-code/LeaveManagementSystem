document.addEventListener("DOMContentLoaded", () => {

const employee = JSON.parse(localStorage.getItem("employee"));
const employeeId = employee?.id;
const token = localStorage.getItem("token");

if (!employeeId || !token) {
    alert("Employee not logged in");
    window.location.href = "login.html";
    return;
}

/* ----------- FIRST CHART : Leave Usage ----------- */

fetch(`/api/leaves/leave-summary/${employeeId}`, {
    headers: {
        "Authorization": `Bearer ${token}`
    }
})
.then(res => res.json())
.then(data => {

    const total = data.taken + data.remaining;

    /* SHOW SUMMARY VALUES */

    document.getElementById("totalLeave").innerText =
        "Total Leave: " + total;

    document.getElementById("takenLeave").innerText =
        "Taken Leave: " + data.taken;

    document.getElementById("remainingLeave").innerText =
        "Remaining Leave: " + data.remaining;

    document.getElementById("pendingLeave").innerText =
        "Pending Leave: " + data.pending;

    const ctx = document.getElementById("leaveChart");

    new Chart(ctx, {
        type: "doughnut",

        data: {
            labels: ["Taken", "Remaining", "Pending"],
            datasets: [{
                data: [data.taken, data.remaining, data.pending],
                backgroundColor: [
                    "#4CAF50",
                    "#2196F3",
                    "#FFC107"
                ]
            }]
        },

        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: "bottom"
                }
            }
        }
    });

})
.catch(err => console.error("Error loading leave summary:", err));


/* ----------- SECOND CHART : Leave Type Distribution ----------- */

fetch(`/api/leaves/leave-types/${employeeId}`, {
    headers: {
        "Authorization": `Bearer ${token}`
    }
})
.then(res => res.json())
.then(data => {

    const ctx2 = document.getElementById("leaveTypeChart");

    new Chart(ctx2, {
        type: "doughnut",

        data: {
            labels: Object.keys(data),
            datasets: [{
                data: Object.values(data),
                backgroundColor: [
                    "#FF6384",
                    "#36A2EB",
                    "#FFCE56",
                    "#4CAF50"
                ]
            }]
        },

        options: {
            responsive: true,
            plugins: {
                legend: {
                    position: "bottom"
                }
            }
        }
    });

})
.catch(err => console.error("Error loading leave types:", err));


/* ----------- MONTHLY LEAVE TREND ----------- */

fetch(`/api/leaves/monthly-leaves/${employeeId}`, {
    headers: {
        "Authorization": `Bearer ${token}`
    }
})
.then(res => res.json())
.then(data => {

    const ctx3 = document.getElementById("monthlyLeaveChart");

    new Chart(ctx3, {
        type: "bar",

        data: {
            labels: Object.keys(data),
            datasets: [{
                label: "Leaves Taken",
                data: Object.values(data)
            }]
        },

        options: {
            responsive: true,
            plugins: {
                legend: {
                    display: true
                }
            }
        }
    });

})
.catch(err => console.error("Error loading monthly leaves:", err));

});