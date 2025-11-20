// =============================================================================
// MateZone Website JavaScript
// =============================================================================

document.addEventListener('DOMContentLoaded', function () {
	initNavigation();
	initScrollEffects();
	initFormValidation();
	initCodeCopy();
	initTooltips();
});

// Navigation mobile
function initNavigation() {
	const hamburger = document.querySelector('.hamburger');
	const navMenu = document.querySelector('.nav-menu');

	if (hamburger && navMenu) {
		hamburger.addEventListener('click', function () {
			hamburger.classList.toggle('active');
			navMenu.classList.toggle('active');
		});

		// Fermer le menu lors du clic sur un lien
		document.querySelectorAll('.nav-link').forEach(link => {
			link.addEventListener('click', () => {
				hamburger.classList.remove('active');
				navMenu.classList.remove('active');
			});
		});
	}

	// Marquer le lien actif
	const currentPage = window.location.pathname.split('/').pop() || 'index.html';
	document.querySelectorAll('.nav-link').forEach(link => {
		if (link.getAttribute('href') === currentPage ||
			(currentPage === '' && link.getAttribute('href') === 'index.html')) {
			link.classList.add('active');
		}
	});
}

// Effets de scroll
function initScrollEffects() {
	// Animation des éléments au scroll
	const observerOptions = {
		threshold: 0.1,
		rootMargin: '0px 0px -50px 0px'
	};

	const observer = new IntersectionObserver(function (entries) {
		entries.forEach(entry => {
			if (entry.isIntersecting) {
				entry.target.classList.add('fade-in');
			}
		});
	}, observerOptions);

	// Observer les cartes et sections
	document.querySelectorAll('.card, .section').forEach(el => {
		observer.observe(el);
	});

	// Smooth scroll pour les liens d'ancrage
	document.querySelectorAll('a[href^="#"]').forEach(anchor => {
		anchor.addEventListener('click', function (e) {
			e.preventDefault();
			const target = document.querySelector(this.getAttribute('href'));
			if (target) {
				target.scrollIntoView({
					behavior: 'smooth',
					block: 'start'
				});
			}
		});
	});
}

// Validation des formulaires
function initFormValidation() {
	const forms = document.querySelectorAll('form');

	forms.forEach(form => {
		form.addEventListener('submit', function (e) {
			let isValid = true;
			const requiredFields = form.querySelectorAll('[required]');

			requiredFields.forEach(field => {
				if (!field.value.trim()) {
					isValid = false;
					showFieldError(field, 'Ce champ est requis');
				} else {
					clearFieldError(field);
				}

				// Validation email
				if (field.type === 'email' && field.value) {
					const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
					if (!emailRegex.test(field.value)) {
						isValid = false;
						showFieldError(field, 'Adresse email invalide');
					}
				}
			});

			if (!isValid) {
				e.preventDefault();
			}
		});
	});
}

function showFieldError(field, message) {
	clearFieldError(field);

	const errorDiv = document.createElement('div');
	errorDiv.className = 'field-error';
	errorDiv.style.color = '#e74c3c';
	errorDiv.style.fontSize = '0.875rem';
	errorDiv.style.marginTop = '0.25rem';
	errorDiv.textContent = message;

	field.style.borderColor = '#e74c3c';
	field.parentNode.appendChild(errorDiv);
}

function clearFieldError(field) {
	const existingError = field.parentNode.querySelector('.field-error');
	if (existingError) {
		existingError.remove();
	}
	field.style.borderColor = '';
}

// Copie du code
function initCodeCopy() {
	document.querySelectorAll('.code-block').forEach(codeBlock => {
		const button = document.createElement('button');
		button.className = 'copy-btn';
		button.innerHTML = '📋 Copier';
		button.style.cssText = `
            position: absolute;
            top: 10px;
            right: 10px;
            background: #3498db;
            color: white;
            border: none;
            padding: 5px 10px;
            border-radius: 4px;
            cursor: pointer;
            font-size: 0.8rem;
        `;

		codeBlock.style.position = 'relative';
		codeBlock.appendChild(button);

		button.addEventListener('click', function () {
			const code = codeBlock.querySelector('pre, code') || codeBlock;
			const text = code.textContent;

			navigator.clipboard.writeText(text).then(() => {
				button.innerHTML = '✅ Copié!';
				setTimeout(() => {
					button.innerHTML = '📋 Copier';
				}, 2000);
			}).catch(() => {
				// Fallback pour les navigateurs plus anciens
				const textArea = document.createElement('textarea');
				textArea.value = text;
				document.body.appendChild(textArea);
				textArea.select();
				document.execCommand('copy');
				document.body.removeChild(textArea);

				button.innerHTML = '✅ Copié!';
				setTimeout(() => {
					button.innerHTML = '📋 Copier';
				}, 2000);
			});
		});
	});
}

