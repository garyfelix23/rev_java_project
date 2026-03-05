const BASE_URL = "http://localhost:7000";

// to prevent
window.addEventListener("DOMContentLoaded", async () => {

    const path = window.location.pathname;

    const response = await fetch(`${BASE_URL}/me`, {
        credentials: "include"
    });

    const isLoggedIn = response.ok;

    // If login page and already logged in → redirect
    if (path.includes("login.html") && isLoggedIn) {
        window.location.href = "dashboard.html";
        return;
    }

    // Redirect to login.html if try to open dashboard.html without login
    if (path.includes("dashboard.html") && !isLoggedIn) {
        window.location.href = "login.html";
        return;
    }

    // if logged in, and try to open login.html, then the page redirects to the dashboard itself.
    if (path.includes("dashboard.html") && isLoggedIn) {

        const user = await response.json();

        const employeeSection = document.getElementById("employeeSection");
        const adminSection = document.getElementById("adminSection");

        employeeSection.style.display = "none";
        adminSection.style.display = "none";

        if (user.role === "ADMIN") {
            adminSection.style.display = "block";
        } else {
            employeeSection.style.display = "block";
        }
    }
});

const form = document.getElementById("loginForm");

// Employee login check
if (form) {
    form.addEventListener("submit", async (e) => {
        e.preventDefault();

        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        try {
            const response = await fetch(`${BASE_URL}/login`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include",
                body: JSON.stringify({
                    email: email,
                    password: password
                })
            });

            const data = await response.text();

            if (response.ok) {
                window.location.href = "dashboard.html";
            } else {
                document.getElementById("message").innerText = data;
            }

        } catch (error) {
            console.log("Login error:", error);
        }
    });
}

// Employee side - to apply leave
async function applyLeave() {

    const start_date = document.getElementById("start_date").value;
    const end_date = document.getElementById("end_date").value;
    const reason = document.getElementById("reason").value;

    try {
        const response = await fetch(`${BASE_URL}/leave`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
            credentials: "include",
            body: JSON.stringify({
                start_date: start_date,
                end_date: end_date,
                reason: reason
            })
        });

        const data = await response.text();
        document.getElementById("leaveMessage").innerText = data;

    } catch (error) {
        console.log("Apply leave error:", error);
    }
}

// Employee side: load all leaves
async function loadMyLeaves() {
    try {
        const response = await fetch(`${BASE_URL}/leave/my`, {
            method: "GET",
            credentials: "include"
        });

        const leaves = await response.json();

        let html = "<ul>";

        leaves.forEach(l => {
            html += `<li>
                ${l.start_date} to ${l.end_date}
                | ${l.reason}
                | ${l.status}
            </li>`;
        });

        html += "</ul>";

        document.getElementById("leaveList").innerHTML = html;

    } catch (error) {
        console.log("Load leaves error:", error);
    }
}

// logout function
async function logout() {
    await fetch("http://localhost:7000/logout", {
        method: "POST",
        credentials: "include"
    });

    window.location.href = "login.html";
}

if (window.location.pathname.includes("dashboard.html")) {

    window.onload = async function () {

        const response = await fetch(`${BASE_URL}/me`, {
            credentials: "include"
        });

        if (!response.ok) {
            window.location.href = "login.html";
            return;
        }

        const user = await response.json();

        const employeeSection = document.getElementById("employeeSection");
        const adminSection = document.getElementById("adminSection");

        // Hide both first
        employeeSection.style.display = "none";
        adminSection.style.display = "none";

        if (user.role === "ADMIN") {
            adminSection.style.display = "block";
        } else {
            employeeSection.style.display = "block";
        }
    };
}

// window.onload = async function (){
//     const response = await fetch("http://localhost:7000/me", {
//         credentials: "include"
//     });
//
//     if(!response.ok){
//         window.location.href = "login.html";
//         return;
//     }
//
//     const user = await response.json();
//
//     if(user.role === "ADMIN"){
//         document.getElementById("adminSection").style.display = "block";
//     }else{
//         document.getElementById("employeeSection").style.display = "block";
//     }
// }

// Admin side - to get all leaves
async function loadAllLeaves(){
    const response = await fetch("http://localhost:7000/admin/leaves", {
        credentials: "include"
    });

    if(!response.ok){
        alert("Access Denied");
        return;
    }

    const leaves = await response.json();

    let html = "<ul>";

    leaves.forEach(l => {
        html += `<li>
        User ID: ${l.user_id} | ${l.start_date} to ${l.end_date} | ${l.reason} | ${l.status}
        <button onclick="updateStatus(${l.id}, 'APPROVED')">Approve</button>
        <button onclick="updateStatus(${l.id}, 'REJECTED')">Reject</button>
        </li>`;
    });

    html += "</ul>";

    document.getElementById("adminLeaveList").innerHTML = html;
}

// update status button function
async function updateStatus(id, status){
    const response = await fetch(`${BASE_URL}/admin/leaves/${id}`, {
        method: "PUT",
        headers: {
            "Content-Type": "application/json"
        },
        credentials: "include",
        body: JSON.stringify({
            status: status
        })
    });

    const result = await response.json();

    alert(result.message || result);

    loadAllLeaves();
}

// signup
// const signUpForm = document.getElementById("signUpForm");
//
// if(signUpForm){
//     signUpForm.addEventListener("submit", async (e) => {
//         e.preventDefault();
//
//         const name = document.getElementById("name").value;
//         const email = document.getElementById("email").value;
//         const password = document.getElementById("password").value;
//
//         try{
//            const response = await fetch(`${BASE_URL}/signup`, {
//                method: "POST",
//                headers: {
//                    "Content-Type": "application/json"
//                },
//                credentials: "include",
//                body: JSON.stringify({
//                    name: name,
//                    email: email,
//                    password: password,
//                })
//            });
//
//            const data = await response.text();
//
//            if(response.ok){
//                alert("User signed up. Please login!!!");
//                window.location.href = "login.html";
//            }else{
//                document.getElementById("message").innerText = data;
//            }
//         }catch (error){
//             console.log(error)
//         }
//     })
// }

const signupForm = document.getElementById("signupForm");

if (signupForm) {
    signupForm.addEventListener("submit", async (e) => {
        e.preventDefault();

        const name = document.getElementById("name").value;
        const email = document.getElementById("email").value;
        const password = document.getElementById("password").value;

        try {
            const response = await fetch(`${BASE_URL}/signup`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify({
                    name: name,
                    email: email,
                    password: password
                })
            });

            const data = await response.text();

            if (response.ok) {
                alert("Registration successful. Please login.");
                window.location.href = "login.html";
            } else {
                document.getElementById("message").innerText = data;
            }

        } catch (error) {
            console.log("Signup error:", error);
        }
    });
}