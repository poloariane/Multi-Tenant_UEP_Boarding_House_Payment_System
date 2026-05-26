document.addEventListener("DOMContentLoaded", () => {
  const sidebar = document.getElementById("sidebar");
  const navLinks = document.querySelectorAll(".nav a, .sidebar a");

  if (!sidebar || navLinks.length === 0) {
    return;
  }

  function normalizePath(href) {
    if (!href) return "";
    return href.replace(/^.*[\\/]/, "").toLowerCase();
  }

  function setActiveLink(href) {
    const normalizedTarget = normalizePath(href);
    navLinks.forEach(link => {
      const normalizedLink = normalizePath(link.getAttribute("href"));
      if (normalizedLink === normalizedTarget) {
        link.classList.add("active");
      } else {
        link.classList.remove("active");
      }
    });
  }

  navLinks.forEach(link => {
    link.addEventListener("click", () => {
      navLinks.forEach(l => l.classList.remove("active"));
      link.classList.add("active");
      localStorage.setItem("activeSidebarLink", normalizePath(link.getAttribute("href")));

      if (window.innerWidth <= 768) {
        sidebar.classList.add("hidden");
      }
    });
  });

  const savedActive = localStorage.getItem("activeSidebarLink");
  if (savedActive) {
    setActiveLink(savedActive);
  } else {
    setActiveLink(window.location.pathname.split("/").pop());
  }

  window.addEventListener("resize", () => {
    if (window.innerWidth > 768) {
      sidebar.classList.remove("hidden");
    }
  });
});
