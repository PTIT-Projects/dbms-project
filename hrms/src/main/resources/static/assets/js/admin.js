// Dashboard functionality
document.addEventListener('DOMContentLoaded', function() {
    console.log('Dashboard initialized');

    // Toggle sidebar on mobile
    const sidebarToggle = document.querySelector('.navbar-toggler');
    if (sidebarToggle) {
        sidebarToggle.addEventListener('click', function() {
            document.querySelector('.sidebar').classList.toggle('show');
        });
    }

    // Highlight active navigation item
    const currentPath = window.location.pathname;
    const navLinks = document.querySelectorAll('.sidebar .nav-link');

    navLinks.forEach(link => {
        const href = link.getAttribute('href');
        if (href === currentPath ||
            (currentPath.includes(href) && href !== '/')) {
            link.classList.add('active');
        }
    });
});