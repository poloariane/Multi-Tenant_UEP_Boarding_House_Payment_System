(function () {
    const publicPages = new Set([
        "",
        "index.html",
        "login.html",
        "admin-signup.html",
        "logout.html"
    ]);

    const currentPage = window.location.pathname.split("/").pop();
    if (publicPages.has(currentPage)) {
        return;
    }

    function readJson(key) {
        try {
            return JSON.parse(localStorage.getItem(key) || "null");
        } catch {
            return null;
        }
    }

    const hasBackendSession = !!localStorage.getItem("authToken");
    const role = localStorage.getItem("role");
    const hasLocalAdminSession = role === "admin" && !!readJson("activeAdmin");
    const hasLocalTenantSession = role === "tenant" && !!readJson("activeTenant");

    if (!hasBackendSession && !hasLocalAdminSession && !hasLocalTenantSession) {
        const target = window.location.pathname + window.location.search + window.location.hash;
        window.location.replace("login.html?redirect=" + encodeURIComponent(target));
    }
})();
