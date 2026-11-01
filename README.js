// Wait until the page is fully loaded
document.addEventListener("DOMContentLoaded", () => {

    // Smooth scrolling for navigation links
    const navigationLinks = document.querySelectorAll(
        '.nav-links a, .nav-button, .hero-buttons a'
    );

    navigationLinks.forEach(link => {

        link.addEventListener("click", function (event) {

            const targetId = this.getAttribute("href");

            if (targetId && targetId.startsWith("#")) {

                const target = document.querySelector(targetId);

                if (target) {
                    event.preventDefault();

                    target.scrollIntoView({
                        behavior: "smooth"
                    });
                }
            }
        });
    });


    // Reveal sections when they enter the screen
    const revealElements = document.querySelectorAll(
        ".section-heading, .info-card, .feature-card, .tech-card, .workflow-step, .future-grid div"
    );

    revealElements.forEach(element => {
        element.classList.add("reveal");
    });

    const observer = new IntersectionObserver((entries) => {

        entries.forEach(entry => {

            if (entry.isIntersecting) {

                entry.target.classList.add("active");

                observer.unobserve(entry.target);
            }
        });

    }, {
        threshold: 0.15
    });

    revealElements.forEach(element => {
        observer.observe(element);
    });


    // Update navigation appearance while scrolling
    const sections = document.querySelectorAll("main section[id]");

    window.addEventListener("scroll", () => {

        let currentSection = "";

        sections.forEach(section => {

            const sectionTop = section.offsetTop - 150;

            if (window.scrollY >= sectionTop) {
                currentSection = section.getAttribute("id");
            }
        });

        navigationLinks.forEach(link => {

            link.classList.remove("active-link");

            if (link.getAttribute("href") === "#" + currentSection) {
                link.classList.add("active-link");
            }
        });

    });

});