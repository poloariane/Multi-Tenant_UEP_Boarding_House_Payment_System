import {firebaseConfig} from "./firebaseConfig.js"
import { initializeApp } from "https://www.gstatic.com/firebasejs/11.0.1/firebase-app.js";
import {
  getDatabase,
  ref,
  set,
  push,
  get,
  update,
  remove,
  onValue,
} from "https://www.gstatic.com/firebasejs/11.0.1/firebase-database.js";

const app = initializeApp(firebaseConfig);
const db = getDatabase(app);

const fullName = document.getElementById("fullname");
const email = document.getElementById("email");
const number = document.getElementById("number");
const password = document.getElementById("password");
const role = document.getElementById("role");
const cardContainer = document.getElementById("cardContainer");

// CREATE USER
document.getElementById("submit").onclick = function () {
  if (!fullName.value || !email.value || !number.value || !password.value || !role.value) {
    alert("Please fill in all fields.");
    return;
  }

  const userRef = ref(db, "users");
  const newUserRef = push(userRef);

  set(newUserRef, {
    id: newUserRef.key,
    name: fullName.value,
    email: email.value,
    number: number.value,
    password: password.value,
    role: role.value,
  }).then(() => {
    alert("User added");
    clearFields();
  });
};

// READ + SHOW USERS IN CARDS
function loadUsers() {
  const userRef = ref(db, "users");

  onValue(userRef, (snapshot) => {
    cardContainer.innerHTML = "";

    snapshot.forEach((child) => {
      const user = child.val();
      createCard(user);
    });
  });
}

// CREATE CARD UI
function createCard(user) {
  const card = document.createElement("div");
  card.classList.add("card");

  card.innerHTML = `
    <h3>${user.name}</h3>
    <p><b>Email:</b> ${user.email}</p>
    <p><b>Number:</b> ${user.number}</p>
    <p><b>Role:</b> ${user.role}</p>

    <div class="icons">
      <span class="edit" data-id="${user.id}">✏️</span>
      <span class="delete" data-id="${user.id}">🗑️</span>
    </div>
  `;

  // Edit button
  card.querySelector(".edit").onclick = () => editUser(user);

  // Delete button
  card.querySelector(".delete").onclick = () => deleteUser(user.id);

  cardContainer.appendChild(card);
}

// EDIT USER
function editUser(user) {
  fullName.value = user.name;
  email.value = user.email;
  number.value = user.number;
  password.value = user.password;
  role.value = user.role;

  document.getElementById("submit").innerText = "Update";
  document.getElementById("submit").onclick = function () {
    update(ref(db, "users/" + user.id), {
      name: fullName.value,
      email: email.value,
      number: number.value,
      password: password.value,
      role: role.value,
    }).then(() => {
      alert("User updated");
      clearFields();
      document.getElementById("submit").innerText = "Submit";
    });
  };
}

// DELETE USER
function deleteUser(id) {
  remove(ref(db, "users/" + id))
    .then(() => alert("User deleted"));
}

// CLEAR INPUT FIELDS
function clearFields() {
  fullName.value = "";
  email.value = "";
  number.value = "";
  password.value = "";
  role.value = "";
}

// Load data initially
loadUsers();
