const togglePassword = document.getElementById('togglePassword');
    const passwordInput = document.getElementById('password');

    togglePassword.addEventListener('click', function () {
        const type = passwordInput.type === 'password' ? 'text' : 'password';
        passwordInput.type = type;

        // Toggle eye icon
        this.innerHTML = type === 'password'
            ? '<i class="fa-solid fa-lock"></i>'
            : '<i class="fa-solid fa-lock-open"></i>';
    });
