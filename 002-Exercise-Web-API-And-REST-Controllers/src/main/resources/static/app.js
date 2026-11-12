const API = '/api/books';
const tbody = document.getElementById('books');
const form = document.getElementById('bookForm');
const fields = ['title', 'author', 'isbn', 'tags'].reduce((acc, id) => ({ ...acc, [id]: document.getElementById(id) }), {});
const submitBtn = document.getElementById('submit');
const cancelBtn = document.getElementById('cancel');
let editingId = null;

document.getElementById('loadBooks').addEventListener('click', loadBooks);
form.addEventListener('submit', saveBook);
cancelBtn.addEventListener('click', resetForm);
loadBooks();

async function request(url, options = {}) {
    const response = await fetch(url, { headers: { 'Content-Type': 'application/json' }, ...options });
    if (!response.ok) {
        throw new Error(`${options.method || 'GET'} ${url} failed with ${response.status}`);
    }
    return response.status === 204 ? null : response.json();
}

async function loadBooks() {
    const books = await request(API);
    tbody.replaceChildren(...books.map(renderBook));
}

function renderBook(book) {
    const tr = document.createElement('tr');
    for (const value of [book.title, book.author, book.isbn, book.tags.join(', ')]) {
        const td = document.createElement('td');
        td.textContent = value; // textContent, not innerHTML: book data is user input
        tr.appendChild(td);
    }
    const actions = document.createElement('td');
    actions.append(button('Edit', () => startEdit(book)), button('Delete', () => deleteBook(book.id)));
    tr.appendChild(actions);
    return tr;
}

function button(text, onClick) {
    const btn = document.createElement('button');
    btn.textContent = text;
    btn.addEventListener('click', onClick);
    return btn;
}

function startEdit(book) {
    editingId = book.id;
    fields.title.value = book.title;
    fields.author.value = book.author;
    fields.isbn.value = book.isbn;
    fields.tags.value = book.tags.join(', ');
    submitBtn.textContent = 'Edit';
    cancelBtn.hidden = false;
}

function resetForm() {
    editingId = null;
    form.reset();
    submitBtn.textContent = 'Submit';
    cancelBtn.hidden = true;
}

async function saveBook(event) {
    event.preventDefault();
    const book = {
        title: fields.title.value.trim(),
        author: fields.author.value.trim(),
        isbn: fields.isbn.value.trim(),
        tags: fields.tags.value.split(',').map(t => t.trim()).filter(Boolean),
    };
    if (editingId === null) {
        await request(API, { method: 'POST', body: JSON.stringify(book) });
    } else {
        await request(`${API}/${editingId}`, { method: 'PUT', body: JSON.stringify(book) });
    }
    resetForm();
    await loadBooks();
}

async function deleteBook(id) {
    await request(`${API}/${id}`, { method: 'DELETE' });
    if (editingId === id) {
        resetForm();
    }
    await loadBooks();
}
