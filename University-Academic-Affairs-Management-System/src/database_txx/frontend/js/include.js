// js/include.js
function includeHTML() {
    const includeElements = document.querySelectorAll('[data-include]');
    includeElements.forEach(el => {
        const file = el.getAttribute('data-include');
        if (file) {
            fetch(file)
                .then(response => response.text())
                .then(data => {
                    el.innerHTML = data;
                })
                .catch(err => {
                    el.innerHTML = `<div style="color:red;">加载失败: ${file}</div>`;
                });
        }
    });
}
