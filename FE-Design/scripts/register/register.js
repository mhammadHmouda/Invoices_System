import { DELAY, REGISTER_URL } from "../../settings/settings.js";

let token = localStorage.getItem("token");

if (token) {
  window.location.href = "item.html";
}

function registerUser(e) {
  e.preventDefault();
  console.log("Hello!");
  const form = e.target;
  const user = {
    username: form.username.value,
    password: form.password.value,
    email: form.email.value,
  };

  $.ajax({
    url: REGISTER_URL,
    type: "POST",
    contentType: 'application/json',
    data: JSON.stringify(user),
    success: (data) => {
      const message = `<div class="alert alert-success" role="alert">${data}</div>`;
      $("#message").append(message);
      setTimeout(() => {
        window.location.href = "../../pages/login.html";
        $("#message").empty();
      }, DELAY);
    },
    error: (err) => {
      console.log(err.responseText);
      const message = `<div class="alert alert-danger" role="alert">${err.responseText}</div>`;
      $("#error").append(message);
      setTimeout(() => {
        $("#error").empty();
      }, DELAY);
    },
  });
}

$("#register-form").submit(registerUser);
