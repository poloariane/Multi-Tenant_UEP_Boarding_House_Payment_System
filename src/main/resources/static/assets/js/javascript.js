document.addEventListener("DOMContentLoaded", () => {
    const tenants = JSON.parse(localStorage.getItem('tenants')) || {};
    const total = Object.values(tenants).reduce((sum, roomList) => sum + roomList.length, 0);

    const totalTenantsBox = document.querySelector('.box p');
    if (totalTenantsBox) {
        totalTenantsBox.textContent = total;
    }

    const now = new Date();
    const year = now.getFullYear();
    const month = now.getMonth();
    const daysInMonth = new Date(year, month + 1, 0).getDate();
    const dueStartDay = Math.max(1, daysInMonth - 6);
    const dueDate = new Date(year, month, dueStartDay);
    const formatted = `${dueDate.toLocaleString('default', { month: 'long' })} ${dueDate.getDate()}, ${year}`;

    const upcomingBox = document.querySelectorAll('.box p')[2];
    if (upcomingBox) {
        upcomingBox.textContent = formatted;
    }

    const role = localStorage.getItem('role') || 'tenant';
    const boxes = document.querySelectorAll('.box');
    if (role === 'tenant') {
        const allowed = [0, 1, 2, 3];
        boxes.forEach((box, index) => {
            if (!allowed.includes(index)) {
                box.style.display = 'none';
            }
        });
    }

    const reportsBtn = document.querySelector('a[href="reports.html"]');
    const notifBtn = document.querySelector('a[href="notifications.html"]');
    if (role === 'tenant') {
        if (reportsBtn) reportsBtn.style.display = 'none';
        if (notifBtn) notifBtn.style.display = 'none';
    }

    const adminBtn = document.querySelector('.admin-btn');
    if (adminBtn) {
        adminBtn.textContent = role === 'tenant' ? 'Profile' : 'Admin';
        adminBtn.href = 'profile.html';
    }

    if (role === 'admin') {
        const payRentLink = document.querySelector('a[href="pay-rent.html"]');
        if (payRentLink) {
            const icon = payRentLink.querySelector('i');
            payRentLink.textContent = 'Tenant Payments';
            if (icon) payRentLink.prepend(icon);
        }
    }
});

const ham = document.getElementById('hamburger');
const side = document.getElementById('sidebar');
if (ham && side) {
    ham.addEventListener('click', () => side.classList.toggle('show'));
}