// Tooltips
function initTooltips() {
	document.querySelectorAll('[data-tooltip]').forEach(element => {
		element.addEventListener('mouseenter', function () {
			const tooltip = document.createElement('div');
			tooltip.className = 'tooltip';
			tooltip.textContent = this.getAttribute('data-tooltip');
			tooltip.style.cssText = `
                position: absolute;
                background: #2c3e50;
                color: white;
                padding: 5px 10px;
                border-radius: 4px;
                font-size: 0.875rem;
                z-index: 1000;
                pointer-events: none;
                white-space: nowrap;
            `;

			document.body.appendChild(tooltip);

			const rect = this.getBoundingClientRect();
			tooltip.style.top = (rect.top - tooltip.offsetHeight - 5) + 'px';
			tooltip.style.left = (rect.left + rect.width / 2 - tooltip.offsetWidth / 2) + 'px';

			this.tooltip = tooltip;
		});

		element.addEventListener('mouseleave', function () {
			if (this.tooltip) {
				this.tooltip.remove();
				this.tooltip = null;
			}
		});
	});
}

// Utilitaires
function showAlert(message, type = 'info') {
	const alert = document.createElement('div');
	alert.className = `alert alert-${type}`;
	alert.textContent = message;
	alert.style.cssText = `
        position: fixed;
        top: 100px;
        right: 20px;
        z-index: 1001;
        min-width: 300px;
        max-width: 500px;
    `;

	document.body.appendChild(alert);

	setTimeout(() => {
		alert.remove();
	}, 5000);
}

// Animation au scroll pour les compteurs
function animateCounters() {
	document.querySelectorAll('.counter').forEach(counter => {
		const target = parseInt(counter.getAttribute('data-target'));
		const increment = target / 50;
		let current = 0;

		const timer = setInterval(() => {
			current += increment;
			if (current >= target) {
				current = target;
				clearInterval(timer);
			}
			counter.textContent = Math.floor(current);
		}, 50);
	});
}

// Initialiser les compteurs quand ils sont visibles
const counterObserver = new IntersectionObserver((entries) => {
	entries.forEach(entry => {
		if (entry.isIntersecting) {
			animateCounters();
			counterObserver.unobserve(entry.target);
		}
	});
});

document.querySelectorAll('.counter').forEach(counter => {
	counterObserver.observe(counter);
});

// Gestion du thème sombre (optionnel)
function initThemeToggle() {
	const themeToggle = document.querySelector('.theme-toggle');
	if (themeToggle) {
		themeToggle.addEventListener('click', function () {
			document.body.classList.toggle('dark-theme');
			localStorage.setItem('darkTheme', document.body.classList.contains('dark-theme'));
		});

		// Charger le thème sauvegardé
		if (localStorage.getItem('darkTheme') === 'true') {
			document.body.classList.add('dark-theme');
		}
	}
}

// Recherche dans la documentation
function initSearch() {
	const searchInput = document.querySelector('.search-input');
	const searchResults = document.querySelector('.search-results');

	if (searchInput && searchResults) {
		searchInput.addEventListener('input', function () {
			const query = this.value.toLowerCase();

			if (query.length < 2) {
				searchResults.innerHTML = '';
				return;
			}

			// Simulation de recherche (à remplacer par une vraie recherche)
			const mockResults = [
				{ title: 'Installation', url: 'guide.html#installation', excerpt: 'Guide d\'installation de MateZone...' },
				{ title: 'Architecture', url: 'architecture.html', excerpt: 'Description de l\'architecture...' },
				{ title: 'Base de données', url: 'database.html', excerpt: 'Configuration de la base de données...' }
			].filter(item => item.title.toLowerCase().includes(query));

			searchResults.innerHTML = mockResults.map(item => `
                <div class="search-result">
                    <h4><a href="${item.url}">${item.title}</a></h4>
                    <p>${item.excerpt}</p>
                </div>
            `).join('');
		});
	}
}

// Export pour usage externe
window.MateZone = {
	showAlert,
	animateCounters,
	initThemeToggle,
	initSearch
};