document.addEventListener("DOMContentLoaded", function () {
    const rowsPerPage = 8;
    const tableBody = document.getElementById('tableBody');
    const rows = Array.from(tableBody.getElementsByTagName('tr'));
    const pagination = document.querySelector('.pagination');
    const prevBtn = document.getElementById('prevBtn');
    const nextBtn = document.getElementById('nextBtn');

    let currentPage = 1;

    function displayPage(page) {
        const startIndex = (page - 1) * rowsPerPage;
        const endIndex = startIndex + rowsPerPage;

        rows.forEach((row, index) => {
            row.style.display = (index >= startIndex && index < endIndex) ? '' : 'none';
        });

        updatePagination(page);
    }

    function updatePagination(currentPage) {
        const totalPages = Math.ceil(rows.length / rowsPerPage);

        // Clear old page numbers
        while (pagination.children.length > 2) {
            pagination.removeChild(pagination.children[1]);
        }

        // Display only 3 pages at a time
        let startPage = Math.max(1, currentPage - 1);
        let endPage = Math.min(totalPages, startPage + 2);

        if (endPage - startPage < 2 && startPage > 1) {
            startPage = Math.max(1, endPage - 2);
        }

        for (let i = startPage; i <= endPage; i++) {
            const li = document.createElement('li');
            li.className = `page-item ${i === currentPage ? 'active' : ''}`;
            li.innerHTML = `<a class="page-link" href="#">${i}</a>`;

            li.addEventListener('click', function () {
                currentPage = i;
                displayPage(currentPage);
            });

            pagination.insertBefore(li, nextBtn);
        }

        // Disable prev/next at boundaries
        prevBtn.classList.toggle('disabled', currentPage === 1);
        nextBtn.classList.toggle('disabled', currentPage === totalPages);
    }

    prevBtn.addEventListener('click', function () {
        if (currentPage > 1) {
            currentPage--;
            displayPage(currentPage);
        }
    });

    nextBtn.addEventListener('click', function () {
        const totalPages = Math.ceil(rows.length / rowsPerPage);
        if (currentPage < totalPages) {
            currentPage++;
            displayPage(currentPage);
        }
    });

    displayPage(1);
});